package io.github.maseev.mapfinder.model;

import java.util.List;
import lombok.Data;

@Data
public class Map {

  private int id;
  private List<Point> polygon;
}
