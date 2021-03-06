package cronapi;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public abstract class  HttpWithBody extends HttpEntityEnclosingRequestBase {

 private static final Logger log = LoggerFactory.getLogger(HttpWithBody.class);

  public HttpWithBody() {
   super();
  }

  @Override
  public void setEntity(final HttpEntity entity) {
    super.setEntity(entity);
    this.remakeURI();
  }

  protected void remakeURI() {
    try {
      URI uri = new URIBuilder(this.getURI()).build();
      this.setURI(uri);
    }
    catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public abstract String getMethod();
}
