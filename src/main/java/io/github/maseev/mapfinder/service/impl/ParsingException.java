package io.github.maseev.mapfinder.service.impl;

public class ParsingException extends RuntimeException {

  public ParsingException(String message) {
    super(message);
  }

  public ParsingException(String message, Throwable cause) {
    super(message, cause);
  }
}
