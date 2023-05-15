package com.dimakovalenko.weathersensorrestapp.service;

import com.dimakovalenko.weathersensorrestapp.models.Measurement;
import com.dimakovalenko.weathersensorrestapp.repositories.MeasurementRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepositories measurementRepositories;
    private final SensorService sensorService;
    @Autowired
    public MeasurementService(MeasurementRepositories measurementRepositories, SensorService sensorService) {
        this.measurementRepositories = measurementRepositories;
        this.sensorService = sensorService;
    }

    public List<Measurement> findAll() {
        return measurementRepositories.findAll();
    }

    @Transactional
    public void save(Measurement measurement) {
        enrichMeasurement(measurement);
        measurementRepositories.save(measurement);
    }
    private void enrichMeasurement(Measurement measurement) {
        measurement.setSensor(sensorService.findByName(measurement.getSensor().getName()).get());
        measurement.setMeasurementDateTime(LocalDateTime.now());
    }

}
