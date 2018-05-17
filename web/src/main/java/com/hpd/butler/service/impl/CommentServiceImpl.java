package com.hpd.butler.service.impl;

import com.hpd.butler.domain.Comment;
import com.hpd.butler.domain.CommentDetail;
import com.hpd.butler.domain.CommentTag;
import com.hpd.butler.domain.ICommentRepository;
import com.hpd.butler.service.CommentDetailService;
import com.hpd.butler.service.CommentService;
import com.hpd.butler.vo.CommentVo;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private ICommentRepository iCommentRepository;
    @Autowired
    private CommentDetailService commentDetailService;

    @Transactional
    public String save(CommentVo commentVo) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Comment comment = new Comment();
        PropertyUtils.copyProperties(comment,commentVo);
        Comment new_comment = new Comment();
        new_comment = iCommentRepository.saveAndFlush(comment);
        List<CommentTag> commentTagList = new ArrayList<CommentTag>();
        commentTagList = commentVo.getCommentTags();
        for(int i = 0; i < commentTagList.size(); i++){
            CommentDetail commentDetail = new CommentDetail();
            commentDetail.setCommentId(new_comment.getId());
            commentDetail.setCommentTagId(commentTagList.get(i).getId());
            commentDetailService.save(commentDetail);
        }
        return "ok";
    }

    @Override
    public BigDecimal count(String innCode, String customerServices) {

        Object[] count = iCommentRepository.countComment(innCode,customerServices);
        Object[] newCount = (Object[]) count[0];
        BigDecimal countNum = new BigDecimal(newCount[0]+"");  //评价总数
        BigDecimal sumNum = new BigDecimal(newCount[1]+"");    //评价总分

        Object[] starCount = iCommentRepository.countStarLavel(innCode,customerServices);
        BigDecimal countBig = new BigDecimal(newCount[0]+"");
        BigDecimal sum = new BigDecimal("0");
        BigDecimal val = new BigDecimal("0");
        BigDecimal star = new BigDecimal("0");
        for(int i = 0; i < starCount.length; i++){
            Object[] obj = (Object[]) starCount[i];
            BigDecimal count1 = new BigDecimal(obj[2].toString());
            sum = sum.add(count1);
            if(val.compareTo(new BigDecimal("0")) == 0){
                val = new BigDecimal(obj[1]+"").divide(countNum,1,BigDecimal.ROUND_HALF_UP);
                star = new BigDecimal(obj[0]+"");
                continue;
            }
            BigDecimal newVal = new BigDecimal("0");
            newVal = new BigDecimal(obj[1]+"").divide(countNum,1,BigDecimal.ROUND_HALF_UP);
            if(val.compareTo(newVal) == 0){
                if(star.compareTo(new BigDecimal(obj[0]+"")) == -1){
                    val = newVal;
                    star = new BigDecimal(obj[0]+"");
                }
            }else if(val.compareTo(newVal) == -1){
                val = newVal;
                star = new BigDecimal(obj[0]+"");
            }
        }
        BigDecimal resultVal = ((sum.divide(countNum)).add(star)).divide(new BigDecimal(2),1,BigDecimal.ROUND_HALF_UP);
        return resultVal;
    }

}
