package estruturas;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public final class ListaCoordenadas {
    private static ArrayList<Coordenada> coordenadas;

    private ListaCoordenadas(){}

    public static ArrayList<Coordenada> inicializa(){
        if(coordenadas == null) coordenadas = new ArrayList<Coordenada>();

        return coordenadas;
    }

    public static void insereCoordenada(Coordenada cord){
        coordenadas.add(cord);
    }

    public static void clear(){
        coordenadas.clear();
    }



}


