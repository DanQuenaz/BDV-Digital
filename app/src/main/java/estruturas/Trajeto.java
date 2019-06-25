package estruturas;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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
        Double velocidade = new Double(0.0);
        Double aux = new Double(0.0);

        if(coordenadas.size() > 1) {
            for (int i = 0; i < coordenadas.size() - 1; ++i) {
                aux = Localizacao.calculaDistancia(coordenadas.get(i).getLatitude(),
                        coordenadas.get(i).getLongitude(),
                        coordenadas.get(i + 1).getLatitude(),
                        coordenadas.get(i + 1).getLongitude()
                );
                total += aux;

                if (!coordenadas.get(i).getRodovia() && !coordenadas.get(i + 1).getRodovia()) {
                    cidade += aux;
                } else if (!coordenadas.get(i).getRodovia() && coordenadas.get(i + 1).getRodovia()) {
                    cidade += aux;
                } else {
                    rodovia += aux;
                }

            }

            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date data1 = null;
            Date data2 = null;
            try {
                data1 = format2.parse(coordenadas.get(0).getHora());
                data2 = format2.parse(coordenadas.get(coordenadas.size() - 1).getHora());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long diff = data2.getTime() - data1.getTime();
            long diffSeconds = diff / 1000;

            Log.i("TEMPO", "" + diffSeconds);

            velocidade = total * 3.6 / diffSeconds;

            Log.i("VELOCIDADE", "" + velocidade);
        }

        dist.put("TOTAL", total/1000.0);
        dist.put("RODOVIA", rodovia/1000.0);
        dist.put("CIDADE", cidade/1000.0);
        dist.put("VELOCIDADE", velocidade);

        return dist;
    }

}


