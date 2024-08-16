package com.sjprogramming.restapi.Service;

import com.sjprogramming.restapi.entity.ApiResponseEntity;
import com.sjprogramming.restapi.repository.ApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
@Service
public class ApiService {
    @Autowired
    private ApiRepository cryptoResponseRepository;

    private static final String COINMARKETCAP_URL = "https://pro-api.coinmarketcap.com/v2/cryptocurrency/quotes/latest?symbol=BTC";
    private static final String API_KEY = "27ab17d1-215f-49e5-9ca4-afd48810c149";

    public ApiResponseEntity fetchAndSaveCryptoData(Long userId) {

         RestTemplate restTemplate = new RestTemplate();

            // Set up the headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-CMC_PRO_API_KEY", API_KEY);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Make the API call
            ResponseEntity<String> response = restTemplate.exchange(COINMARKETCAP_URL, HttpMethod.GET, entity, String.class);

            // Save response to the database
            ApiResponseEntity cryptoResponse = new ApiResponseEntity();
            cryptoResponse.setUserId(userId);
            cryptoResponse.setResponseData(response.getBody());
            cryptoResponse.setTimestamp(LocalDateTime.now());

        return cryptoResponseRepository.save(cryptoResponse);
        }
    }
