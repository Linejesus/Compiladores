import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class GerandoCodigo {

    private StringBuilder codigo;
    private HashMap<String, String> tipos;

    public GerandoCodigo() {
        codigo = new StringBuilder();

        tipos = new HashMap<>();
    }

    public String gerar(Node raiz) {

        codigo.append("#include <stdio.h>\n\n");
        codigo.append("int main() {\n\n");

        visitar(raiz);

        codigo.append("\nreturn 0;\n");
        codigo.append("}\n");

        return codigo.toString();
    }

    private void visitar(Node node) {

        switch(node.nome) {

            case "cmdDeclara":
                gerarDeclaracao(node);
                return;

            case "cmdExpressao":
                gerarAtribuicao(node);
                return;

            case "cmdEscrita":
                gerarEscrita(node);
                return;

            case "cmdLeitura":
                gerarLeitura(node);
                return;

            case "cmdSe":
                gerarSe(node);
                return;

            case "cmdEnquanto":
                gerarEnquanto(node);
                return;

            case "cmdPara":
                gerarPara(node);
                return;

            case "cmdExecute":
                gerarExecute(node);
                return;
        }

        // somente percorre automaticamente
        // se NÃO foi tratado acima
        for(Node filho : node.nodes) {
            visitar(filho);
        }
    }

    private void gerarDeclaracao(Node node) {

        String tipo = node.nodes.get(0).nome;
        String nome = node.nodes.get(1).nome;

        // salva tipo original da linguagem
        tipos.put(nome, tipo);

        if(tipo.equals("inteiro")) {
            tipo = "int";
        }

        else if(tipo.equals("decimal")) {
            tipo = "float";
        }

        else if(tipo.equals("texto")) {
            codigo.append("char " + nome + "[100];\n");
            return;
        }

        codigo.append(tipo + " " + nome + ";\n");
    }
    

    private void gerarAtribuicao(Node node) {

        String var = node.nodes.get(0).nome;

        codigo.append(var + " = ");

        gerarExpressao(node.nodes.get(2));

        codigo.append(";\n");
    }

    private void gerarExpressao(Node node) {

        for(Node filho : node.nodes) {

            switch(filho.nome) {

                case "expressao":
                case "termo":
                case "termoLinha":
                case "expressaoLinha":
                case "fator":

                    gerarExpressao(filho);
                    break;

                default:
                    String valor = filho.nome;

                    if(isNumeroDecimal(valor)) {
                        valor = trocarVirgulaPorPonto(valor);
                    }

                    codigo.append(valor + "");
            }
        }
    }

    private boolean isNumeroDecimal(String s) {

        boolean encontrouVirgula = false;

        for(int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);

            if(c == ',') {

                if(encontrouVirgula) {
                    return false;
                }

                encontrouVirgula = true;
            }

            else if(!Character.isDigit(c)) {
                return false;
            }
        }

        return encontrouVirgula;
    }


    private String trocarVirgulaPorPonto(String s) {

        String resultado = "";

        for(int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);

            if(c == ',') {
                resultado += '.';
            }

            else {
                resultado += c;
            }
        }

        return resultado;
    }

    private void gerarEscrita(Node node) {

        Node conteudo = node.nodes.get(2);

        String valor = extrairValor(conteudo);

        // TEXTO
        if(valor.startsWith("\"") && valor.endsWith("\"")) {

            codigo.append("printf(" + valor + ");\n");
            return;
        }

        // NÚMERO
        if(isNumero(valor)) {

            if(valor.contains(",")) {

                valor = trocarVirgulaPorPonto(valor);

                codigo.append("printf(\"%f\", " + valor + ");\n");
            }

            else {

                codigo.append("printf(\"%d\", " + valor + ");\n");
            }

            return;
        }

        // VARIÁVEL
        String tipo = tipos.get(valor);

        if(tipo != null) {

            if(tipo.equals("inteiro")) {

                codigo.append("printf(\"%d\", " + valor + ");\n");
            }

            else if(tipo.equals("decimal")) {

                codigo.append("printf(\"%f\", " + valor + ");\n");
            }

            else if(tipo.equals("texto")) {

                codigo.append("printf(\"%s\", " + valor + ");\n");
            }

            return;
        }

        // fallback
        codigo.append("printf(" + valor + ");\n");
    }



    private boolean isNumero(String s) {

        if(s == null || s.length() == 0) {
            return false;
        }

        boolean possuiVirgula = false;

        for(int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);

            if(Character.isDigit(c)) {
                continue;
            }

            if(c == ',') {

                if(possuiVirgula) {
                    return false;
                }

                possuiVirgula = true;
                continue;
            }

            return false;
        }

        return true;
    }


    private String extrairValor(Node node) {

        if(node == null) {
            return "";
        }

        // nó folha
        if(node.nodes.size() == 0) {
            return node.nome;
        }

        // percorre filhos até encontrar valor real
        for(Node filho : node.nodes) {

            String valor = extrairValor(filho);

            if(valor != null && !valor.equals("")) {
                return valor;
            }
        }

        return "";
    }


    //scanf("%d", &idade); 
    private void gerarLeitura(Node node) {

        String valor = node.nodes.get(2).nome;

        String tipo = tipos.get(valor);

        // variável não encontrada
        if(tipo == null) {

            codigo.append("// erro: variável não encontrada\n");
            return;
        }

        // inteiro
        if(tipo.equals("inteiro")) {

            codigo.append("scanf(\"%d\", &" + valor + ");\n");
        }

        // decimal
        else if(tipo.equals("decimal")) {

            codigo.append("scanf(\"%f\", &" + valor + ");\n");
        }

        // texto
        else if(tipo.equals("texto")) {

            codigo.append("scanf(\"%s\", " + valor + ");\n");
        }
    }


    private void gerarSe(Node node) {

        codigo.append("if(");

        // procura a condição
        for(Node filho : node.nodes) {

            if(filho.nome.equals("condicao")) {
                gerarCondicao(filho);
                break;
            }
        }

        codigo.append(") {\n");

        // procura o bloco principal
        for(Node filho : node.nodes) {

            if(filho.nome.equals("bloco")) {
                visitar(filho);
                break;
            }
        }

        codigo.append("}\n");

        // senao / senaose
        for(Node filho : node.nodes) {

            if(filho.nome.equals("senaoParte")) {
                gerarSenao(filho);
            }
        }
    }


    private void gerarCondicao(Node node) {

        for(Node filho : node.nodes) {

            switch(filho.nome) {

                case "expressao":
                case "termo":
                case "termoLinha":
                case "expressaoLinha":
                case "fator":
                case "condicao":

                    gerarCondicao(filho);
                    break;

                default:

                    codigo.append(filho.nome + "");
            }
        }
    }


    private void gerarSenao(Node node) {

        if(node.nodes.isEmpty()) {
            return;
        }

        Node primeiro = node.nodes.get(0);

        // else
        if(primeiro.nome.equals("senao")) {

            codigo.append("else {\n");

            for(Node filho : node.nodes) {

                if(filho.nome.equals("bloco")) {
                    visitar(filho);
                }
            }

            codigo.append("}\n");
        }

        // else if
        else if(primeiro.nome.equals("senaose")) {

            codigo.append("else if(");

            for(Node filho : node.nodes) {

                if(filho.nome.equals("condicao")) {
                    gerarCondicao(filho);
                }
            }

            codigo.append(") {\n");

            for(Node filho : node.nodes) {

                if(filho.nome.equals("bloco")) {
                    visitar(filho);
                }
            }

            codigo.append("}\n");

            // encadeamento
            for(Node filho : node.nodes) {

                if(filho.nome.equals("senaoParte")) {
                    gerarSenao(filho);
                }
            }
        }
    }


    private void gerarPara(Node node) {

        codigo.append("for(");

        boolean primeiraParte = true;

        for(Node filho : node.nodes) {

            // declaração
            if(filho.nome.equals("tipo")) {

                gerarTipoPara(filho);

                primeiraParte = false;
            }

            // inicialização
            else if(filho.nome.equals("cmdExpressao")) {

                gerarAtribuicaoPara(filho);

                codigo.append("; ");
            }

            // condição
            else if(filho.nome.equals("condicao")) {

                gerarCondicao(filho);

                codigo.append("; ");
            }

            // incremento
            else if(filho.nome.equals("cmdExpressaoPara")) {

                gerarAtribuicaoPara(filho);
            }
        }

        codigo.append(") {\n");

        // bloco do for
        for(Node filho : node.nodes) {

            if(filho.nome.equals("bloco")) {
                visitar(filho);
            }
        }

        codigo.append("}\n");
    }


    private void gerarTipoPara(Node node) {

        for(Node filho : node.nodes) {

            switch(filho.nome) {

                case "inteiro":
                case "reservada_inteiro":
                    codigo.append("int ");
                    break;

                case "decimal":
                case "reservada_decimal":
                    codigo.append("float ");
                    break;

                case "texto":
                case "reservada_texto":
                    codigo.append("char ");
                    break;
            }
        }
    }


    private void gerarAtribuicaoPara(Node node) {

        String variavel = node.nodes.get(0).nome;

        codigo.append(variavel + " = ");

        gerarExpressao(node.nodes.get(2));
    }


    private void gerarEnquanto(Node node) {

        codigo.append("while(");

        // condição
        for(Node filho : node.nodes) {

            if(filho.nome.equals("condicao")) {
                gerarCondicao(filho);
                break;
            }
        }

        codigo.append(") {\n");

        // bloco
        for(Node filho : node.nodes) {

            if(filho.nome.equals("bloco")) {
                visitar(filho);
                break;
            }
        }

        codigo.append("}\n");
    }


    private void gerarExecute(Node node) {

        codigo.append("do {\n");

        // bloco
        for(Node filho : node.nodes) {

            if(filho.nome.equals("bloco")) {
                visitar(filho);
                break;
            }
        }

        codigo.append("} while (");

        // condição
        for(Node filho : node.nodes) {

            if(filho.nome.equals("condicao")) {
                gerarCondicao(filho);
                break;
            }
        }

        codigo.append(");\n");      
    }
    

    public void salvar(String arquivo) throws IOException {

        FileWriter writer = new FileWriter(arquivo);

        writer.write(codigo.toString());

        writer.close();
    }
}