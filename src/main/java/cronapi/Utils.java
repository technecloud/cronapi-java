package cronapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Id;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import cronapi.i18n.Messages;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * Classe que representa ...
 * 
 * @author Usuário de Teste
 * @version 1.0
 * @since 2017-03-28
 *
 */

public class Utils {

	private static final Map<String, DateFormat[]> DATE_FORMATS = new HashMap<>();

	private static final Map<String, DateFormat> DATETIME_FORMAT = new HashMap<>();

	private static final Map<String, DateFormat> PARSE_DATETIME_FORMAT = new HashMap<>();

	static {
		DATE_FORMATS.put("pt", getGenericParseDateFormat(new Locale("pt", "BR")));
		DATE_FORMATS.put("en", getGenericParseDateFormat(new Locale("en", "US")));

		PARSE_DATETIME_FORMAT.put("pt", new SimpleDateFormat(Messages.getBundle(new Locale("pt", "BR")).getString("ParseDateFormat")));
		PARSE_DATETIME_FORMAT.put("en", new SimpleDateFormat(Messages.getBundle(new Locale("en", "US")).getString("ParseDateFormat")));

		DATETIME_FORMAT.put("pt", new SimpleDateFormat(Messages.getBundle(new Locale("pt", "BR")).getString("DateTimeFormat")));
		DATETIME_FORMAT.put("en", new SimpleDateFormat(Messages.getBundle(new Locale("en", "US")).getString("DateTimeFormat")));
	}

	public static boolean deleteFolder(File dir) throws Exception {
		if (dir.isDirectory()) {
			Path rootPath = Paths.get(dir.getPath());
			Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder()).map(Path::toFile)
					.peek(System.out::println).forEach(File::delete);
		}
		return dir.delete();
	}

	public static String MD5AsStringFromFile(File file) throws Exception {
		String filename = file.getPath();
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(Files.readAllBytes(Paths.get(filename)));
		byte[] digest = md.digest();
		String myChecksum = DatatypeConverter.printHexBinary(digest).toUpperCase();
		return myChecksum;
	}

	public static void copyFileTo(File src, File dst) throws Exception {
		if (src == null || dst == null) {
			return;
		}
		Files.copy(Paths.get(src.getPath()), Paths.get(dst.getPath()), StandardCopyOption.REPLACE_EXISTING);
	}

	public static StringBuilder getFileContent(FileInputStream fstream) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream) fstream));
		StringBuilder out = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			out.append(line);
			out.append(System.getProperty("line.separator"));
		}
		reader.close();
		return out;
	}

	public static String getFileContent(String file) throws Exception {
		return FileUtils.readFileToString(new File(file));
	}

	public static boolean stringToBoolean(final String str) {
		if (str == null)
			return false;
		return Boolean.valueOf(str.trim());
	}

	public static byte[] getFromBase64(String base64) {
		byte[] bytes = null;
		if (base64 != null && !base64.equals("")) {
			bytes = Base64.getDecoder().decode(base64);
		}
		return bytes;
	}

	public static String stringToJs(String string) {
		return StringEscapeUtils.escapeEcmaScript(string);
	}

	public static int getFromCalendar(Date date, int field) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(field);
	}

  public static List<Field> findIds(Object obj) {
    Field[] fields = obj instanceof Class?((Class) obj).getDeclaredFields():obj.getClass().getDeclaredFields();
    List<Field> pks = new ArrayList<>();
    for (Field f : fields) {
      Annotation[] annotations = f.getDeclaredAnnotations();
      for (int i = 0; i < annotations.length; i++) {
        if (annotations[i].annotationType().equals(Id.class)) {
          pks.add(f);
        }
      }
    }
    return pks;
  }

	public static Method findMethod(Object obj, String method) {
    Method[] methods = obj instanceof Class?((Class) obj).getMethods():obj.getClass().getMethods();
		for (Method m : methods) {
			if (m.getName().equalsIgnoreCase(method)) {
				return m;
			}
		}
		return null;
	}

	public static Calendar toGenericCalendar(String value) {
		Date date = null;
		try {
			if (NumberUtils.isNumber(value)) {
				Double d = Double.valueOf(value);
				date = new Date(d.longValue());
			}
		} catch (Exception e) {
			//
		}

		if (date == null) {
			DateFormat[] formats = DATE_FORMATS.get(Messages.getLocale().getLanguage());
			if (formats == null) {
				formats = DATE_FORMATS.get("pt");
			}
			for (DateFormat format : formats) {
				try {
					date = format.parse(value);
					break;
				} catch (Exception e2) {
					//Abafa
				}
			}
		}

		if (date != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			return c;
		}

		return null;
	}

	public static Calendar toCalendar(String value, String mask) {
		if (value == null) {
			return null;
		}

		try {
			if (mask != null && !mask.isEmpty()) {
				SimpleDateFormat format = new SimpleDateFormat(mask);
				Date date = format.parse(value);
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				return c;
			}
		} catch (Exception e) {
			//
		}

		return toGenericCalendar(value);
	}

	public static final DateFormat getParseDateFormat () {
		DateFormat format = PARSE_DATETIME_FORMAT.get(Messages.getLocale().getLanguage());
		if (format == null) {
			format = PARSE_DATETIME_FORMAT.get("pt");
		}

		return format;
	}

	public static final DateFormat getDateFormat () {
		DateFormat format = DATETIME_FORMAT.get(Messages.getLocale().getLanguage());
		if (format == null) {
			format = DATETIME_FORMAT.get("pt");
		}

		return format;
	}

	private static DateFormat[] getGenericParseDateFormat(Locale locale) {
		String datePattern = Messages.getBundle(locale).getString("ParseDateFormat");

		final String[] formats = { (datePattern + " H:m:s.SSS"), (datePattern + " H:m:s"), (datePattern + " H:m"), "yyyy-M-d H:m:s.SSS", "yyyy-M-d H:m:s",
				"yyyy-M-d H:m", datePattern, "yyyy-M-d", "H:m:s", "H:m" };

		DateFormat[] dateFormats = new DateFormat[formats.length + 1];
		dateFormats[0] = new ISO8601DateFormat();

		for (int i=0;i<formats.length;i++) {
			dateFormats[i+1] = new SimpleDateFormat(formats[i]);
		}

		return dateFormats;
	}
	
	private static String fillIndexesIfExists(List<String> indexes, String key) {
	  String index = null;
	  if (key.contains("[") && key.endsWith("]")) {
      String searchBrackets = key;
      while (searchBrackets.indexOf("[") > -1) {
        index = searchBrackets.substring(searchBrackets.indexOf("[")+1, searchBrackets.indexOf("]"));
        indexes.add(index);
        if (searchBrackets.indexOf("]") < (searchBrackets.length() - 1))
          searchBrackets = searchBrackets.substring(searchBrackets.indexOf("]") + 1);
        else
          searchBrackets = searchBrackets.substring(searchBrackets.indexOf("]"));
      }
      key = key.substring(0, key.indexOf("["));
    }
    return key;
	}
	
	private static final Object getValueByKey(Object obj, String key) {
	  if (obj instanceof JsonObject)
      return ((JsonObject) obj).get(key);
    else if (obj instanceof java.util.HashMap)
      return ((Map) obj).get(key);
    else
      return getFieldReflection(obj, key);
	}
	
	private static final Object getFieldReflection(Object obj, String key) {
	  Object o = null;
	  try {
	    Class c = obj.getClass();
  	  Field f = c.getDeclaredField(key);
  	  f.setAccessible(true);
  	  o = f.get(obj);  
	  }
	  catch (Exception e) {
	  }
	  return o;
	}
	
	private static final Object getValueByIndex(Object obj, int idx) {
	  try {
  	  if (obj instanceof JsonArray)
        return ((JsonArray)obj).get(idx);
      else if (obj instanceof java.util.ArrayList)
        return ((List) obj).get(idx);
      else
        return ((Object[])obj)[idx];
	  }
	  catch(Exception e) {
	    //Dont has index, return null
	    return null;
	  }
	}
	
	private static final void setValueByIndex(Object list, Object valueToSet, int idx) {
	  
  	Object val = valueToSet;
  	if (val instanceof Var)
  	  val = ((Var)val).getObject();
	  
    if (list instanceof JsonArray) {
      
      if (idx <= (((JsonArray)list).size() -1) ) {
        if (val instanceof JsonElement)
          ((JsonArray) list).set(idx, (JsonElement)val);
        else if (val instanceof Character)
          ((JsonArray) list).set(idx, new JsonPrimitive((Character)val));
        else if (val instanceof Number)
          ((JsonArray) list).set(idx, new JsonPrimitive((Number)val));
        else if (val instanceof Boolean)
          ((JsonArray) list).set(idx, new JsonPrimitive((Boolean)val));
        else if (val instanceof String)
          ((JsonArray) list).set(idx, new JsonPrimitive((String)val));
      }
      else {
        for (int i = 0; i < idx; i++) {
          if ( i >= ((JsonArray)list).size())
            ((JsonArray)list).add((JsonObject)null);
        }
        if (val instanceof JsonElement)
          ((JsonArray) list).add((JsonElement)val);
        else if (val instanceof Character)
          ((JsonArray) list).add((Character)val);
        else if (val instanceof Number)
          ((JsonArray) list).add((Number)val);
        else if (val instanceof Boolean)
          ((JsonArray) list).add((Boolean)val);
        else if (val instanceof String)
          ((JsonArray) list).add((String)val);
      }
    }
    else {
      if (idx <= (((ArrayList)list).size() -1) ) 
        ((ArrayList) list).set(idx, val);
      else {
        for (int i = 0; i < idx; i++) {
          if ( i >= ((ArrayList)list).size())
            ((ArrayList)list).add(null);
        }
        ((ArrayList)list).add(val);
      }
    }
	}
	
	private static final void setValueInObj(Object obj, String key, Object valueToSet ) {
    if (obj instanceof Var) {
      obj = ((Var)obj).getObject();
    }
    
    if (valueToSet instanceof Var) {
      valueToSet = ((Var)valueToSet).getObject();
    }
    
    if (obj instanceof JsonObject) {
      if (valueToSet instanceof JsonElement)
        ((JsonObject) obj).add(key, (JsonElement)valueToSet);
      else if (valueToSet instanceof Character)
        ((JsonObject) obj).addProperty(key, (Character)valueToSet);
      else if (valueToSet instanceof Number)
        ((JsonObject) obj).addProperty(key, (Number)valueToSet);
      else if (valueToSet instanceof Boolean)
        ((JsonObject) obj).addProperty(key, (Boolean)valueToSet);
      else if (valueToSet instanceof String)
        ((JsonObject) obj).addProperty(key, (String)valueToSet);
    }
    else
      ((Map) obj).put(key, valueToSet);
  }
	
	private static final Object addEmptyDefaultValueByKey(Object obj, String key) {
	  Object value = null;
	  if (obj instanceof JsonObject) {
	    value = new JsonObject();
	    ((JsonObject) obj).add(key, (JsonObject)value);
	  }
    else {
      value = new HashMap<>();
      ((Map) obj).put(key, value);
    }
    return value;
	}
	
	private static Object addOrSetEmptyValueOnArray(Object obj, String keyOrPreviusIdx, int idx) {
	  Object value = getPreviousListFromArray(obj, keyOrPreviusIdx);
	  if (obj instanceof JsonElement) {
	    if (value == null || !(value instanceof JsonArray))
	      value = new JsonArray();
      setValueByIndex(value, new JsonObject(), idx);
	  }
	  else {
	    if (value == null || !(value instanceof ArrayList))
	      value = new ArrayList();
      setValueByIndex(value, new HashMap(), idx);
	  }
	  
	  if (obj instanceof JsonObject || obj instanceof Map) 
      setValueInObj(obj, keyOrPreviusIdx, value);
    else if (obj instanceof JsonArray || obj instanceof ArrayList) 
      setValueByIndex(obj, value, Integer.parseInt(keyOrPreviusIdx));
	  return value;
	}
	
	private static Object getPreviousListFromArray(Object obj, String keyOrPreviusIdx) {
	  try {
  	  if (obj instanceof JsonElement) {
  	    if (obj instanceof JsonObject) 
  	      return ((JsonObject)obj).get(keyOrPreviusIdx);
  	    else if (obj instanceof JsonArray) 
          return ((JsonArray)obj).get(Integer.parseInt(keyOrPreviusIdx));
  	  }
  	  else {
  	    if (obj instanceof Map) 
  	      return ((Map)obj).get(keyOrPreviusIdx);
  	    else if (obj instanceof ArrayList) 
          return ((ArrayList)obj).get(Integer.parseInt(keyOrPreviusIdx));
  	  }
	  }
	  catch (Exception e) {
	  }
	  return null;
	}
	
	private static Object addEmptyDefaultValuesByIndexes(Object obj, String key, List<String> indexes) {
	  Object value = obj;
	  for (int i = 0; i < indexes.size(); i++) {
	    String idx = indexes.get(i);
	    if (i == 0) 
	      value = addOrSetEmptyValueOnArray(value, key, Integer.parseInt(idx));
	    else
	      value = addOrSetEmptyValueOnArray(value, String.valueOf(indexes.get(i-1)), Integer.parseInt(idx));
	  }
	  return getValueByKey(obj, key);
	}
	
	private static Object createObjectPath(Object obj, String key, List<String> indexes) {
    Object value = null;
    if (indexes.size() == 0) 
      value = addEmptyDefaultValueByKey(obj, key);
    else 
      value = addEmptyDefaultValuesByIndexes(obj, key, indexes);
    return value;
  }
	
	public static final Object mapGetObjectPathExtractElement(Object obj, String key, boolean createIfNotExist) throws Exception {
    if (obj instanceof Var) 
      obj = ((Var)obj).getObject();
    
    List<String> indexes = new ArrayList<String>();
    key = fillIndexesIfExists(indexes, key);
    Object value = getValueByKey(obj, key);
    if ((value == null || value instanceof JsonNull) && createIfNotExist) 
      value = createObjectPath(obj, key, indexes);
    
    if (indexes.size() > 0) {
      for (String idx: indexes ) {
        Object o = value;
        if (value instanceof Var) 
          o = ((Var)value).getObject();
        value = getValueByIndex(o, Integer.parseInt(idx));
        if ((value == null || value instanceof JsonNull) && createIfNotExist) {
          createObjectPath(obj, key, indexes);
          value = getValueByIndex(o, Integer.parseInt(idx));
        }
      }
    }
    return value;
  }
  
  private static final void setValueInArray(Object obj, String key, Object valueToSet ) {
    if (obj instanceof Var) {
      obj = ((Var)obj).getObject();
    }
    
    if (valueToSet instanceof Var) {
      valueToSet = ((Var)valueToSet).getObject();
    }
    
    List<String> indexes = new ArrayList<String>();
    key = fillIndexesIfExists(indexes, key);
    Object value = getValueByKey(obj, key);
    
    if (indexes.size() > 0) {
      for (int i=0; i< indexes.size(); i++ ) {
        String idx = indexes.get(i);
        Object o = value;
        if (value instanceof Var) 
          o = ((Var)value).getObject();
        if (i == indexes.size() - 1)
          setValueByIndex(o, valueToSet, Integer.parseInt(idx));
        else
          value = getValueByIndex(o, Integer.parseInt(idx));
      }
    }
  }
  
  public static final void mapSetObject(Object obj, String key, Object valueToSet) throws Exception {
    if (key.endsWith("]")) {
      mapGetObjectPathExtractElement(obj, key, true);
      setValueInArray(obj, key, valueToSet);
    }
    else
      setValueInObj(obj, key, valueToSet);
  }

}
