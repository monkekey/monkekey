package com.hpd.butler.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zy on 2017/10/9.
 */
public interface ISysuserdetailRepository  extends JpaRepository<Sysuserdetail, Long> {

    public Sysuserdetail findByUid(long uid);

    public List<Sysuserdetail> findByInnCode(String inncode);

}
