package cronapi;

import org.apache.http.client.methods.HttpRequestBase;

public class HttpDeleteWithBody extends HttpWithBody {

  public final static String METHOD_NAME = "DELETE";

  public HttpDeleteWithBody(final HttpRequestBase httpGet) {
    super();
    setURI(httpGet.getURI());
    this.setHeaders(httpGet.getAllHeaders());
  }

  @Override
  public String getMethod() {
    return METHOD_NAME;
  }
}