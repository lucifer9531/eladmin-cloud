package com.google.system;

import com.google.core.feign.annotation.EnableElAdminFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author iris
 */
@SpringBootApplication
@EnableElAdminFeign
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
