package cronapi.database;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.nio.ByteBuffer;

@Converter
public class VersionConverter implements AttributeConverter<Object, Object> {
  @Override
  public Object convertToDatabaseColumn(Object value) {
    if (value instanceof Long) {
      return value;
    }
    return null;
  }

  @Override
  public Object convertToEntityAttribute(Object value) {
    if (value instanceof byte[]) {
      ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
      buffer.put((byte[]) value);
      buffer.flip();
      return buffer.getLong();
    }
    if (value instanceof Long) {
      return value;
    }
    return null;
  }
}