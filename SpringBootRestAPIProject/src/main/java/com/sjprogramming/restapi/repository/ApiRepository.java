package com.sjprogramming.restapi.repository;

import com.sjprogramming.restapi.entity.ApiResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiRepository extends JpaRepository<ApiResponseEntity,Long> {
}
