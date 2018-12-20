package com.labs.reactive.springboot;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Image {

  private String id;
  private String name;

  public Image(String id, String name) {
    this.id = id;
    this.name = name;
  }
}
