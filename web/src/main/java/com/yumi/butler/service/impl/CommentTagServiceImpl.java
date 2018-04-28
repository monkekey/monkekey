package com.yumi.butler.service.impl;

import com.yumi.butler.domain.CommentTag;
import com.yumi.butler.domain.ICommentTagRepository;
import com.yumi.butler.service.CommentTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentTagServiceImpl implements CommentTagService {

    @Autowired
    private ICommentTagRepository iCommentTagRepository;

    public List<CommentTag> tagList(){
        return iCommentTagRepository.findAll();
    }

}
