package com.labs.reactive.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/home")
public class HomeController {

  @GetMapping("/images")
  public Flux<Image> images() {
    return Flux.just(
        new Image("1", "learning-spring-boot-2-cover.jpg"),
        new Image("2", "learning-spring-data.jpg"),
        new Image("3", "learning-spring-cloud.jpg")
    );
  }

  @PostMapping("/images")
  public Mono<Void> create(@RequestBody Flux<Image> images) {
    return images.map(image -> {
      System.out.println("We will save " + image + " to a Reactive database soon!");
      return image;
    }).then();
  }

}
