package com.eurodyn.qlack.util.data.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * A Jackson deserializer for {@link Instant} fields expecting an ISO datetime format in UTC.
 */
public class QInstantFromISOUTCDeserializer extends StdDeserializer<Instant> {

  /**
   * Default no-args constructor.
   */
  public QInstantFromISOUTCDeserializer() {
    this(null);
  }

  protected QInstantFromISOUTCDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public Instant deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException {
    return LocalDateTime.parse(p.getText(),  DateTimeFormatter.ISO_DATE_TIME).toInstant(ZoneOffset.UTC);
  }
}
