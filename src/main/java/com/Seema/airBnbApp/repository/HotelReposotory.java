package com.Seema.airBnbApp.repository;

import com.Seema.airBnbApp.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelReposotory extends JpaRepository<Hotel,Long> {
}
