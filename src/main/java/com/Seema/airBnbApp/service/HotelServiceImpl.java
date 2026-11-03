package com.Seema.airBnbApp.service;

import com.Seema.airBnbApp.config.MapperConfig;
import com.Seema.airBnbApp.dto.HotelDto;
import com.Seema.airBnbApp.entity.Hotel;
import com.Seema.airBnbApp.entity.Room;
import com.Seema.airBnbApp.exception.ResourceNotFoundException;
import com.Seema.airBnbApp.repository.HotelReposotory;
import jakarta.transaction.Transactional;
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
    private final InventoryService inventoryService;
    private final RoomService roomService;

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
    @Transactional
    public void deleteHotelById(Long id) {
        Hotel hotel = hotelReposotory
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with ID: "+id));
        hotel.setActive(true);

        hotelReposotory.deleteById(id);
        for(Room room: hotel.getRooms())
        {
            inventoryService.deleteFutureInventories(room);
        }

    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("Getting the hotel with id: {}",hotelId);
        Hotel hotel = hotelReposotory
                .findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with ID: "+hotelId));
        hotel.setActive(true);

        //assuming only do it once
        for (Room room : hotel.getRooms())
        {
            inventoryService.initializeRoomForAYear(room);
        }


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
