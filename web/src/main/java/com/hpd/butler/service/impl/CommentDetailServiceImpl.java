package com.hpd.butler.service.impl;

import com.hpd.butler.domain.CommentDetail;
import com.hpd.butler.domain.ICommentDetailRepository;
import com.hpd.butler.service.CommentDetailService;
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
