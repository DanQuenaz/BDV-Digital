package estruturas;

import java.util.ArrayList;

public final class AssinaturasBDV {
    private static ArrayList<AssinaturaPassageiro> assinaturas;

    private AssinaturasBDV(){}

    public static ArrayList<AssinaturaPassageiro> getInstance(){
        if(assinaturas == null){
            assinaturas = new ArrayList<AssinaturaPassageiro>();
        }
        return assinaturas;
    }

    public static void addAssinatura(AssinaturaPassageiro a){
        assinaturas.add(a);
    }

    public static Boolean isVazio(){
        return assinaturas.isEmpty();
    }

    public static Integer getTamanho(){
        return assinaturas.size();
    }

    public static void clear(){
        if(assinaturas != null){
            assinaturas.clear();
        }
    }

}
