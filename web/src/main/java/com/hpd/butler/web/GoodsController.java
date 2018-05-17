package com.hpd.butler.web;

import com.hpd.butler.common.RequestResult;
import com.hpd.butler.constant.CommonFlag;
import com.hpd.butler.utils.HeaderUtils;
import com.hpd.butler.domain.Goods;
import com.hpd.butler.domain.IGoodsDao;
import com.hpd.butler.domain.IGoodsRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zy on 2018/2/6.
 */
@RestController
@RequestMapping("/sys/goods")
@Api(value = "GoodsController", description = "小商品相关接口")
public class GoodsController {
    @Autowired
    private IGoodsRepository goodsRepository;
    @Autowired
    private IGoodsDao goodsDao;

    @ApiOperation(value = "All Goods|列表")
    @GetMapping("")
    public RequestResult getAll(@RequestParam(value = "category", required = false, defaultValue = "") final String category,
                                @RequestParam(value = "goodsName", required = false, defaultValue = "") final String goodsName,
                                @RequestParam(value = "pageIdx", required = false, defaultValue = "0") Integer pageIdx,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize){
        Pageable pageable = new PageRequest(pageIdx, pageSize, Sort.Direction.DESC, "id");

        Specification<Goods> spec = new Specification<Goods>() {
            public Predicate toPredicate(Root<Goods> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                Predicate p = cb.equal(root.get("flag").as(Integer.class), CommonFlag.VALID.getValue());
                predicates.add(p);

                if(!StringUtils.isEmpty(category)){
                    p = cb.equal(root.get("category").as(String.class), category);
                    predicates.add(p);
                }

                if(!StringUtils.isEmpty(goodsName)){
                    p = cb.like(root.get("goodsName").as(String.class), "%"+goodsName+"%");
                    predicates.add(p);
                }

                Predicate[] props = new Predicate[predicates.size()];
                predicates.toArray(props);
                query.where(props);
                return query.getRestriction();
            }
        };

        Page<Goods> sysDictPage =  goodsDao.findAll(spec,pageable);

        return RequestResult.success(sysDictPage);
    }

    @ApiOperation(value = "Goods|详情")
    @GetMapping("/{goodsid}")
    public RequestResult getGoods(@NotNull @PathVariable("goodsid") long goodsid) {
        Goods goods = goodsRepository.findOne(goodsid);

        if(null == goods){
            return RequestResult.fail("无法获取记录");
        }
        return RequestResult.success(goods);
    }

    @ApiOperation(value = "新增|Goods")
    @PostMapping("")
    @Transactional
    public RequestResult add(@RequestBody Goods goods){
        //Authorization:Bearer + " " + token
        goods.setCreateBy(HeaderUtils.getCurrentUser());
        goods.setLastModifyBy(HeaderUtils.getCurrentUser());
        goods.setCreateTime(new Date());
        goods.setLastModifyTime(new Date());
        goods.setFlag(CommonFlag.VALID.getValue());

        goods = goodsRepository.saveAndFlush(goods);
        return RequestResult.success(goods);
    }

    @ApiOperation(value = "更新|Goods")
    @RequestMapping(value = "/{goodsid}", method = RequestMethod.PUT)
    @Modifying
    @Transactional
    public RequestResult update(@PathVariable("goodsid") long goodsid,
                                @RequestBody Goods goods){
        Goods old_goods = goodsRepository.findOne(goodsid);

        if(null == old_goods){
            return RequestResult.fail("无法获取记录");
        }

        old_goods.setGoodsName(goods.getGoodsName());
        old_goods.setCategory(goods.getCategory());
        old_goods.setInnCode(goods.getInnCode());
        old_goods.setOriginalPrice(goods.getOriginalPrice());
        old_goods.setPic(goods.getPic());
        old_goods.setLastModifyBy(HeaderUtils.getCurrentUser());
        old_goods.setLastModifyTime(new Date());

        old_goods = goodsRepository.saveAndFlush(old_goods);
        return RequestResult.success(old_goods);
    }

    @ApiOperation(value = "删除|Goods")
    @DeleteMapping("")
    public RequestResult delete(@RequestHeader("Authorization") String Authorization,
                                @RequestParam(value = "id", required = true)  long goodsid) {
        Goods goods = goodsRepository.findOne(goodsid);

        if(null == goods){
            return RequestResult.fail("无法获取记录");
        }

        //goodsRepository.delete(goods);
        goods.setFlag(CommonFlag.DELETED.getValue());
        goodsRepository.saveAndFlush(goods);
        return  RequestResult.success("ok");
    }
}
