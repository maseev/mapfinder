package io.github.maseev.mapfinder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point implements Serializable {

  @JsonProperty("lat")
  private double latitude;

  @JsonProperty("lng")
  private double longitude;
}
