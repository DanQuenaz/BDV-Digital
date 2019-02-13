package estruturas;

import java.util.HashMap;

public final class Comunicator {
    private static HashMap<String, Object> stream;

    private Comunicator(){}

    public static HashMap<String, Object> getInstance(){
        if(stream == null){
            stream = new HashMap<String, Object>();
        }
        return stream;
    }

    public static void addObject(String index, Object obj){
        stream.put(index, obj);
    }

    public static void clear(){
        stream.clear();
    }

    public static Object getItem(String index){
        return stream.get(index);
    }
}