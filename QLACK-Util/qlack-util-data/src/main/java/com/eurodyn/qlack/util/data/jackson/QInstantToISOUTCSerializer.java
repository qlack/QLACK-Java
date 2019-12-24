package com.eurodyn.qlack.util.data.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * A Jackson serializer for {@link Instant} fields converting to ISO datetime
 * format in UTC.
 */
public class QInstantToISOUTCSerializer extends StdSerializer<Instant> {

  private transient DateTimeFormatter formatter =
    DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneOffset.UTC);

  /**
   * Default no-args constructor.
   */
  public QInstantToISOUTCSerializer() {
    this(null);
  }

  protected QInstantToISOUTCSerializer(Class<Instant> t) {
    super(t);
  }

  @Override
  public void serialize(Instant value, JsonGenerator gen,
    SerializerProvider provider)
    throws IOException {
    gen.writeString(formatter.format(value));
  }
}
