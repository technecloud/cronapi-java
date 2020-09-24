package cronapi;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;

public class HttpGetWithBody extends HttpEntityEnclosingRequestBase {

  public final static String METHOD_NAME = "GET";

  public HttpGetWithBody() { super(); }

  public HttpGetWithBody(final URI uri) {
    super();
    setURI(uri);
  }

  public HttpGetWithBody(final HttpRequestBase httpGet) {
    super();
    setURI(httpGet.getURI());
    this.setHeaders(httpGet.getAllHeaders());
  }

  @Override
  public void setEntity(final HttpEntity entity) {
    super.setEntity(entity);
    this.remakeURI();
  }

  private void remakeURI() {
    try {
      URI uri = new URIBuilder(this.getURI()).build();
      this.setURI(uri);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @throws IllegalArgumentException if the uri is invalid.
   */
  public HttpGetWithBody(final String uri) {
    super();
    setURI(URI.create(uri));
  }

  @Override
  public String getMethod() {
    return METHOD_NAME;
  }
}