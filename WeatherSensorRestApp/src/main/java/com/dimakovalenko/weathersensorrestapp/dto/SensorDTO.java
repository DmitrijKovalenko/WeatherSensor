package com.dimakovalenko.weathersensorrestapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class SensorDTO {
    @NotEmpty(message = "name should not be empty")
    @Size(min = 2,max = 30,message = "name should be between 2 and 30 characters")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
