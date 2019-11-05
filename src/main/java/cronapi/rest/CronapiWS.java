package cronapi.rest;

import cronapi.RestClient;
import cronapi.RestResult;
import cronapi.Var;
import cronapi.database.TransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.WebMethod;
import java.util.concurrent.Callable;

@RequestMapping(value = "/api/cronapi")
public class CronapiWS {

  @WebMethod
  public Var ws(String clazz, Var[] params) throws Exception {
    return runIntoTransaction(() -> {
      return cronapi.util.Operations.callBlockly(new Var(clazz), true, RestClient.getRestClient().getMethod(), params);
    }).getValue();
  }

  private RestResult runIntoTransaction(Callable<Var> callable) throws Exception {
    RestClient.getRestClient().setFilteredEnabled(true);
    Var var = Var.VAR_NULL;
    try {
      var = callable.call();
      TransactionManager.commit();
    } catch (Exception e) {
      TransactionManager.rollback();
      throw e;
    } finally {
      TransactionManager.close();
      TransactionManager.clear();
    }
    return new RestResult(var, RestClient.getRestClient().getCommands());
  }
}
