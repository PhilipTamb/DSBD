package com.dsbd.dsbd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class DsbdApplication {

    public static void main(String[] args) {
        SpringApplication.run(DsbdApplication.class, args);
    }

}
