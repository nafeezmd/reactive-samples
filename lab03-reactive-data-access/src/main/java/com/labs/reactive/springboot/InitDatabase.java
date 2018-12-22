package com.labs.reactive.springboot;

import com.labs.reactive.springboot.model.Image;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class InitDatabase {

  @Bean
  CommandLineRunner init(MongoOperations mongoOperations) {
    return args -> {
      mongoOperations.dropCollection(Image.class);

      mongoOperations.insert(new Image("1", "learning-spring-boot-1-cover.jpg"));
      mongoOperations.insert(new Image("2", "learning-spring-boot-2-cover.jpg"));
      mongoOperations.insert(new Image("3", "learning-spring-boot-3-cover.jpg"));

      mongoOperations.findAll(Image.class).forEach(image -> System.out.println(image.toString()));
    };
  }
}
