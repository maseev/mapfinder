package io.github.maseev.mapfinder.service;

import io.github.maseev.mapfinder.model.GeoMap;
import java.io.InputStream;

public interface MapParsingService {

  CloseableIterator<GeoMap> parse(InputStream inputStream);
}
