package io.github.maseev.mapfinder.service;

import io.github.maseev.mapfinder.model.GeoMap;

public interface MapFinderService {

  boolean contains(GeoMap map, double latitude, double longitude);
}
