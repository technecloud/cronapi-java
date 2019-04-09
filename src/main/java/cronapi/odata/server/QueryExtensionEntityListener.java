package cronapi.odata.server;

import com.google.gson.JsonObject;
import cronapi.ClientCommand;
import cronapi.ErrorResponse;
import cronapi.QueryManager;
import cronapi.RestClient;
import cronapi.Utils;
import cronapi.Var;
import java.lang.reflect.Field;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.apache.olingo.odata2.api.ClientCallback;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.uri.UriInfo;
import org.apache.olingo.odata2.api.uri.expression.BinaryExpression;
import org.apache.olingo.odata2.api.uri.expression.CommonExpression;
import org.apache.olingo.odata2.api.uri.expression.FilterExpression;
import org.apache.olingo.odata2.api.uri.expression.MethodExpression;
import org.apache.olingo.odata2.api.uri.expression.PropertyExpression;
import org.apache.olingo.odata2.api.uri.info.DeleteUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityCountUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetCountUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityUriInfo;
import org.apache.olingo.odata2.api.uri.info.PostUriInfo;
import org.apache.olingo.odata2.api.uri.info.PutMergePatchUriInfo;
import org.apache.olingo.odata2.core.edm.provider.EdmSimplePropertyImplProv;
import org.apache.olingo.odata2.core.uri.UriInfoImpl;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAQueryExtensionEntityListener;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import org.apache.olingo.odata2.jpa.processor.core.ODataExpressionParser;
import org.apache.olingo.odata2.jpa.processor.core.ODataParameterizedWhereExpressionUtil;
import org.apache.olingo.odata2.jpa.processor.core.access.data.ReflectionUtil;
import org.apache.olingo.odata2.jpa.processor.core.access.data.VirtualClass;
import org.apache.olingo.odata2.jpa.processor.core.model.JPAEdmMappingImpl;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.internal.jpa.jpql.HermesParser;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.jpa.jpql.parser.DefaultEclipseLinkJPQLGrammar;
import org.eclipse.persistence.jpa.jpql.parser.Expression;
import org.eclipse.persistence.jpa.jpql.parser.GroupByClause;
import org.eclipse.persistence.jpa.jpql.parser.HavingClause;
import org.eclipse.persistence.jpa.jpql.parser.InputParameter;
import org.eclipse.persistence.jpa.jpql.parser.JPQLExpression;
import org.eclipse.persistence.jpa.jpql.parser.SelectClause;
import org.eclipse.persistence.jpa.jpql.parser.SelectStatement;
import org.eclipse.persistence.jpa.jpql.parser.WhereClause;
import org.eclipse.persistence.queries.DatabaseQuery;

public class QueryExtensionEntityListener extends ODataJPAQueryExtensionEntityListener {

  private void findInputParams(Expression expression, List<String> inputs) {

    if (expression instanceof InputParameter) {
      inputs.add(expression.toString());
    }

    if (expression.children() != null) {
      expression.children().forEach((e) -> {
        findInputParams(e, inputs);
      });
    }
  }

  private String getBlocklyMethod(UriInfo uriInfo, JsonObject customQuery) {
    String restMethod = getRestMehtod(uriInfo);

    if (QueryManager.isNull(customQuery.get("entityFullName"))) {
      String blocklyMethod = QueryManager.getBlocklyMethod(customQuery, restMethod);
      if (customQuery.get("baseEntity") != null && "default".equals(blocklyMethod)) {
        return null;
      } else {
        return blocklyMethod;
      }
    }

    return null;
  }

  private String getRestMehtod(UriInfo uriInfo) {
    String restMethod = RestClient.getRestClient().getMethod();
    if (uriInfo.getFilter() != null || (uriInfo.getKeyPredicates() != null && uriInfo.getKeyPredicates().size() > 0)) {
      restMethod = "FILTER";
    }

    if (uriInfo.isCount()) {
      restMethod = "COUNT";
    }

    return restMethod;
  }

  public Query getBaseQuery(UriInfo uriInfo, EntityManager em) throws ODataJPARuntimeException {

    try {

      JsonObject customQuery = null;

      try {
        customQuery = QueryManager.getQuery(uriInfo.getTargetEntitySet().getName());
      } catch (Exception e) {
        //No Command
      }

      EdmEntityType entityType = uriInfo.getTargetEntitySet().getEntityType();

      boolean isJPQL = entityType.getName().equals("jpql");

      if (customQuery != null || isJPQL) {

        if (!isJPQL) {
          QueryManager.checkSecurity(customQuery, RestClient.getRestClient().getMethod());
        }

        boolean isBlockly = !isJPQL && QueryManager.isNull(customQuery.get("entityFullName"));

        String restMethod = getRestMehtod(uriInfo);

        if (isBlockly) {
          String blocklyMethod = getBlocklyMethod(uriInfo, customQuery);
          if (blocklyMethod == null) {
            return null;
          }
        }

        Query query = null;
        String whereExpression = null;
        String selectExpression = null;
        SelectStatement selectStatement = null;
        String jpqlStatement = "";
        String alias = null;
        String orderBy = null;
        List<String> inputs = new LinkedList<>();
        boolean hasGroupBy = false;

        if (!isBlockly) {
          if (isJPQL) {
            jpqlStatement = RestClient.getRestClient().getParameter("jpql");
          } else {
            jpqlStatement = QueryManager.getJPQL(customQuery, false);
          }

          JPQLExpression jpqlExpression = new JPQLExpression(
              jpqlStatement,
              DefaultEclipseLinkJPQLGrammar.instance(),
              true
          );

          findInputParams(jpqlExpression, inputs);

          selectStatement = ((SelectStatement) jpqlExpression.getQueryStatement());
          String selection = ((SelectClause) selectStatement.getSelectClause()).getSelectExpression().toActualText();
          String distinct = ((SelectClause) selectStatement.getSelectClause()).getActualDistinctIdentifier();
          boolean hasDistinct = ((SelectClause) selectStatement.getSelectClause()).hasDistinct();
          String mainAlias = JPQLParserUtil.getMainAlias(jpqlExpression);
          hasGroupBy = selectStatement.getGroupByClause().getLength() > 0 || uriInfo.getOrderBy() != null;

          if (!selection.contains(".") && !selection.contains(",")) {
            alias = mainAlias;
          }

          if (uriInfo.isCount() || uriInfo.rawEntity()) {
            setField(selectStatement, "selectClause", null);
            if (uriInfo.rawEntity()) {
              selectExpression = "SELECT " + mainAlias + " ";
            } else {
              if (hasDistinct) {
                selectExpression = "SELECT count( " + distinct + " " + selection + ") ";
              } else {
                selectExpression = "SELECT count( " + mainAlias + ") ";
              }
            }

            if (selectStatement.hasOrderByClause()) {
              setField(selectStatement, "orderByClause", null);
            }

            jpqlStatement = selectStatement.toString();
          }

          if (selectStatement.hasOrderByClause()) {
            orderBy = selectStatement.getOrderByClause().toString();
            setField(selectStatement, "orderByClause", null);
            jpqlStatement = selectStatement.toString();
          }

          if (uriInfo.getOrderBy() != null && !uriInfo.isCount()) {
            String orderExpression = ODataExpressionParser.parseToJPAOrderByExpression(uriInfo.getOrderBy(), alias);
            orderBy = "ORDER BY " + orderExpression;
          }

        }

        ODataExpressionParser.reInitializePositionalParameters();
        Map<String, Map<Integer, Object>> parameterizedExpressionMap = new HashMap<String, Map<Integer, Object>>();

        if (uriInfo.getFilter() != null) {
          checkFilter(entityType, uriInfo.getFilter());
          whereExpression = ODataExpressionParser.parseToJPAWhereExpression(uriInfo.getFilter(), alias);
          parameterizedExpressionMap.put(whereExpression, ODataExpressionParser.getPositionalParameters());
          ODataParameterizedWhereExpressionUtil.setParameterizedQueryMap(parameterizedExpressionMap);
          ODataExpressionParser.reInitializePositionalParameters();
        }

        if (uriInfo.getKeyPredicates().size() > 0) {
          whereExpression = ODataExpressionParser.parseKeyPredicates(uriInfo.getKeyPredicates(), alias);
          parameterizedExpressionMap.put(whereExpression, ODataExpressionParser.getPositionalParameters());
          ODataParameterizedWhereExpressionUtil.setParameterizedQueryMap(parameterizedExpressionMap);
          ODataExpressionParser.reInitializePositionalParameters();
        }

        String where = null;
        String having = null;
        String groupBy = null;

        if (whereExpression != null) {

          if (selectStatement != null && selectStatement.hasWhereClause()) {
            where = ((WhereClause) selectStatement.getWhereClause()).getConditionalExpression().toString();
            setField(selectStatement, "whereClause", null);
            jpqlStatement = selectStatement.toString();
          }

          if (selectStatement != null && selectStatement.hasGroupByClause()) {
            groupBy = ((GroupByClause) selectStatement.getGroupByClause()).toString();
            setField(selectStatement, "groupByClause", null);
            jpqlStatement = selectStatement.toString();
          }

          if (selectStatement != null && selectStatement.hasHavingClause()) {
            having = ((HavingClause) selectStatement.getHavingClause()).toString();
            setField(selectStatement, "havingClause", null);
            jpqlStatement = selectStatement.toString();
          }

          if (where != null) {
            jpqlStatement += " WHERE (" + where + ") AND " + whereExpression;
          } else {
            jpqlStatement += " WHERE " + whereExpression;
          }

          if (having != null) {
            jpqlStatement += " " + having;
          }

          if (groupBy != null) {
            jpqlStatement += " " + groupBy;
          }
        }

        if (orderBy != null) {
          jpqlStatement += " " + orderBy;
        }

        if (selectExpression != null) {
          jpqlStatement = selectExpression + " " + jpqlStatement;
        }

        Map<String, Map<Integer, Object>> parameterizedMap = ODataParameterizedWhereExpressionUtil.getParameterizedQueryMap();

        int maxParam = 0;

        if (parameterizedMap != null && parameterizedMap.size() > 0) {
          for (Map.Entry<String, Map<Integer, Object>> parameterEntry : parameterizedMap.entrySet()) {
            if (jpqlStatement.contains(parameterEntry.getKey())) {
              Map<Integer, Object> positionalParameters = parameterEntry.getValue();
              for (Map.Entry<Integer, Object> param : positionalParameters.entrySet()) {
                if (param.getKey() > maxParam) {
                  maxParam = param.getKey();
                }
              }
            }
          }
        }

        int i = maxParam;
        for (String param : inputs) {
          i++;
          jpqlStatement = jpqlStatement.replace(param, "?" + i);
        }

        if (!isBlockly) {
          query = em.createQuery(jpqlStatement);
        } else {
          String type = "select";
          if (uriInfo.isCount()) {
            type = "count";
          }
          query = new BlocklyQuery(customQuery, restMethod, type, jpqlStatement, (uriInfo.getFilter() != null ? uriInfo.getFilter().getExpressionString() : ""), uriInfo.getTargetEntitySet().getName());
        }

        if (parameterizedMap != null && parameterizedMap.size() > 0) {
          for (Map.Entry<String, Map<Integer, Object>> parameterEntry : parameterizedMap.entrySet()) {
            if (jpqlStatement.contains(parameterEntry.getKey())) {
              Map<Integer, Object> positionalParameters = parameterEntry.getValue();
              for (Map.Entry<Integer, Object> param : positionalParameters.entrySet()) {
                if (param.getValue() instanceof Calendar || param.getValue() instanceof Timestamp) {
                  query.setParameter(param.getKey(), (Calendar) param.getValue(), TemporalType.TIMESTAMP);
                } else if (param.getValue() instanceof Time) {
                  query.setParameter(param.getKey(), (Time) param.getValue(), TemporalType.TIME);
                } else {
                  query.setParameter(param.getKey(), param.getValue());
                }
              }
              parameterizedMap.remove(parameterEntry.getKey());
              ODataParameterizedWhereExpressionUtil.setJPQLStatement(null);
              break;
            }
          }
        }

        if (inputs.size() > 0) {
          AbstractSession session = (AbstractSession) ((EntityManagerImpl) em.getDelegate()).getActiveSession();
          HermesParser parser = new HermesParser();
          DatabaseQuery queryParsed = parser.buildQuery(jpqlStatement, session);
          List<Class> argsTypes = queryParsed.getArgumentTypes();
          List<String> argsNames = queryParsed.getArguments();
          i = maxParam;
          for (String param : inputs) {
            i++;
            String strValue = RestClient.getRestClient().getParameter(param.substring(1));
            int idx = argsNames.indexOf(String.valueOf(i));
            Class type = null;
            if (idx != -1) {
              type = argsTypes.get(idx);

              if (strValue != null) {
                Var requestParam = null;
                if (strValue.contains("@@") || type.getSimpleName().equals("Object")) {
                  requestParam = Var.valueOf(Var.deserialize(strValue));
                } else {
                  requestParam = Var.valueOf(strValue);
                }

                if (param.indexOf("__") > 0) {
                  Class paramClass = Var.getType(param.substring(1));
                  type = paramClass;
                }

                query.setParameter(i, requestParam.getObject(type));
              } else {
                Map<String, Var> customValues = new LinkedHashMap<>();
                customValues.put("entityName", Var.valueOf(uriInfo.getTargetEntitySet().getName()));

                query.setParameter(i,
                    QueryManager.getParameterValue(customQuery, param.substring(1), customValues)
                        .getObject(type));
              }
            }
          }
        }

        if (uriInfo.isCount() && hasGroupBy) {
          List result = query.getResultList();
          int count = result.size();
          selectExpression = "SELECT " + count;
          query = em.createNativeQuery(selectExpression);
        }

        return query;
      }

      if (entityType.getMapping() != null && ((JPAEdmMappingImpl) entityType.getMapping()).getJPAType() != null) {
        Class clazz = ((JPAEdmMappingImpl) entityType.getMapping()).getJPAType();
        QueryManager.checkSecurity(clazz, RestClient.getRestClient().getMethod());
      }

    } catch (Exception e) {
      throw ErrorResponse.createException(e, RestClient.getRestClient().getMethod());
    }

    return null;
  }

  @Override
  public Query getQuery(GetEntitySetUriInfo uriInfo, EntityManager em) throws ODataJPARuntimeException {
    return this.getBaseQuery((UriInfo) uriInfo, em);
  }

  @Override
  public Query getQuery(GetEntityCountUriInfo uriInfo, EntityManager em) throws ODataJPARuntimeException {
    return this.getBaseQuery((UriInfo) uriInfo, em);
  }

  @Override
  public Query getQuery(GetEntitySetCountUriInfo uriInfo, EntityManager em) throws ODataJPARuntimeException {
    return this.getBaseQuery((UriInfo) uriInfo, em);
  }

  @Override
  public Query getQuery(GetEntityUriInfo uriInfo, EntityManager em) throws ODataJPARuntimeException {
    return this.getBaseQuery((UriInfo) uriInfo, em);
  }

  @Override
  public Query getQuery(PutMergePatchUriInfo uriInfo, EntityManager em) throws ODataJPARuntimeException {
    return this.getBaseQuery((UriInfo) uriInfo, em);
  }

  @Override
  public Query getQuery(DeleteUriInfo uriInfo, EntityManager em) throws ODataJPARuntimeException {
    return this.getBaseQuery((UriInfo) uriInfo, em);
  }

  private void setField(Object obj, String name, Object value) {
    try {

      Field field;
      try {
        field = obj.getClass().getDeclaredField(name);
      } catch (Exception e) {
        field = obj.getClass().getSuperclass().getDeclaredField(name);
      }

      if (field != null) {
        field.setAccessible(true);
        field.set(obj, value);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Object getField(Object obj, String name) {
    try {
      Field field;
      try {
        field = obj.getClass().getDeclaredField(name);
      } catch (Exception e) {
        field = obj.getClass().getSuperclass().getDeclaredField(name);
      }

      if (field != null) {
        field.setAccessible(true);
        return field.get(obj);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return null;
  }

  @Override
  public boolean authorizeProperty(EdmEntityType entityType, EdmProperty property) {

    JsonObject query = null;

    try {
      try {
        query = QueryManager.getQuery(entityType.getName());
      } catch (Exception e) {
        //No Command
      }

      if (query != null) {
        return QueryManager.isFieldAuthorized(query, property.getName(), RestClient.getRestClient().getMethod());
      }

      if (entityType.getMapping() != null && ((JPAEdmMappingImpl) entityType.getMapping()).getJPAType() != null) {
        Class clazz = ((JPAEdmMappingImpl) entityType.getMapping()).getJPAType();
        return QueryManager.isFieldAuthorized(clazz, property.getName(), RestClient.getRestClient().getMethod());
      }

      return true;

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void checkOprAuthorization(final UriInfo uriView) throws ODataJPARuntimeException {
    JsonObject query = null;

    try {
      EdmEntityType entityType = uriView.getTargetEntitySet().getEntityType();

      try {
        query = QueryManager.getQuery(uriView.getTargetEntitySet().getName());
      } catch (Exception e) {
        //No Command
      }

      if (query != null) {
        QueryManager.checkSecurity(query, RestClient.getRestClient().getMethod());
      } else {
        if (entityType.getMapping() != null && ((JPAEdmMappingImpl) entityType.getMapping()).getJPAType() != null) {
          Class clazz = ((JPAEdmMappingImpl) entityType.getMapping()).getJPAType();
          QueryManager.checkSecurity(clazz, RestClient.getRestClient().getMethod());
        }
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void checkAuthorization(final PostUriInfo postView) throws ODataJPARuntimeException {
    this.checkOprAuthorization((UriInfo) postView);
  }

  @Override
  public void checkAuthorization(final PutMergePatchUriInfo putView) throws ODataJPARuntimeException {
    this.checkOprAuthorization((UriInfo) putView);
  }

  @Override
  public void checkAuthorization(final DeleteUriInfo deleteView) throws ODataJPARuntimeException {
    this.checkOprAuthorization((UriInfo) deleteView);
  }

  @Override
  public void checkEntityGetAuthorization(final EdmEntityType entityType) throws ODataJPARuntimeException {
    try {
      if (entityType.getMapping() != null && ((JPAEdmMappingImpl) entityType.getMapping()).getJPAType() != null) {
        Class clazz = ((JPAEdmMappingImpl) entityType.getMapping()).getJPAType();
        QueryManager.checkSecurity(clazz, RestClient.getRestClient().getMethod());
      }
    } catch (Exception e) {
      throw ErrorResponse.createException(e, RestClient.getRestClient().getMethod());
    }
  }

  private Map<String, Object> convertValues(Map<String, Object> defaults, EdmEntityType entityType) throws Exception {
    if (defaults != null) {
      for (String key : defaults.keySet()) {
        Class clazz = ((JPAEdmMappingImpl) ((EdmSimplePropertyImplProv) entityType.getProperty(key)).getMapping()).getOriginaType();
        Object value = defaults.get(key);
        value = Var.valueOf(value).getObject(clazz);
        defaults.put(key, value);
      }
    }

    return defaults;
  }

  @Override
  public Map<String, Object> getDefaultFieldValues(final EdmEntityType entityType, Object data) throws ODataJPARuntimeException {
    JsonObject query = null;

    try {

      try {
        query = QueryManager.getQuery(entityType.getName());
      } catch (Exception e) {
        //No Command
      }

      if (query != null && RestClient.getRestClient() != null && RestClient.getRestClient().getRequest() != null) {
        return convertValues(QueryManager.getDefaultValues(query, data), entityType);
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return null;
  }

  @Override
  public Map<String, Object> getCalcFieldValues(final EdmEntityType entityType, Object data) throws ODataJPARuntimeException {
    JsonObject query = null;

    try {

      try {
        query = QueryManager.getQuery(entityType.getName());
      } catch (Exception e) {
        //No Command
      }

      if (query != null && RestClient.getRestClient() != null && RestClient.getRestClient().getRequest() != null) {
        return convertValues(QueryManager.getCalcFieldValues(query, data), entityType);
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return null;
  }

  @Override
  public void checkFilter(final EdmEntityType entityType, FilterExpression filter) throws ODataJPARuntimeException {
    try {
      JsonObject query = null;

      try {
        query = QueryManager.getQuery(entityType.getName());
      } catch (Exception e) {
        //No Command
      }

      List<String> filters = new LinkedList<>();
      visitExpression(filter, filters);
      if (query != null) {
        QueryManager.checkFilterSecurity(query, filters);
      } else {
        Class clazz = ((JPAEdmMappingImpl) entityType.getMapping()).getJPAType();
        QueryManager.checkEntityFilterSecurity(clazz, filters);
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void doCheckFilter(BinaryExpression expression, List<String> filters) {
    visitExpression(expression.getLeftOperand(), filters);
    visitExpression(expression.getRightOperand(), filters);
  }

  private void doCheckFilter(FilterExpression expression, List<String> filters) {
    visitExpression(expression.getExpression(), filters);
  }

  private void doCheckFilter(PropertyExpression expression, List<String> filters) {
    filters.add(expression.getPropertyName());
  }

  private void doCheckFilter(MethodExpression expression, List<String> filters) {
    for (CommonExpression e : expression.getParameters()) {
      visitExpression(e, filters);
    }
  }

  private void visitExpression(CommonExpression expression, List<String> filters) {
    if (expression instanceof BinaryExpression) {
      doCheckFilter((BinaryExpression) expression, filters);
    } else if (expression instanceof PropertyExpression) {
      doCheckFilter((PropertyExpression) expression, filters);
    } else if (expression instanceof FilterExpression) {
      doCheckFilter((FilterExpression) expression, filters);
    } else if (expression instanceof MethodExpression) {
      doCheckFilter((MethodExpression) expression, filters);
    }
  }

  @Override
  public List<ClientCallback> getClientCallbacks() {
    List<ClientCallback> callbacks = null;
    for (ClientCommand command : RestClient.getRestClient().getCommands()) {
      if (callbacks == null) {
        callbacks = new LinkedList<>();
      }
      callbacks.add(command.toClientCallback());
    }
    return callbacks;
  }

  @Override
  public Object execEvent(final UriInfo infoView, final EdmEntityType entityType, String type, Object data) throws ODataJPARuntimeException {
    if (infoView != null) {
      try {
        JsonObject query = null;
        if (data != null) {
          Utils.processCloudFields(data);
        }

        try {
          query = QueryManager.getQuery(entityType.getName());
        } catch (Exception e) {
          //No Command
        }

        if (query != null) {
          List<Object> keys = new LinkedList<>();
          try {
            for (String key : entityType.getKeyPropertyNames()) {
              keys.add(ReflectionUtil.getter(data, key));
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
          Var result = QueryManager.executeEvent(query, data, type, keys, entityType.getName());
          if (result != null) {
            return result.getObject();
          }
        }

        ((UriInfoImpl) infoView).setClientCallbacks(getClientCallbacks());

      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    return null;
  }

  @Override
  public Object processNew(UriInfo infoView) {

    try {
      final EdmEntitySet oDataEntitySet = infoView.getTargetEntitySet();
      final EdmEntityType entityType = oDataEntitySet.getEntityType();

      JsonObject query = null;

      try {
        query = QueryManager.getQuery(entityType.getName());
      } catch (Exception e) {
        //No Command
      }

      if (query != null) {

        Object jpaEntity = ((JPAEdmMappingImpl) entityType.getMapping()).getJPAType().newInstance();

        String jpqlStatement = QueryManager.getJPQL(query, false);

        JPQLExpression jpqlExpression = new JPQLExpression(
            jpqlStatement,
            DefaultEclipseLinkJPQLGrammar.instance(),
            true
        );

        String mainAlias = JPQLParserUtil.getMainAlias(jpqlExpression);

        VirtualClass virtualClass = new VirtualClass();

        for (String name : entityType.getPropertyNames()) {
          EdmSimplePropertyImplProv type = (EdmSimplePropertyImplProv) entityType.getProperty(name);
          if (type.getMapping() != null && type.getMapping().getInternalExpression() != null) {
            String expression = type.getMapping().getInternalExpression();
            String[] parts = expression.split("\\.");
            if (parts.length == 2) {
              String f = parts[1];
              if (parts[0].equals(mainAlias)) {
                Field field = ReflectionUtil.getField(jpaEntity, f);
                if (field != null) {
                  field.setAccessible(true);
                  Object o = field.get(jpaEntity);
                  virtualClass.set(name, o);
                }
              }
            }
          }
        }

        return virtualClass;

      }

    } catch (Exception e) {

    }

    return null;
  }

  @Override
  public Object overridePost(UriInfo infoView, Object entity) {
    JsonObject query = null;

    try {
      final EdmEntitySet oDataEntitySet = infoView.getTargetEntitySet();
      final EdmEntityType entityType = oDataEntitySet.getEntityType();

      query = QueryManager.getQuery(entityType.getName());

      String blocklyMethod = getBlocklyMethod(infoView, query);
      if (blocklyMethod == null) {
        return null;
      }

      if (query != null && QueryManager.isNull(query.get("entityFullName"))) {

        Var result = QueryManager.executeBlockly(query, getRestMehtod(infoView), Var.valueOf(entity));
        if (result != null && !result.isNull()) {
          if (query.get("baseEntity") != null) {
            return result.getObject();
          } else {
            return result;
          }
        } else {
          return entity;
        }
      }

    } catch (Exception e) {
      //No Command
    }

    return null;
  }

  @Override
  public boolean canOverridePut(UriInfo infoView) {
    try {
      final EdmEntitySet oDataEntitySet = infoView.getTargetEntitySet();
      final EdmEntityType entityType = oDataEntitySet.getEntityType();

      JsonObject query = QueryManager.getQuery(entityType.getName());

      String blocklyMethod = getBlocklyMethod(infoView, query);
      if (blocklyMethod == null) {
        return false;
      }

      return query != null && QueryManager.isNull(query.get("entityFullName"));

    } catch (Exception e) {
      //NoCommand
    }

    return false;
  }

  @Override
  public boolean canOverrideDelete(UriInfo infoView) {
    return canOverridePut(infoView);
  }

  @Override
  public boolean canOverridePost(UriInfo infoView) {
    return canOverridePut(infoView);
  }

  @Override
  public Object overridePut(UriInfo infoView, Object entity) {
    return overridePost(infoView, entity);
  }

  @Override
  public boolean overrideDelete(UriInfo infoView, Object entity) {
    JsonObject query = null;

    try {
      final EdmEntitySet oDataEntitySet = infoView.getTargetEntitySet();
      final EdmEntityType entityType = oDataEntitySet.getEntityType();

      query = QueryManager.getQuery(entityType.getName());

      String blocklyMethod = getBlocklyMethod(infoView, query);
      if (blocklyMethod == null) {
        return false;
      }

      if (query != null && QueryManager.isNull(query.get("entityFullName"))) {

        QueryManager.executeBlockly(query, getRestMehtod(infoView), Var.valueOf(entity));
        return true;
      }

    } catch (Exception e) {
      //No Command
    }

    return false;
  }

}
