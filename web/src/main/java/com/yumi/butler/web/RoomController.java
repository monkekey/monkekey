package com.yumi.butler.web;

import com.yumi.butler.common.RequestResult;
import com.yumi.butler.domain.*;
import com.yumi.butler.domain.IRoomDao;
import com.yumi.butler.domain.Room;
import com.yumi.butler.utils.HeaderUtils;
import com.yumi.butler.domain.IRoomRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.yumi.butler.constant.CommonFlag;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by zy on 7/15/2017.
 */
@RestController
@RequestMapping("/sys/room")
@Api(value = "RoomController", description = "房间相关接口")
public class RoomController {
    @Autowired
    private IRoomRepository iRoomRepository;
    @Autowired
    private IRoomDao iRoomDao;
    @Autowired
    @Qualifier("entityManagerFactoryButler")
    private EntityManager entityManager;

    @ApiOperation(value = "Room|列表")
    @GetMapping("")
    public RequestResult list(@RequestParam(value = "innCode", required = false, defaultValue = "") final String innCode,
                              @RequestParam(value = "roomNo", required = false, defaultValue = "") final String roomNo,
                              @RequestParam(value = "pageIdx", required = false, defaultValue = "0") Integer pageIdx,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Pageable pageable = new PageRequest(pageIdx, pageSize, Sort.Direction.DESC, "id");

        Specification<Room> spec = new Specification<Room>() {
            public Predicate toPredicate(Root<Room> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
            List<Predicate> predicates = new ArrayList<Predicate>();
            Predicate p = cb.equal(root.get("flag").as(Integer.class), CommonFlag.VALID.getValue());
            predicates.add(p);

            if(!StringUtils.isEmpty(innCode)){
                p = cb.equal(root.get("innCode").as(String.class), innCode);
                predicates.add(p);
            }

            if(!StringUtils.isEmpty(roomNo)){
                p = cb.like(root.get("roomNo").as(String.class), "%"+roomNo+"%");
                predicates.add(p);
            }

            Predicate[] props = new Predicate[predicates.size()];
            predicates.toArray(props);
            query.where(props);
            return query.getRestriction();
            }
        };

        return RequestResult.success(iRoomDao.findAll(spec,pageable));
    }

    @ApiOperation(value = "Room|详情")
    @GetMapping("/{roomId}")
    public RequestResult getByRoomCode(@NotNull @PathVariable("roomId") long roomId) {
        Room room = iRoomRepository.findOne(roomId);

        if(null == room){
            return RequestResult.fail("无法获取记录");
        }
        return RequestResult.success(room);
    }

    @ApiOperation(value = "新增|Room")
    @PostMapping("")
    @Transactional
    public RequestResult add(@RequestBody Room room){

        room.getRoomNo().split(",");

        String regEx="，|,|;|；";
        Pattern p = Pattern.compile(regEx);

        List<Room> roomList = new ArrayList<>();
        Room newRoom = null;
        for (String roomno : p.split(room.getRoomNo())){
            newRoom = new Room();
            newRoom.setInnCode(room.getInnCode());
            newRoom.setRoomNo(roomno);
            newRoom.setWifiName(room.getWifiName());
            newRoom.setWifiPassword(room.getWifiPassword());
            newRoom.setCreateTime(new Date());
            newRoom.setLastModifyTime(new Date());
            newRoom.setCreateBy(HeaderUtils.getCurrentUser());
            newRoom.setLastModifyBy(HeaderUtils.getCurrentUser());
            newRoom.setFlag(CommonFlag.VALID.getValue());

            roomList.add(newRoom);
        }

        entityManager.flush();
        for (int i = 0; i < roomList.size(); i++) {
            newRoom = roomList.get(i);
            entityManager.persist(newRoom);
            if (i % 20 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();

        return RequestResult.success("ok");
    }

    @ApiOperation(value = "更新|Room")
    @PutMapping("/{roomId}")
    @Modifying
    @Transactional
    public RequestResult update(@NotNull @PathVariable("roomId") long roomId,
                                @RequestBody Room room){
        Room old_room = iRoomRepository.findOne(roomId);

        if(null == old_room){
            return RequestResult.fail("无法获取记录");
        }

        try {
            PropertyUtils.copyProperties(old_room, room);
            old_room.setLastModifyTime(new Date());
            old_room.setLastModifyBy(HeaderUtils.getCurrentUser());
            iRoomRepository.saveAndFlush(old_room);

            return RequestResult.success(old_room);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return RequestResult.fail("error");
    }

    @ApiOperation(value = "删除|Room")
    @DeleteMapping("")
    public RequestResult delete(@RequestParam(value = "id", required = true)  long roomId) {
        Room room = iRoomRepository.findOne(roomId);

        if(null == room){
            return RequestResult.fail("无法获取记录");
        }

        room.setFlag(CommonFlag.DELETED.getValue());
        room.setLastModifyTime(new Date());
        room.setLastModifyBy(HeaderUtils.getCurrentUser());
        iRoomRepository.saveAndFlush(room);

        return  RequestResult.success("ok");
    }
}
