package com.labs.reactive.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
public class Lab02Application {

  public static void main(String[] args) {
    SpringApplication.run(Lab02Application.class, args);
  }

  @Bean
  public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
    return new HiddenHttpMethodFilter(); // need this bean to make the Http DELETE methods work properly
  }
}
