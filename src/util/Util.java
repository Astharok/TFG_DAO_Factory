package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
    
    public static String toJson(Object json) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        String resp = gson.toJson(json);
        return resp;
    }
    
    public static <J> String toJson(List<J> json) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        String resp = gson.toJson(json);
        return resp;
    }
    
    public static <J> String toJson(Map<J,J> json) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        String resp = gson.toJson(json);
        return resp;
    }
    
    public static Map<String, String> fromJson(String json) {
        
        Map<String, String> result = new HashMap<>();

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        
        try{
            result = gson.fromJson(json, HashMap.class);
        }catch(JsonSyntaxException ex){
            System.err.println("EXCEPTION");
            System.err.println("Method: fromJson");
            System.err.println(ex.getMessage());
            System.err.println(json);
        }
        
        return result;
    }

    public static void showResults(Map<String, String> results) {
        if(results.get("STATE").equals("SUCCESS")){
            for(String key: results.keySet()){
                System.out.println(key + ": " + results.get(key));
            }
        }
        if(results.get("STATE").equals("FAILURE")){
            for(String key: results.keySet()){
                System.err.println(key + ": " + results.get(key));
            }
        }
        if(results.get("STATE").equals("EXCEPTION")){
            for(String key: results.keySet()){
                System.err.println(key + ": " + results.get(key));
            }
        }
    }
    
}
