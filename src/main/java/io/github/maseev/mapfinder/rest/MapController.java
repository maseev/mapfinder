package io.github.maseev.mapfinder.rest;

import io.github.maseev.mapfinder.service.MapStorageService;
import io.github.maseev.mapfinder.service.MapUploadService;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map")
@Log4j2(topic = "io.github.maseev.map")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MapController {

  private final MapUploadService mapUploadService;
  private final MapStorageService mapStorageService;

  @PostMapping
  public void upload(HttpServletRequest request) throws IOException, FileUploadException {
    mapUploadService.upload(request);
  }

  @GetMapping
  public List<Integer> find(@RequestParam double latitude, @RequestParam double longitude)
    throws InterruptedException, ExecutionException, TimeoutException {
    return mapStorageService.contains(latitude, longitude);
  }
}



