package io.github.maseev.mapfinder.service;

import io.github.maseev.mapfinder.model.GeoMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface MapStorageService {

  void add(GeoMap map);

  List<Integer> contains(double latitude, double longitude) throws InterruptedException,
    ExecutionException, TimeoutException;
}
