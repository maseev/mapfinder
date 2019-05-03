package io.github.maseev.mapfinder.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoMap {

  private int id;
  private List<Point> polygon;
}
