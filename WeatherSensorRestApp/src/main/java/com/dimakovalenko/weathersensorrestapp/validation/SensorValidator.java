package com.dimakovalenko.weathersensorrestapp.validation;

import com.dimakovalenko.weathersensorrestapp.models.Sensor;
import com.dimakovalenko.weathersensorrestapp.repositories.SensorRepositories;
import com.dimakovalenko.weathersensorrestapp.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SensorValidator implements Validator {

    private final SensorService sensorService;

    @Autowired
    public SensorValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Sensor sensor = (Sensor) target;
        if (sensorService.findByName(sensor.getName()).isPresent()) {
            errors.rejectValue("name", "", "Sensor with this name is already been registered");
        }
    }
}
