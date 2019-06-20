package ru.otus.kchu.jsonWriter;

import javax.json.*;
import java.lang.reflect.Field;
import java.util.*;

public class MyJsonWriter {

    public static String toJson(Object obj) throws IllegalAccessException {
        JsonObject jsonObject = null;
        JsonObjectBuilder builder =null;

        if(obj == null) return null;
        final Class  clazz = obj.getClass();

        if(obj instanceof Number)
            return  obj.toString();
        if(obj instanceof Character ||obj instanceof String)
            return  Json.createValue(String.valueOf(obj)).toString();
        if(obj  instanceof Collection)
            return Json.createArrayBuilder((Collection)obj).build().toString();

        if(clazz.isArray() ){
            Class arrayComponentType = clazz.getComponentType();
            JsonArrayBuilder arrayBuilder = buildArray(arrayComponentType.getSimpleName(),obj);
            return arrayBuilder.build().toString();
        }


        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());

        try {

            builder = fieldsToBuilder(fields, obj);
            jsonObject = builder.build();

        } catch (Exception e) {
            System.out.println("Exception!:" + e.getMessage());
            return null;
        }
        System.out.println();
        System.out.println("builder:" + jsonObject);
        return jsonObject.toString();

    }

    private static JsonObjectBuilder fieldsToBuilder( List<Field> fields, Object obj ) throws IllegalAccessException {
        JsonObjectBuilder builder =   Json.createObjectBuilder();

        for (Field field : fields) {
            System.out.println("-------");
            Class<?> fieldType = field.getType();

            field.setAccessible(true);
            System.out.println("field name : " + field.getName());
            System.out.print("field type : " + fieldType.getName());
            System.out.println(" (primitive:"+fieldType.isPrimitive() + (fieldType.isArray()?" ;ARRAY":"" ) +( field.get(obj) instanceof Collection?";Collection":"")+"); ") ;

          if(fieldType.isArray() ){

                Class arrayComponentType = fieldType.getComponentType();
                System.out.println("arrayComponentType :"+arrayComponentType + " (primitive:"+arrayComponentType.isPrimitive()+")" );
            //JsonArrayBuilder arrayBuilder =  Json.createArrayBuilder( Arrays.asList(( Object[])field.get(obj)));

                JsonArrayBuilder arrayBuilder = buildArray(arrayComponentType.getSimpleName(),field.get(obj));

                builder.add(field.getName(),arrayBuilder);
                System.out.println();
            }else {
                addFieldByType(builder,field.getName(),field.get(obj));
              System.out.println("Value: "+field.get(obj));
            }

            field.setAccessible(false);

        }
        return builder;
    }

    private static JsonArrayBuilder buildArray(String typeSimpleName, Object obj ){
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        switch (typeSimpleName) {
            case "int":
                int[] tmpInArr  =  ((int[]) obj);
                Arrays.stream(tmpInArr).forEach(o -> arrayBuilder.add(o));
                break;
            case "Integer":
                Integer[] tmpIngArr  =  ((Integer[]) obj);
                Arrays.stream(tmpIngArr).forEach(o -> arrayBuilder.add(o));
                break;
            case "long":
                long[] tmpLnArr  =  (long[])obj;
                Arrays.stream(tmpLnArr).forEach(o -> arrayBuilder.add(o));
                break;
            case "boolean":
                boolean[] tmpBArr  =  (boolean[])obj;
                for (boolean b : tmpBArr) { arrayBuilder.add(b);}
                break;
            case "String":
                String[] tmpSArr  =  (String[])obj;
                Arrays.stream(tmpSArr).forEach(o -> arrayBuilder.add(o) );
                break;
            case "double":
                double[] tmpDbArr  =  (double[])obj;
                Arrays.stream(tmpDbArr).forEach(o -> arrayBuilder.add(o) );
                break;
            default: Object[] tmpArr = (Object[])obj;
                List<?> tmpList = Arrays.asList(tmpArr);
                tmpList.forEach(o -> {
                    List<Field> fieldsTmp = Arrays.asList(o.getClass().getDeclaredFields());
                    try {
                        JsonObjectBuilder builderTmp = fieldsToBuilder(fieldsTmp,o);
                        arrayBuilder.add(builderTmp);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
        }

    return arrayBuilder;
    }

    private static void addFieldByType(JsonObjectBuilder objectBuilder, String fName, Object obj){

        if(obj instanceof Integer)
            objectBuilder.add(fName,(Integer) obj);
        else if(obj instanceof Long)
            objectBuilder.add(fName,(long) obj);
        else if(obj instanceof String)
            objectBuilder.add(fName,(String) obj);
        else if (obj instanceof Float)
            objectBuilder.add(fName, (Float)obj);
        else if (obj instanceof Double)
            objectBuilder.add(fName, (Double)obj);
        else if (obj instanceof Boolean)
            objectBuilder.add(fName, (Boolean)obj);
        else if(obj  instanceof Collection)
            objectBuilder.add(fName,Json.createArrayBuilder((Collection)obj));
        else if(obj  instanceof Map)
            objectBuilder.add(fName, Json.createObjectBuilder((Map) obj));
        }

}
