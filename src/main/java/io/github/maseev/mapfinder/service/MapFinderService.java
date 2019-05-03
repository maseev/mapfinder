package io.github.maseev.mapfinder.service;

import io.github.maseev.mapfinder.model.Point;
import java.util.List;

public interface MapFinderService {

  boolean contains(List<Point> polygon, double latitude, double longitude);
}
