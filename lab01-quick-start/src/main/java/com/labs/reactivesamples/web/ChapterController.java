package com.labs.reactivesamples.web;

import com.labs.reactivesamples.model.Chapter;
import com.labs.reactivesamples.repository.ChapterRepository;
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
