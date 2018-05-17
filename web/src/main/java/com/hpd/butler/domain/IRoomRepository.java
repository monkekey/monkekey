package com.hpd.butler.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zy on 2017/10/9.
 */
public interface IRoomRepository extends JpaRepository<Room, Long> {

    public Room findByInnCodeAndRoomNo(String innCode,String roomCode);

    public List<Room> findByInnCodeOrderByRoomNo(String innCode);

    public Page<Room> findByFlag(Pageable pageable, Integer flag);

    public Page<Room> findByInnCodeLikeAndFlag(Pageable pageable, String innCode, Integer flag);
}
