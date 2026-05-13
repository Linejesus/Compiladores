import java.util.HashMap;

public class Env {

    private HashMap<String, String> tabela;
    private Env anterior;

    public Env(Env anterior) {
        this.anterior = anterior;
        this.tabela = new HashMap<>();
    }

    public void put(String nome, String tipo) {

        tabela.put(nome, tipo);
    }

    public String get(String nome) {

        if(tabela.containsKey(nome)) {
            return tabela.get(nome);
        }

        if(anterior != null) {
            return anterior.get(nome);
        }

        throw new RuntimeException(
            "Variável não declarada: " + nome
        );
    }

    public boolean existeAqui(String nome) {
        return tabela.containsKey(nome);
    }
}