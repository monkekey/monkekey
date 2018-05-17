package com.hpd.butler.web;

import com.hpd.butler.common.RequestResult;
import com.hpd.butler.domain.IInnDao;
import com.hpd.butler.domain.Inn;
import com.hpd.butler.utils.HeaderUtils;
import com.hpd.butler.domain.IInnRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.hpd.butler.constant.CommonFlag;
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

/**
 * Created by zy on 2017/10/9.
 */
@RestController
@RequestMapping("/sys/inn")
@Api(value = "InnController", description = "分店相关接口")
public class InnController {
    @Autowired
    private IInnRepository iInnRepository;
    @Autowired
    private IInnDao iInnDao;
    @Autowired
    @Qualifier("entityManagerFactoryButler")
    private EntityManager entityManager;


    @ApiOperation(value = "Inn|列表")
    @GetMapping("")
    public RequestResult list(@RequestParam(value = "innName", required = false, defaultValue = "") final String innName,
                              @RequestParam(value = "pageIdx", required = false, defaultValue = "0") Integer pageIdx,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Pageable pageable = new PageRequest(pageIdx, pageSize, Sort.Direction.DESC, "id");

        Specification<Inn> spec = new Specification<Inn>() {
            public Predicate toPredicate(Root<Inn> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                Predicate p = cb.equal(root.get("flag").as(Integer.class), CommonFlag.VALID.getValue());
                predicates.add(p);

                if(!StringUtils.isEmpty(innName)){
                    p = cb.like(root.get("innName").as(String.class), "%"+innName+"%");
                    predicates.add(p);
                }

                Predicate[] props = new Predicate[predicates.size()];
                predicates.toArray(props);
                query.where(props);
                return query.getRestriction();
            }
        };

        return RequestResult.success(iInnDao.findAll(spec,pageable));
    }

    @ApiOperation(value = "All Inn|列表")
    @GetMapping("/all")
    public RequestResult getAllInn(){
        return RequestResult.success(iInnRepository.findAllInn());
    }

    @ApiOperation(value = "Inn|详情")
    @GetMapping("/{innId}")
    public RequestResult getByRoomCode(@NotNull @PathVariable("innId") long innId) {
        Inn inn = iInnRepository.findOne(innId);

        if(null == inn){
            return RequestResult.fail("无法获取记录");
        }
        return RequestResult.success(inn);
    }

    @ApiOperation(value = "新增|Inn")
    @PostMapping("")
    @Transactional
    public RequestResult add(@RequestBody Inn inn){
        inn.setFlag(CommonFlag.VALID.getValue());
        inn.setCreateTime(new Date());
        inn.setLastModifyTime(new Date());
        inn.setCreateBy(HeaderUtils.getCurrentUser());
        inn.setLastModifyBy(HeaderUtils.getCurrentUser());
        inn = iInnRepository.saveAndFlush(inn);

        return RequestResult.success(inn);
    }

    @ApiOperation(value = "更新|Inn")
    @RequestMapping(value = "/{innId}", method = RequestMethod.PUT)
    @Modifying
    @Transactional
    public RequestResult update(@PathVariable("innId") long innId,
                                @RequestBody Inn inn){
        Inn old_inn = iInnRepository.findOne(innId);

        if(null == inn){
            return RequestResult.fail("无法获取记录");
        }

        try {
            PropertyUtils.copyProperties(old_inn, inn);

            old_inn.setLastModifyTime(new Date());
            old_inn.setLastModifyBy(HeaderUtils.getCurrentUser());
            old_inn = iInnRepository.saveAndFlush(old_inn);
            return RequestResult.success(old_inn);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        return RequestResult.fail("error");

    }

    @ApiOperation(value = "删除|Inn")
    @DeleteMapping("")
    public RequestResult delete(@RequestParam(value = "id", required = true)  long innId) {
        //String uid = "1bdc26cc-6c33-11e7-961f-00163e00091c";
//        String uid = HeaderUtil.getHeaderUID();
//        if(StringUtils.isEmpty(uid)){
//            return  RequestResult.fail("无法获取当前用户");
//        }
        Inn inn = iInnRepository.findOne(innId);

        if(null == inn){
            return RequestResult.fail("无法获取记录");
        }

        inn.setFlag(CommonFlag.DELETED.getValue());
        inn.setLastModifyTime(new Date());
        inn.setLastModifyBy(HeaderUtils.getCurrentUser());
        iInnRepository.saveAndFlush(inn);

        return  RequestResult.success("ok");
    }


}
