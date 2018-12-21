package com.labs.reactive.springboot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ImageService {

  private static String UPLOAD_ROOT = "upload-dir";

  private final ResourceLoader resourceLoader;

  public ImageService(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @Bean
  CommandLineRunner setUp() {
    return (args) -> {
      FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));

      Files.createDirectory(Paths.get(UPLOAD_ROOT));

      FileCopyUtils.copy("Test file 1 content", new FileWriter(UPLOAD_ROOT + "learning-spring-boot-1-cover.jpg"));
      FileCopyUtils.copy("Test file 2 content", new FileWriter(UPLOAD_ROOT + "learning-spring-boot-2-cover.jpg"));
      FileCopyUtils.copy("Test file 3 content", new FileWriter(UPLOAD_ROOT + "learning-spring-boot-3-cover.jpg"));
    };
  }

  public Mono<Resource> findOneImage(String fileName) {
    return Mono.fromSupplier(() -> resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + fileName));
  }

  public Flux<Image> findAllImages() {
    try {
      return Flux.fromIterable(
          Files.newDirectoryStream(Paths.get(UPLOAD_ROOT))
      ).map(path -> new Image(UUID.randomUUID().toString(), path.getFileName().toString()));
    } catch (IOException e) {
      return Flux.empty();
    }
  }

  public Mono<Void> createImage(Flux<FilePart> files) {
    return files.flatMap(file ->
        file.transferTo(Paths.get(UPLOAD_ROOT, file.filename()).toFile())
    ).then();
  }

  public Mono<Void> deleteImage(String fileName) {
    return Mono.fromRunnable(() -> {
      try {
        Files.deleteIfExists(Paths.get(UPLOAD_ROOT, fileName));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
