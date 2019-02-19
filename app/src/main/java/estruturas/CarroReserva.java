package estruturas;

public final class CarroReserva {
    private static Boolean reserva;
    private static String placaReserva;

    private CarroReserva(){}

    public static void setReserva(Boolean reserva) {
        CarroReserva.reserva = reserva;
    }

    public static Boolean getReserva(){
        return CarroReserva.reserva;
    }

    public static void changeReserva(){
        CarroReserva.reserva = !CarroReserva.reserva;
    }

    public static void setPlacaReserva(String placaReserva){
        CarroReserva.placaReserva = placaReserva;
    }

    public static String getPlacaReserva(){
        return CarroReserva.placaReserva;
    }
}
