package br.com.cronapi.mock;

import java.io.UnsupportedEncodingException;

public class JsonMock {

    public static String getJson() {
        return "{\n" +
                "   \"foo\" : \"foo\",\n" +
                "   \"bar\" : 10,\n" +
                "   \"baz\" : true\n" +
                "}";
    }

    public static String gerJsonToMap(){
        return "[".concat(getJson()).concat("]");
    }

    public static String getJsonToMapEncode() throws UnsupportedEncodingException {
        return java.util.Base64.getEncoder().encodeToString(((String) gerJsonToMap()).getBytes("UTF-8"));
    }

    public static String getJsonTempFile() {
        return "{\n" +
                "   \"type\" : \"tempFile\",\n" +
                "   \"path\" : \"/books.json\" \n" +
                "}";
    }

}
