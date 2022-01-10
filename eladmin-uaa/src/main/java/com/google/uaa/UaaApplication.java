package com.google.uaa;

import com.google.core.feign.annotation.EnableElAdminFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author iris
 */
@SpringBootApplication
@EnableElAdminFeign
public class UaaApplication {
    public static void main(String[] args) {
        SpringApplication.run(UaaApplication.class, args);
    }
}
