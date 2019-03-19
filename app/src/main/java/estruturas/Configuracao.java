package estruturas;

public final class Configuracao {
    private static Boolean rodovia;
    private static Boolean reserva;
    private static String placaReserva;

    private Configuracao(){}

    public static Boolean getRodovia() {
        return rodovia;
    }

    public static void setRodovia(Boolean rodovia) {
        Configuracao.rodovia = rodovia;
    }

    public static void setReserva(Boolean reserva) {
        Configuracao.reserva = reserva;
    }

    public static Boolean getReserva(){
        return Configuracao.reserva;
    }

    public static void changeReserva(){
        Configuracao.reserva = !Configuracao.reserva;
    }

    public static void setPlacaReserva(String placaReserva){
        Configuracao.placaReserva = placaReserva;
    }

    public static String getPlacaReserva(){
        return Configuracao.placaReserva;
    }
}
