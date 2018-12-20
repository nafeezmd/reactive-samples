package com.labs.reactive.springboot.repository;

import com.labs.reactive.springboot.model.Chapter;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChapterRepository extends ReactiveCrudRepository<Chapter, String> {
}
