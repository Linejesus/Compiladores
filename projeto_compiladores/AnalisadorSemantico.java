public class AnalisadorSemantico {

    private Env top;
    private boolean temErro;

    public AnalisadorSemantico() {
        top = new Env(null);
        temErro = false;
    }

    public boolean analisar(Node raiz) {

        visitar(raiz);

        return !temErro;
    }

    private void visitar(Node node) {

        if(node == null) return;

        switch(node.nome) {

            case "bloco":
                entrarEscopo(node);
                return;

            case "cmdDeclara":
                verificarDeclaracao(node);
                break;

            case "cmdExpressao":
                verificarAtribuicao(node);
                break;

            case "fator":
                verificarUsoVariavel(node);
                break;

            case "condicao":
                verificarCondicao(node);
                break;

            case "cmdPara":
                verificarPara(node);
                return;

            case "cmdLeitura":
                verificarLeitura(node);
                break;
        }

        for(Node filho : node.nodes) {
            visitar(filho);
        }
    }

    private void entrarEscopo(Node bloco) {

        Env anterior = top;
        top = new Env(top);

        for(Node filho : bloco.nodes) {
            visitar(filho);
        }

        top = anterior;
    }

    private void verificarDeclaracao(Node node) {

        /*
            Estrutura esperada:

            cmdDeclara
             ├── inteiro
             ├── x
        */

        String tipo = node.nodes.get(0).nome;
        String nome = node.nodes.get(1).nome;

        if(existeNoEscopoAtual(nome)) {
            erro("Variável já declarada: " + nome);
            return;
        }

        top.put(nome, tipo);
    }


    private void verificarPara(Node node) {

        /*
            Estrutura esperada:

            cmdPara
            ├── para
            ├── (
            ├── tipo
            ├── cmdExpressao
            ├── condicao
            ├── .
            ├── cmdExpressaoFor
            ├── )
            ├── {
            ├── bloco
            └── }
        */

        Env anterior = top;
        top = new Env(top);

        String tipo = "";
        String nomeVar = "";

        // pega o tipo
        for(Node filho : node.nodes) {

            if(filho.nome.equals("tipo")) {

                tipo = filho.nodes.get(0).nome;
            }

            // primeira atribuição do for
            else if(filho.nome.equals("cmdExpressao")) {

                nomeVar = filho.nodes.get(0).nome;

                // declara variável do for
                top.put(nomeVar, tipo);

                break;
            }
        }

        // agora percorre normalmente
        for(Node filho : node.nodes) {
            visitar(filho);
        }

        top = anterior;
    }


    private void verificarCondicao(Node node) {

        /*
            condicao
            ├── expressao
            ├── op_relacional
            └── expressao
        */

        String tipoEsq = descobrirTipo(node.nodes.get(0));
        String tipoDir = descobrirTipo(node.nodes.get(2));

        if(!tipoEsq.equals(tipoDir)) {

            erro(
                "Tipos incompatíveis em condição: " +
                tipoEsq + " e " + tipoDir
            );
        }
    }


    private void verificarLeitura(Node node) {

        /*
            cmdLeitura
            ├── leia
            ├── (
            ├── id
            ├── )
            └── .
        */

        String nomeVar = node.nodes.get(2).nome;

        try {
            top.get(nomeVar);
        }
        catch(RuntimeException e) {
            erro(e.getMessage());
        }
    }


    private void verificarUsoVariavel(Node node) {

        /*
            fator
            ├── num
            ├── id
            ou
            ├── (
            ├── expressao
            └── )
        */

        if(node.nodes.size() == 0) {
            return;
        }

        String valor = node.nodes.get(0).nome;

        // número
        if(isNumero(valor)) {
            return;
        }

        // expressão entre parênteses
        if(valor.equals("(")) {
            return;
        }

        try {
            top.get(valor);
        }
        catch(RuntimeException e) {
            erro(e.getMessage());
        }
    }

    private void verificarAtribuicao(Node node) {

        /*
            cmdExpressao
             ├── x
             ├── =
             └── expressao
        */

        String nomeVar = node.nodes.get(0).nome;

        String tipoVar;

        try {
            tipoVar = top.get(nomeVar);
        }
        catch(RuntimeException e) {
            erro(e.getMessage());
            return;
        }

        Node expressao = node.nodes.get(2);

        String tipoExpr = descobrirTipo(expressao);

        if(!tipoVar.equals(tipoExpr)) {

            erro(
                "Tipos incompatíveis: variável '" +
                nomeVar +
                "' é " +
                tipoVar +
                " mas expressão é " +
                tipoExpr
            );
        }
    }

    private String descobrirTipo(Node node) {

        if(node == null) {
            return "desconhecido";
        }

        // fator
        if(node.nome.equals("fator")) {

            if(node.nodes.size() == 0) {
                return "desconhecido";
            }

            String valor = node.nodes.get(0).nome;

            // número
            if(isNumero(valor)) {

                if(valor.contains(",")) {
                    return "decimal";
                }

                return "inteiro";
            }

            // expressão entre parênteses
            if(valor.equals("(")) {

                for(Node filho : node.nodes) {

                    String tipoInterno = descobrirTipo(filho);

                    if(!tipoInterno.equals("desconhecido")) {
                        return tipoInterno;
                    }
                }
            }

            // variável
            try {
                return top.get(valor);
            }
            catch(Exception e) {
                return "desconhecido";
            }
        }

        String tipoEncontrado = null;

        for(Node filho : node.nodes) {

            String tipoFilho = descobrirTipo(filho);

            if(tipoFilho.equals("desconhecido")) {
                continue;
            }

            if(tipoEncontrado == null) {
                tipoEncontrado = tipoFilho;
            }

            // conflito de tipos
            else if(!tipoEncontrado.equals(tipoFilho)) {
                return "incorreta, pois possui tipos diferentes";
            }
        }

        if(tipoEncontrado == null) {
            return "desconhecido";
        }

        return tipoEncontrado;
    }

    private boolean existeNoEscopoAtual(String nome) {
        return top.existeAqui(nome);
    }

    private boolean isNumero(String s) {

        if(s == null || s.length() == 0) {
            return false;
        }

        boolean possuiVirgula = false;

        for(int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);

            // dígito
            if(Character.isDigit(c)) {
                continue;
            }

            // vírgula decimal
            if(c == ',') {

                // só pode existir uma
                if(possuiVirgula) {
                    return false;
                }

                possuiVirgula = true;
                continue;
            }

            // qualquer outro caractere invalida
            return false;
        }

        return true;
    }


   private void erro(String msg) {

        temErro = true;

        System.out.println("\nERRO SEMÂNTICO:");
        System.out.println(msg);
        System.out.println("_____________________________________________");
    }
}