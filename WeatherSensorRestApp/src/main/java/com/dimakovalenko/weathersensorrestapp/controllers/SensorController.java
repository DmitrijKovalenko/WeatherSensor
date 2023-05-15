package com.dimakovalenko.weathersensorrestapp.controllers;

import com.dimakovalenko.weathersensorrestapp.dto.SensorDTO;
import com.dimakovalenko.weathersensorrestapp.models.Sensor;
import com.dimakovalenko.weathersensorrestapp.service.SensorService;
import com.dimakovalenko.weathersensorrestapp.util.SensorErrorResponse;
import com.dimakovalenko.weathersensorrestapp.util.SensorException;
import com.dimakovalenko.weathersensorrestapp.validation.SensorValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;


import java.util.List;


@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorValidator sensorValidator;
    private final SensorService sensorService;
    private final ModelMapper modelMapper;

    @Autowired
    public SensorController(SensorValidator sensorValidator, SensorService sensorService, ModelMapper modelMapper) {
        this.sensorValidator = sensorValidator;
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
    }


    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid SensorDTO sensorDTO,BindingResult bindingResult) {
        Sensor sensorToAdd = convertToSensor(sensorDTO);
        sensorValidator.validate(sensorToAdd, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            System.out.println(bindingResult.toString());
            throw new SensorException(errorMsg.toString());
        }
        sensorService.save(sensorToAdd);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorException e) {
    SensorErrorResponse response = new SensorErrorResponse(e.getMessage(),System.currentTimeMillis());
    return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO ){
        Sensor sensor = modelMapper.map(sensorDTO,Sensor.class);
    return sensor;
    }
    private SensorDTO convertToSensorDTO(Sensor sensor){
        SensorDTO sensorDTO = modelMapper.map(sensor,SensorDTO.class);
        return sensorDTO;
    }

}
