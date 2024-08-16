package com.sjprogramming.restapi.controller;

import com.sjprogramming.restapi.Service.ApiService;
import com.sjprogramming.restapi.entity.ApiResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crypto")
public class ApiController {
    @Autowired
    private ApiService cryptoService;

    @GetMapping("/quotes")
    public ResponseEntity<ApiResponseEntity> getCryptoQuotes(@RequestParam Long userId) {
        ApiResponseEntity response = cryptoService.fetchAndSaveCryptoData(userId);
        return ResponseEntity.ok(response);
    }
}
