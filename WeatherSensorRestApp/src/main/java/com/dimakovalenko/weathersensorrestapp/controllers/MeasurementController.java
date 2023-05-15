package com.dimakovalenko.weathersensorrestapp.controllers;

import com.dimakovalenko.weathersensorrestapp.dto.MeasurementDTO;
import com.dimakovalenko.weathersensorrestapp.models.Measurement;
import com.dimakovalenko.weathersensorrestapp.repositories.MeasurementRepositories;
import com.dimakovalenko.weathersensorrestapp.service.MeasurementService;
import com.dimakovalenko.weathersensorrestapp.util.MeasurementErrorResponse;
import com.dimakovalenko.weathersensorrestapp.util.MeasurementException;
import com.dimakovalenko.weathersensorrestapp.validation.MeasurementValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {
    private final MeasurementService measurementService;
    private final ModelMapper modelMapper;
    private final MeasurementValidator measurementValidator;
    private final MeasurementRepositories measurementRepositories;

    @Autowired
    public MeasurementController(MeasurementService measurementService, ModelMapper modelMapper, MeasurementValidator measurementValidator,
                                 MeasurementRepositories measurementRepositories) {
        this.measurementService = measurementService;
        this.modelMapper = modelMapper;
        this.measurementValidator = measurementValidator;
        this.measurementRepositories = measurementRepositories;
    }


    @GetMapping
    public List<MeasurementDTO> getAllMeasurements(){
    return  measurementRepositories.findAll().stream().map(this::convertMeasurementToDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/rainyDaysCount")
    public long getRainyDaysCount(){
      return   measurementService.findAll().stream().filter(Measurement::getRaining).count();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult){
        Measurement measurementToAdd= convertToMeasurement(measurementDTO);
        measurementValidator.validate(measurementToAdd,bindingResult);
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            System.out.println(bindingResult.toString());
            throw new MeasurementException(errorMsg.toString());
    }
    measurementService.save(measurementToAdd);
    return ResponseEntity.ok(HttpStatus.OK);
    }
    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e){
        MeasurementErrorResponse response = new MeasurementErrorResponse(e.getMessage(),System.currentTimeMillis());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    private MeasurementDTO convertMeasurementToDTO(Measurement measurement){
        MeasurementDTO measurementDTOToAdd = modelMapper.map(measurement,MeasurementDTO.class);
        return measurementDTOToAdd;
    }
    private Measurement convertToMeasurement(MeasurementDTO measurementDTO){
        Measurement measurementToAdd = modelMapper.map(measurementDTO,Measurement.class);
        return measurementToAdd;
    }

}
