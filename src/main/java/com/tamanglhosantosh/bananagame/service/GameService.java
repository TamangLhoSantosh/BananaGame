package com.tamanglhosantosh.bananagame.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class GameService {

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<?> getGame(){
        try{
            String gameURL = "https://marcconrad.com/uob/banana/api.php?out=json";
            ResponseEntity<?> response = restTemplate.exchange(gameURL, HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});
            log.info("Got response: {}", response.getBody());
            return response;

        }catch(Exception e){
            log.error(String.valueOf(e));
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Excpetion occurred while fetching game data"
            );
        }
    }
}
