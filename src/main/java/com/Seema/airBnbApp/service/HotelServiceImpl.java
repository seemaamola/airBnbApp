package com.Seema.airBnbApp.service;

import com.Seema.airBnbApp.config.MapperConfig;
import com.Seema.airBnbApp.dto.HotelDto;
import com.Seema.airBnbApp.entity.Hotel;
import com.Seema.airBnbApp.exception.ResourceNotFoundException;
import com.Seema.airBnbApp.repository.HotelReposotory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelReposotory hotelReposotory;
    private final ModelMapper modelMapper;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name: {}",hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto,Hotel.class);
        hotel.setActive(false);
        hotel = hotelReposotory.save(hotel);
        log.info("Created a new hotel with Id:{}",hotel.getId());
        return modelMapper.map(hotel,HotelDto.class);

    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting the hotel with id: {}",id);
        Hotel hotel = hotelReposotory
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with ID: "+id));

        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto)
    {
        log.info("Getting the hotel with id: {}",id);
        Hotel hotel = hotelReposotory
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with ID: "+id));
        modelMapper.map(hotelDto,hotel);
        hotel.setId(id);
        hotel = hotelReposotory.save(hotel);
        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public Boolean deleteHotelById(Long id) {
        boolean exists = hotelReposotory.existsById(id);
        if(!exists) throw new ResourceNotFoundException("Hotel not found with ID: "+id);

        hotelReposotory.deleteById(id);
        //TODO: delete the future inventories for this hotel
        return true;
    }

    @Override
    public List<HotelDto> getAllHotel() {
        List<Hotel> hotels =hotelReposotory.findAll();
        List<HotelDto> hotelDtos = hotels.stream()
                .map(hotel -> modelMapper.map(hotel,HotelDto.class))
                .toList();
        return hotelDtos;
    }

}
