package io.github.maseev.mapfinder.service.impl;

import io.github.maseev.mapfinder.model.Point;
import java.util.List;

public final class MapFinderUtil {

  private MapFinderUtil() {
  }

  public static boolean contains(List<Point> polygon, double latitude, double longitude) {
    if (polygon.size() < 3) {
      return false;
    }

    boolean found = false;

    for (int i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++) {
      if (polygon.get(i).getLongitude() > longitude != polygon.get(j).getLongitude() > longitude
        && latitude < (polygon.get(j).getLatitude() - polygon.get(i).getLatitude()) *
        (longitude - polygon.get(i).getLongitude()) / (polygon.get(j).getLongitude() - polygon.get(i).getLongitude()) + polygon.get(i).getLatitude()) {
        found = !found;
      }
    }

    return found;
  }
}
