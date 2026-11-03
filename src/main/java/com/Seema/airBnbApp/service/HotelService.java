package com.Seema.airBnbApp.service;

import com.Seema.airBnbApp.dto.HotelDto;
import com.Seema.airBnbApp.entity.Hotel;

import java.util.List;

public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id, HotelDto hotelDto);

    void deleteHotelById(Long id);

    void activateHotel(Long hotelId);

    List<HotelDto> getAllHotel();


}
