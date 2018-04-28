package com.yumi.butler.service.impl;

import com.yumi.butler.domain.CommentDetail;
import com.yumi.butler.domain.ICommentDetailRepository;
import com.yumi.butler.service.CommentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentDetailServiceImpl implements CommentDetailService {

    @Autowired
    private ICommentDetailRepository iCommentDetailRepository;

    public void save(CommentDetail commentDetail){
        iCommentDetailRepository.saveAndFlush(commentDetail);
    }

}
