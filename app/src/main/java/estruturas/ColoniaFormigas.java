package estruturas;

public class ColoniaFormigas {

    private final int _alfa; //influência do fator feromonio
    private final int _beta; //influencia do fator distância
    private final int _Q; //contanste de atualização do feromonio
    private final double _sigma;
    private dados_matriz m[][];

    public ColoniaFormigas(){
        this._alfa = 1;
        this._beta = 1;
        this._Q = 10;
        this._sigma = 0.01;
    }
}

class dados_matriz{
    private double distancia;
    private double feromonio;
    private double prob;

    public dados_matriz(){;}

    public dados_matriz(double distancia, double feromonio, double prob) {
        this.distancia = distancia;
        this.feromonio = feromonio;
        this.prob = prob;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getFeromonio() {
        return feromonio;
    }

    public void setFeromonio(double feromonio) {
        this.feromonio = feromonio;
    }

    public double getProb() {
        return prob;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }
}