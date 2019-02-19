package estruturas;

public class AssinaturaPassageiro {
    private String nomePassageiro;
    private String matriculaPassageiro;
    private String observacao;
    private Integer avaliacao;

    public AssinaturaPassageiro(String nomePassageiro, String matriculaPassageiro, String observacao, Integer avaliacao) {
        this.nomePassageiro = nomePassageiro;
        this.matriculaPassageiro = matriculaPassageiro;
        this.observacao = observacao;
        this.avaliacao = avaliacao;
    }

    public String getNomePassageiro() {
        return nomePassageiro;
    }

    public void setNomePassageiro(String nomePassageiro) {
        this.nomePassageiro = nomePassageiro;
    }

    public String getMatriculaPassageiro() {
        return matriculaPassageiro;
    }

    public void setMatriculaPassageiro(String matriculaPassageiro) {
        this.matriculaPassageiro = matriculaPassageiro;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Integer avaliacao) {
        this.avaliacao = avaliacao;
    }
}
