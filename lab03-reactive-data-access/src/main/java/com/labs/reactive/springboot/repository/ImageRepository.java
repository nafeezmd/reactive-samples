package com.labs.reactive.springboot.repository;

import com.labs.reactive.springboot.model.Image;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ImageRepository extends ReactiveCrudRepository<Image, String> {

  Mono<Image> findByName(String name);

}
