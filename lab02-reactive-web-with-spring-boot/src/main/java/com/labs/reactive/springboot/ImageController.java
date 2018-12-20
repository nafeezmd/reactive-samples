package com.labs.reactive.springboot;

import static com.labs.reactive.springboot.ImageController.BASE_PATH;

import java.io.IOException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(BASE_PATH)
public class ImageController {
  protected static final String BASE_PATH = "/images";
  private static final String FILENAME = "{fileName:.+}";

  private final ImageService imageService;

  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @GetMapping(value = "/" + FILENAME + "/raw", produces = MediaType.IMAGE_JPEG_VALUE)
  @ResponseBody
  public Mono<ResponseEntity<?>> oneRawImage(@PathVariable String fileName) {
    return imageService.findOneImage(fileName)
        .map(resource -> {
          try {
            return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .body(new InputStreamResource(resource.getInputStream()));
          } catch (IOException e) {
            return ResponseEntity.badRequest()
                .body("Couldn't find " + fileName + " => " + e.getMessage());
          }
        });
  }

  @PostMapping
  public Mono<String> createFile(@RequestPart(name = "file") Flux<FilePart> files) {
    return imageService.createImage(files)
        .then(Mono.just("redirect:/"));
  }

  @DeleteMapping
  public Mono<String> deleteFile(@PathVariable String fileName) {
    return imageService.deleteImage(fileName)
        .then(Mono.just("redirect:/"));
  }

  @GetMapping
  public Mono<String> index(Model model) {
    model.addAttribute("images", imageService.findAllImages());
    return Mono.just("index");
  }

}
