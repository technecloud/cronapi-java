package io.cronapp.testing.northwind;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.persistence.Persistence;

public class NorthwindExtension implements BeforeAllCallback {
  public static final String PERSISTENCE_UNIT_NAME = "io.cronapp.testing.northwind";

  @Override
  public void beforeAll(ExtensionContext extensionContext) throws Exception {
    Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
  }
}
