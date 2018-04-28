package com.yumi.butler.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISHotelCategoryRepository extends JpaRepository<SHotelCategory,Long> {

    public SHotelCategory findByHotelIdAndCategoryCode(String hotelId,String code);

    public List<SHotelCategory> findByHotelIdAndFlag(String hotelId,int flag);

}
