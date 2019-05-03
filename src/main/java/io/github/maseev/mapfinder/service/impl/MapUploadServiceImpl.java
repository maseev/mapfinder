package io.github.maseev.mapfinder.service.impl;

import io.github.maseev.mapfinder.model.GeoMap;
import io.github.maseev.mapfinder.service.CloseableIterator;
import io.github.maseev.mapfinder.service.MapParsingService;
import io.github.maseev.mapfinder.service.MapStorageService;
import io.github.maseev.mapfinder.service.MapUploadService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MapUploadServiceImpl implements MapUploadService {

  private final MapParsingService mapParsingService;
  private final MapStorageService mapStorageService;

  @Override
  public void upload(HttpServletRequest request) throws IOException, FileUploadException {
    FileItemIterator iter = new ServletFileUpload().getItemIterator(request);

    while (iter.hasNext()) {
      FileItemStream item = iter.next();

      if (isFile(item)) {
        parse(item);
      }
    }
  }

  private void parse(FileItemStream item) throws IOException {
    try (CloseableIterator<GeoMap> iterator = mapParsingService.parse(item.openStream())) {
      while (iterator.hasNext()) {
        GeoMap map = iterator.next();

        mapStorageService.add(map);
      }
    }
  }

  private static boolean isFile(FileItemStream item) {
    return !item.isFormField();
  }
}
