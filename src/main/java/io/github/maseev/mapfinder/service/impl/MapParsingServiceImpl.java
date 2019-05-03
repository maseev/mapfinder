package io.github.maseev.mapfinder.service.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.maseev.mapfinder.model.GeoMap;
import io.github.maseev.mapfinder.service.CloseableIterator;
import io.github.maseev.mapfinder.service.MapParsingService;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.stereotype.Service;

@Service
public class MapParsingServiceImpl implements MapParsingService {

  private static class GeoMapIterator implements CloseableIterator<GeoMap> {

    private final ObjectMapper mapper;
    private final JsonParser parser;

    GeoMapIterator(InputStream inputStream) {
      mapper = new ObjectMapper();

      try {
        parser = createParser(inputStream);

        if (parser.nextToken() != JsonToken.START_ARRAY) {
          throw new ParsingException(
            String.format("expected to see the %s token, but got %s instead",
              JsonToken.START_ARRAY, parser.getCurrentName()));
        }
      } catch (IOException ex) {
        try {
          inputStream.close();
        } catch (IOException e) {
          throw new ParsingException("failed to close the input stream", ex);
        }
        throw new ParsingException("an IO exception occurred while parsing the JSON document", ex);
      }
    }

    @Override
    public boolean hasNext() {
      try {
        return parser.nextToken() == JsonToken.START_OBJECT;
      } catch (IOException ex) {
        throw new ParsingException("an IO exception occurred while parsing the JSON document", ex);
      }
    }

    @Override
    public GeoMap next() {
      try {
        final TreeNode treeNode = mapper.readTree(parser);

        return mapper.treeToValue(treeNode, GeoMap.class);
      } catch (IOException ex) {
        throw new ParsingException("an IO exception occurred while parsing the JSON document", ex);
      }
    }

    @Override
    public void close() throws IOException {
      parser.close();
    }

    private JsonParser createParser(InputStream inputStream) throws IOException {
      return mapper.getFactory().createParser(new BufferedInputStream(inputStream));
    }
  }

  @Override
  public CloseableIterator<GeoMap> parse(InputStream inputStream) {
    return new GeoMapIterator(inputStream);
  }
}

