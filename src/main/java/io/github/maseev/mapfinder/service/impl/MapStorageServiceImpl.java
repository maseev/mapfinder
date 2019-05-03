package io.github.maseev.mapfinder.service.impl;

import io.github.maseev.mapfinder.model.GeoMap;
import io.github.maseev.mapfinder.model.Point;
import io.github.maseev.mapfinder.service.MapStorageService;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class MapStorageServiceImpl implements MapStorageService {

  private final Map<Integer, List<Point>> maps = new ConcurrentHashMap<>();

  @Override
  public void add(GeoMap map) {
    maps.put(map.getId(), map.getPolygon());
  }
}
