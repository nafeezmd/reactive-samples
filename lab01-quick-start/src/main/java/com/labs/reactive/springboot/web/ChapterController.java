package com.labs.reactive.springboot.web;

import com.labs.reactive.springboot.model.Chapter;
import com.labs.reactive.springboot.repository.ChapterRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChapterController {

  private ChapterRepository chapterRepository;

  public ChapterController(ChapterRepository chapterRepository) {
    this.chapterRepository = chapterRepository;
  }

  @GetMapping("/chapters")
  public Flux<Chapter> getAllChapters() {
    return chapterRepository.findAll();
  }

}
