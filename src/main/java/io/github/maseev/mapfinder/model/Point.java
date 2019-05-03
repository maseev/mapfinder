package io.github.maseev.mapfinder.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point implements Serializable {

  private double latitude;
  private double longitude;
}
