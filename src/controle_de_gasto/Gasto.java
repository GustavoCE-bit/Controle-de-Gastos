package controle_de_gasto;

/**
 *
 * @author gusta
 */
class Gasto {
    private int id;
    private String descricao;
    private int diaID;
    private int dia;
    private String mes;
    private double valor;
    private boolean abatimento;
    private double abatimento_val;
    private boolean juros;
    private double juros_val;
    private double valor_total;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getDia() {
        return dia;
    }
    public void setDia(int dia) {
        this.dia = dia;
    }

    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }

    public boolean isAbatimento() {
        return abatimento;
    }
    public void setAbatimento(boolean abatimento) {
        this.abatimento = abatimento;
    }

    public double getAbatimento_val() {
        return abatimento_val;
    }
    public void setAbatimento_val(double abatimento_val) {
        this.abatimento_val = abatimento_val;
    }

    public boolean isJuros() {
        return juros;
    }
    public void setJuros(boolean juros) {
        this.juros = juros;
    }

    public double getJuros_val() {
        return juros_val;
    }
    public void setJuros_val(double juros_val) {
        this.juros_val = juros_val;
    }
    
    public double getValor_total() {
        return valor_total;
    }
    public void setValor_total(double valor_total) {
        this.valor_total = valor_total;
    }

    public String getMes() {
        return mes;
    }
    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getDiaID() {
        return diaID;
    }
    public void setDiaID(int diaID) {
        this.diaID = diaID;
    }
}