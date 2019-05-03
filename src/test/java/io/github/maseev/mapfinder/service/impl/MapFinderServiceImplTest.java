package io.github.maseev.mapfinder.service.impl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import io.github.maseev.mapfinder.model.Point;
import io.github.maseev.mapfinder.service.MapFinderService;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class MapFinderServiceImplTest {

  private MapFinderService mapFinderService;

  @Before
  public void setUp() throws Exception {
    mapFinderService = new MapFinderServiceImpl();
  }

  @Test
  public void findingAPointThatLiesInsideThePolygonMustReturnTrue() {
    final List<Point> polygon = asList(
      new Point(0, 0),
      new Point(0, 1),
      new Point(1, 1),
      new Point(1, 0)
    );

    final boolean found = mapFinderService.contains(polygon, 0.5, 0.5);

    assertTrue(found);
  }

  @Test
  public void findingAPointThatLiesOutsideThePolygonMustReturnFalse() {
    final List<Point> polygon = asList(
      new Point(0, 0),
      new Point(0, 1),
      new Point(1, 1),
      new Point(1, 0)
    );

    final boolean found = mapFinderService.contains(polygon, 1.5, 0.5);

    assertFalse(found);
  }

  @Test
  public void findingAPointInsideANonValidPolygonMustReturnFalse() {
    final List<Point> polygon = asList(
      new Point(0, 0),
      new Point(0, 1)
    );

    final boolean found = mapFinderService.contains(polygon, 0, 0);

    assertFalse(found);
  }
}