package com.labs.reactive.springboot.repository;

import com.labs.reactive.springboot.model.Image;
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
  private final ImageRepository imageRepository;

  public ImageService(ResourceLoader resourceLoader, ImageRepository imageRepository) {
    this.resourceLoader = resourceLoader;
    this.imageRepository = imageRepository;
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
    return imageRepository.findAll();
  }

  public Mono<Void> createImage(Flux<FilePart> files) {
    return files.flatMap(file -> {
      Mono<Image> saveDatabaseImage = imageRepository.save(new Image(UUID.randomUUID().toString(), file.filename()));
      Mono<Void> copyFile = Mono.just(Paths.get(UPLOAD_ROOT, file.filename()).toFile()).log("createImage-pickTarget")
          .map(destFile -> {
            try {
              destFile.createNewFile();
              return destFile;
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          })
          .log("createImage-newFile")
          .flatMap(file::transferTo)
          .log("createImage-copy");

      // combining all operations that need to happen
      return Mono.when(saveDatabaseImage, copyFile);
    }).then();
  }

  public Mono<Void> deleteImage(String fileName) {
    Mono<Void> deleteDatabaseImage = imageRepository
        .findByName(fileName)
        .log("deleteImage-find")
        .flatMap(imageRepository::delete)
        .log("deleteImage-record");
    Mono<Void> deleteFile = Mono.fromRunnable(() -> {
      try {
        Files.deleteIfExists(Paths.get(UPLOAD_ROOT, fileName));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
    return Mono.when(deleteDatabaseImage, deleteFile)
        .log("deleteImage-when")
        .then()
        .log("deleteImage-done");
  }
}
