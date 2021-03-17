package cronapi;

import org.apache.commons.lang3.StringUtils;
import org.apache.olingo.odata2.api.ClientCallback;

import java.util.LinkedList;
import java.util.List;

public class ClientCommand {
  private Var function;
  private List<Var> params = new LinkedList<>();
  private List<Var> names = new LinkedList<>();

  public ClientCommand(String function) {
    this.function = Var.valueOf(function);
  }

  public void addParam(Object... values) {
    for (Object o : values) {
      if (o instanceof Var) {
        Var objectVar = (Var) o;
        if (!StringUtils.isEmpty(objectVar.getId())) {
          names.add(Var.valueOf(objectVar.getId()));
        }
        params.add(Var.valueOf(objectVar.getObject()));
      } else {
        params.add(Var.valueOf(o));
      }
    }
  }

  public Var getFunction() {
    return function;
  }

  public void setFunction(Var function) {
    this.function = function;
  }

  public List<Var> getParams() {
    return params;
  }

  public void setParams(List<Var> params) {
    this.params = params;
  }

  public List<Var> getNames() {
    return names;
  }

  public void setNames(List<Var> names) {
    this.names = names;
  }

  public ClientCallback toClientCallback() {
    ClientCallback clientCallback = new ClientCallback(function.toString());
    for (Var v: params) {
      clientCallback.addParam(v);
    }

    return clientCallback;
  }
}
