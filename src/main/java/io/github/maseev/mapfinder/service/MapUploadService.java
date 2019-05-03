package io.github.maseev.mapfinder.service;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.fileupload.FileUploadException;

public interface MapUploadService {

  void upload(HttpServletRequest request) throws IOException, FileUploadException;
}
