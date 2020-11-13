package cronapi;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VarTest {

  @Test
  void getObjectAsInt() {
    assertThat(Var.valueOf("null").getObjectAsInt()).isEqualTo(Integer.valueOf(0));
  }

  @Test
  void getObjectAsLong() {
    assertThat(Var.valueOf("null").getObjectAsLong()).isEqualTo(Long.valueOf(0));
  }

  @Test
  void getObjectAsDouble() {
    assertThat(Var.valueOf("null").getObjectAsDouble()).isEqualTo(Double.valueOf(0));
  }

  @Test
  void getObjectAsBoolean() {
    assertThat(Var.valueOf("null").getObjectAsBoolean()).isEqualTo(Boolean.FALSE);
  }

  @Test
  void getObjectAsDateTime() {
    assertThat(Var.valueOf("null").getObjectAsDateTime()).isEqualTo(Var.VAR_DATE_ZERO.getObjectAsDateTime());
  }

}