package estruturas;

import java.util.ArrayList;
import java.util.HashMap;

import interfaces.Localizacao;

public final class Trajeto {
    private static ArrayList<Coordenada> coordenadas;

    private Trajeto(){}

    public static ArrayList<Coordenada> inicializa(){
        if(coordenadas == null) coordenadas = new ArrayList<Coordenada>();

        return coordenadas;
    }

    public static void insereCoordenada(Coordenada cord){
        coordenadas.add(cord);
    }

    public static void clear(){
        if(coordenadas != null) {
            coordenadas.clear();
        }
    }

    public static HashMap<String, Double> getKmTrajeto(){
        if(coordenadas == null) return null;

        HashMap<String, Double> dist = new HashMap<String, Double>();

        Double total = new Double(0.0);
        Double cidade = new Double(0.0);
        Double rodovia = new Double(0.0);
        Double aux = new Double(0.0);


        for(int i = 0; i< coordenadas.size()-1; ++i){
            aux = Localizacao.calculaDistancia(coordenadas.get(i).getLatitude(),
                                              coordenadas.get(i).getLongitude(),
                                              coordenadas.get(i+1).getLatitude(),
                                              coordenadas.get(i+1).getLongitude()
                                              );
            total += aux;

            if(!coordenadas.get(i).getRodovia() && !coordenadas.get(i+1).getRodovia()){
                cidade += aux;
            }else if(!coordenadas.get(i).getRodovia() && coordenadas.get(i+1).getRodovia()){
                cidade += aux;
            }else{
                rodovia += aux;
            }

        }

        dist.put("TOTAL", total);
        dist.put("RODOVIA", rodovia);
        dist.put("CIDADE", cidade);

        return dist;
    }

}


