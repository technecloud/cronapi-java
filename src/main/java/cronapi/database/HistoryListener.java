package cronapi.database;

import com.google.gson.*;
import cronapi.RestClient;
import cronapi.Utils;
import cronapi.util.ReflectionUtils;

import javax.persistence.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Set;

public class HistoryListener {

  private static final String CURRENT_IP = getCurrentIp();

  private static String getCurrentIp() {

    try {
      Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
      while (networkInterfaces.hasMoreElements()) {
        NetworkInterface ni = networkInterfaces.nextElement();
        Enumeration<InetAddress> nias = ni.getInetAddresses();
        while (nias.hasMoreElements()) {
          InetAddress ia = nias.nextElement();
          if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress() && ia instanceof Inet4Address) {
            return ia.getHostAddress();
          }
        }
      }
    } catch (SocketException e2) {
      // Abafa
    }

    return null;
  }

  @PostPersist
  private void beforeInsert(Object object) {
    beforeAnyOperation(object, 0);
  }

  @PostRemove
  private void beforeUpdate(Object object) {
    beforeAnyOperation(object, 1);
  }

  @PostUpdate
  private void beforeRemove(Object object) {
    beforeAnyOperation(object, 2);
  }

  private JsonElement toElement(Gson gson, Object value) {
    if (value instanceof Date) {
      return new JsonPrimitive(Utils.getISODateFormat().format((Date) value));
    } else {
      JsonElement element = gson.toJsonTree(value);
      return element;
    }
  }

  private void beforeAnyOperation(Object object, int operation) {

    Class auditClazz = null;

    try {
      auditClazz = Class.forName(object.getClass().getPackage().getName() + ".AuditLog");

      Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
          if (fieldAttributes.getDeclaringClass() == object.getClass() || fieldAttributes.getAnnotation(Id.class) != null) {
            return false;
          }
          return true;
        }

        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
          return false;
        }
      }).create();

      Class domainClass = object.getClass();
      EntityManager em = TransactionManager.getEntityManager(domainClass);

      JsonElement objectJson = gson.toJsonTree(object);

      JsonElement keys = new JsonObject();

      EntityType type = em.getMetamodel().entity(domainClass);

      Set attrs = JPAUtil.getAjustedAttributes(type);

      int total = 0;
      for (Object obj : attrs) {
        SingularAttribute field = (SingularAttribute) obj;
        if (field.isId()) {
          total++;
        }
      }

      for (Object obj : attrs) {
        SingularAttribute field = (SingularAttribute) obj;
        if (field.isId()) {
          Object value = ReflectionUtils.getField(object, field.getName());
          if (total == 1) {
            keys = toElement(gson, value);
          } else {
            keys.getAsJsonObject().add(field.getName(), toElement(gson, value));
          }
        }
      }

      Object auditLog = auditClazz.newInstance();

      ReflectionUtils.setField(auditLog, "type", object.getClass().getName());
      ReflectionUtils.setField(auditLog, "command", operation);
      ReflectionUtils.setField(auditLog, "date", new Date());
      ReflectionUtils.setField(auditLog, "objectData", objectJson.toString());
      if (keys.isJsonPrimitive() && keys.getAsJsonPrimitive().isString()) {
        ReflectionUtils.setField(auditLog, "objectKeys", keys.getAsString());
      } else {
        ReflectionUtils.setField(auditLog, "objectKeys", keys.toString());
      }
      if (RestClient.getRestClient() != null) {
        ReflectionUtils.setField(auditLog, "user", RestClient.getRestClient().getUser() != null ? RestClient.getRestClient().getUser().getUsername() : null);
        ReflectionUtils.setField(auditLog, "host", RestClient.getRestClient().getHost());
        ReflectionUtils.setField(auditLog, "agent", RestClient.getRestClient().getAgent());
      }
      ReflectionUtils.setField(auditLog, "server", CURRENT_IP);

      em.persist(auditLog);

      System.out.println(objectJson.toString());
      System.out.println(keys.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] x) throws Exception {
    Gson gson = new Gson();
    JsonElement element = gson.toJsonTree(null);

    System.out.println(element.toString());
  }
}
