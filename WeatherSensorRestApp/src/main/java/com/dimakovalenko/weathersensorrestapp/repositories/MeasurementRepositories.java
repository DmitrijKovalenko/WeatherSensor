package com.dimakovalenko.weathersensorrestapp.repositories;

import com.dimakovalenko.weathersensorrestapp.models.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepositories extends JpaRepository<Measurement,Integer> {

}
