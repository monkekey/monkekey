package com.hpd.butler.service.impl;

import com.hpd.butler.domain.CommentTag;
import com.hpd.butler.domain.ICommentTagRepository;
import com.hpd.butler.service.CommentTagService;
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
