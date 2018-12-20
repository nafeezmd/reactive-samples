package com.labs.reactive.springboot;

import com.labs.reactive.springboot.model.Chapter;
import com.labs.reactive.springboot.repository.ChapterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
public class LoadDatabase {

  @Bean
  CommandLineRunner init(ChapterRepository chapterRepository) {
    return args -> {
      Flux.just(new Chapter("Head first Java"),
          new Chapter("Learning Springboot 2.0"),
          new Chapter("Clean code"))
      .flatMap(chapterRepository::save)
      .subscribe(System.out::println);
    };
  }
}
