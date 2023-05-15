package org.example;

import com.sun.net.httpserver.Headers;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class App {
    public static void main( String[] args ) {

        final String sensorName = "SensorName";

        registerSensor(sensorName);
        Random random = new Random();
        double minTemperature = -50.0;
        double maxTemperature = 45.0;
        for (int i = 0; i < 500; i++) {
            System.out.println(i);
            sendMeasurement(random.nextDouble()*maxTemperature,random.nextBoolean(),sensorName);
            
        }
    }

    private static void sendMeasurement(double value, boolean raining, String sensorName) {
        final String url = "http://localhost:8082/measurements/add";
        Map<String,Object> jsonData = new HashMap<>();
        jsonData.put("value",value);
        jsonData.put("raining",raining);
        jsonData.put("sensor",Map.of("name",sensorName));

        makePostRequestWithJSONData(url,jsonData);
    }

    private static void registerSensor(String sensorName) {
        String url = "http://localhost:8082/sensors/registration";
        Map<String,Object> jsonToSend = new HashMap<>();
        jsonToSend.put("name",sensorName);
        makePostRequestWithJSONData(url,jsonToSend);
    }

    private static void makePostRequestWithJSONData(String url, Map<String, Object> jsonToSend) {
       final RestTemplate restTemplate = new RestTemplate();

       final HttpHeaders headers= new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<Object>(jsonToSend,headers);

        try{
            restTemplate.postForObject(url,request,String.class);
            System.out.println("Measurements are added successfully");
        }catch (HttpClientErrorException e){
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
    }
}
