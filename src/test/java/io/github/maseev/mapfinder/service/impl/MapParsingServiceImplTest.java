package io.github.maseev.mapfinder.service.impl;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.maseev.mapfinder.model.GeoMap;
import io.github.maseev.mapfinder.model.Point;
import io.github.maseev.mapfinder.service.CloseableIterator;
import io.github.maseev.mapfinder.service.MapParsingService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class MapParsingServiceImplTest {

  private MapParsingService mapParsingService;

  @Before
  public void setUp() throws Exception {
    mapParsingService = new MapParsingServiceImpl();
  }

  @Test
  public void parsingValidJSONMustReturnTheSameMaps() throws IOException {
    List<Point> polygon = asList(
      new Point(0.0, 0.0),
      new Point(0.0, 1.0),
      new Point(1.0, 0.0));
    GeoMap firstMap = new GeoMap(1, polygon);
    GeoMap secondMap = new GeoMap(2, polygon);
    List<GeoMap> maps = asList(firstMap, secondMap);
    InputStream inputStream = serialize(maps);

    CloseableIterator<GeoMap> iterator = mapParsingService.parse(inputStream);
    final List<GeoMap> parsedMaps = toList(iterator);

    assertThat(parsedMaps, is(equalTo(maps)));
  }

  @Test
  public void parsingAnEmptyListOfMapsMustReturnTheSameEmptyList() throws IOException {
    InputStream inputStream = serialize(emptyList());
    CloseableIterator<GeoMap> iterator = mapParsingService.parse(inputStream);
    final List<GeoMap> parsedMaps = toList(iterator);

    assertThat(parsedMaps, is(equalTo(emptyList())));
  }

  @Test(expected = ParsingException.class)
  public void parsingInvalidJSONMustThrowAnException() {
    String text = "test";

    mapParsingService.parse(new ByteArrayInputStream(text.getBytes()));
  }

  @Test(expected = ParsingException.class)
  public void parsingNotAnArrayOfMapsMustThrowAnException() throws IOException {
    List<Point> polygon = asList(
      new Point(0.0, 0.0),
      new Point(0.0, 1.0),
      new Point(1.0, 0.0));
    GeoMap map = new GeoMap(1, polygon);

    InputStream inputStream = serializeObject(map);

    final CloseableIterator<GeoMap> iterator = mapParsingService.parse(inputStream);
    final List<GeoMap> maps = toList(iterator);

    System.out.println(maps);
  }

  private static List<GeoMap> toList(CloseableIterator<GeoMap> iterator) {
    List<GeoMap> maps = new ArrayList<>();

    iterator.forEachRemaining(maps::add);

    return maps;
  }

  private static InputStream serialize(List<GeoMap> maps) throws IOException {
    return serializeObject(maps);
  }

  private static InputStream serializeObject(Object object) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(outputStream);
    jsonGenerator.setCodec(new ObjectMapper());
    jsonGenerator.writeObject(object);

    return new ByteArrayInputStream(outputStream.toByteArray());
  }
}