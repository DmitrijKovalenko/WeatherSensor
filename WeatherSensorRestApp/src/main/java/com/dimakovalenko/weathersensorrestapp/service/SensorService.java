package com.dimakovalenko.weathersensorrestapp.service;

import com.dimakovalenko.weathersensorrestapp.dto.MeasurementDTO;
import com.dimakovalenko.weathersensorrestapp.models.Sensor;
import com.dimakovalenko.weathersensorrestapp.repositories.SensorRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorService {

    private final SensorRepositories sensorRepositories;

    public SensorService(SensorRepositories sensorRepositories) {
        this.sensorRepositories = sensorRepositories;
    }
    @Transactional
    public void save(Sensor sensor){
        enrichSensor(sensor);
        sensorRepositories.save(sensor);
    }

    public void enrichSensor(Sensor sensor){
        sensor.setCreatedAt(LocalDateTime.now());
        sensor.setCreatedWho("Admin");
    }

    public Optional<Sensor> findByName(String name) {
       return sensorRepositories.findByName(name);
    }

}
