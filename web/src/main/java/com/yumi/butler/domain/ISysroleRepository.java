package com.yumi.butler.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zy on 2017/10/9.
 */
public interface ISysroleRepository extends JpaRepository<Sysrole, Long> {
    public List<Sysrole> findByIdIn(List<Long> roleidList);
}
