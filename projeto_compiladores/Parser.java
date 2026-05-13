import java.util.List;

public class Parser{
    List<Token> tokens;
    Token token;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public Token getNextToken(){
        if(tokens.size() > 0)
            return tokens.remove(0);
        return null;
    }

    private void erro(String regra){
        System.out.println("Regra: " + regra);
        System.out.println("token inválido: " + token.lexema);
        System.out.println("----------------------------------");
    }


    public boolean iniciarPrograma(Node pai){

        Node programa = pai.addNode("iniciarPrograma");

        if(match("reservada_programa", programa)) {

            if(bloco(programa)) {

                if(match("reservada_fimprograma", programa)) {
                    return true;
                }
            }
        }

        erro("iniciarPrograma");
        return false;
    }


    public boolean bloco(Node pai) {

        Node bloco = pai.addNode("bloco");

        if(cmd(bloco)) {

            if(blocoLinha(bloco)) {

                return true;
            }
        }

        erro("bloco");
        return false;
    }


    public boolean blocoLinha(Node pai) {

        Node blocoLinha = pai.addNode("blocoLinha");

        if(inicioCmd()) {

            if(cmd(blocoLinha)) {

                return blocoLinha(blocoLinha);
            }

            erro("blocoLinha");
            return false;
        }

        // ε (vazio)
        return true;
    }


    private boolean inicioCmd() {
        return token.tipo == "reservada_leia" ||
           token.tipo == "reservada_escreva" ||
           token.tipo == "id" ||
           token.tipo == "reservada_se" ||
           token.tipo == "reservada_enquanto" ||
           token.tipo == "reservada_para" ||
           token.tipo == "reservada_execute" ||
           token.tipo == "reservada_inteiro" ||
           token.tipo == "reservada_decimal" ||
           token.tipo == "reservada_texto";
    }


    public boolean cmd(Node pai) {

        Node cmd = pai.addNode("cmd");

        if(token.tipo.equals("reservada_inteiro") ||
        token.tipo.equals("reservada_decimal") ||
        token.tipo.equals("reservada_texto")) {

            return cmdDeclara(cmd);
        }

        else if(token.tipo.equals("reservada_leia")) {
            return cmdLeitura(cmd);
        }

        else if(token.tipo.equals("reservada_escreva")) {
            return cmdEscrita(cmd);
        }

        else if(token.tipo.equals("id")) {
            return cmdExpressao(cmd);
        }

        else if(token.tipo.equals("reservada_se")) {
            return cmdSe(cmd);
        }

        else if(token.tipo.equals("reservada_para") ||
                token.tipo.equals("reservada_enquanto") ||
                token.tipo.equals("reservada_execute")) {

            return cmdLoop(cmd);
        }

        erro("cmd");
        return false;
    }


    public boolean cmdEscrita(Node pai){

        Node escrita = pai.addNode("cmdEscrita");

        if(match("reservada_escreva", escrita)) {

            if(match("abre_parenteses", escrita)) {

                if(conteudo(escrita)) {

                    if(match("fecha_parenteses", escrita)) {

                        if(match("fim_linha", escrita)) {
                            return true;
                        }
                    }
                }
            }
        }

        erro("cmdEscrita");
        return false;
    }



    public boolean conteudo(Node pai){

        Node conteudo = pai.addNode("conteudo");

        if(token.tipo.equals("texto")) {
            if(match("texto", conteudo)) {
                return true;
            }
        }

        else if(token.tipo.equals("id") ||
                token.tipo.equals("num") ||
                token.tipo.equals("abre_parenteses")) {

            if(expressao(conteudo)) {
                return true;
            }
        }

        erro("conteudo");
        return false;
    }



    public boolean senaoParte(Node pai){

        Node senaoParte = pai.addNode("senaoParte");

        if(token.tipo.equals("reservada_senao")) {

            if(match("reservada_senao", senaoParte)) {

                if(match("abre_chaves", senaoParte)) {

                    if(bloco(senaoParte)) {

                        if(match("fecha_chaves", senaoParte)) {
                            return true;
                        }
                    }
                }
            }

            erro("senaoParte");
            return false;
        }

        else if(token.tipo.equals("reservada_senaose")) {

            if(match("reservada_senaose", senaoParte)) {

                if(match("abre_parenteses", senaoParte)) {

                    if(condicao(senaoParte)) {

                        if(match("fecha_parenteses", senaoParte)) {

                            if(match("abre_chaves", senaoParte)) {

                                if(bloco(senaoParte)) {

                                    if(match("fecha_chaves", senaoParte)) {

                                        return senaoParte(senaoParte);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            erro("senaoParte");
            return false;
        }

        // ε (vazio)
        return true;
    }



    public boolean cmdSe(Node pai){

        Node cmdSe = pai.addNode("cmdSe");

        if(match("reservada_se", cmdSe)) {

            if(match("abre_parenteses", cmdSe)) {

                if(condicao(cmdSe)) {

                    if(match("fecha_parenteses", cmdSe)) {

                        if(match("abre_chaves", cmdSe)) {

                            if(bloco(cmdSe)) {

                                if(match("fecha_chaves", cmdSe)) {

                                    return senaoParte(cmdSe);
                                }
                            }
                        }
                    }
                }
            }
        }

        erro("cmdSe");
        return false;
    }



    public boolean cmdLoop(Node pai){

        Node loop = pai.addNode("cmdLoop");

        if(token.tipo.equals("reservada_para")) {
            return cmdPara(loop);
        }

        else if(token.tipo.equals("reservada_enquanto")) {
            return cmdEnquanto(loop);
        }

        else if(token.tipo.equals("reservada_execute")) {
            return cmdExecute(loop);
        }

        erro("cmdLoop");
        return false;
    }



    public boolean cmdPara(Node pai){

        Node para = pai.addNode("cmdPara");

        if(match("reservada_para", para)) {

            if(match("abre_parenteses", para)) {

                if(tipo(para)) {

                    if(cmdExpressao(para)) {

                        if(condicao(para)) {

                            if(match("fim_linha", para)) {

                                if(cmdExpressaoPara(para)) {

                                    if(match("fecha_parenteses", para)) {

                                        if(match("abre_chaves", para)) {

                                            if(bloco(para)) {

                                                if(match("fecha_chaves", para)) {
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        erro("cmdPara");
        return false;
    }


    public boolean cmdExpressaoPara(Node pai){

        Node cmdExpressaoPara = pai.addNode("cmdExpressaoPara");

        if(match("id", cmdExpressaoPara)) {

            if(match("op_atribuicao", cmdExpressaoPara)) {

                if(expressao(cmdExpressaoPara)) {
                    return true;
                }
            }
        }

        erro("cmdExpressaoPara");
        return false;
    }


    public boolean cmdEnquanto(Node pai){

        Node enquanto = pai.addNode("cmdEnquanto");

        if(match("reservada_enquanto", enquanto)) {

            if(match("abre_parenteses", enquanto)) {

                if(condicao(enquanto)) {

                    if(match("fecha_parenteses", enquanto)) {

                        if(match("abre_chaves", enquanto)) {

                            if(bloco(enquanto)) {

                                if(match("fecha_chaves", enquanto)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        erro("cmdEnquanto");
        return false;
    }


    public boolean cmdExecute(Node pai){

        Node execute = pai.addNode("cmdExecute");

        if(match("reservada_execute", execute)) {

            if(match("abre_chaves", execute)) {

                if(bloco(execute)) {

                    if(match("fecha_chaves", execute)) {

                        if(match("reservada_enquanto", execute)) {

                            if(match("abre_parenteses", execute)) {

                                if(condicao(execute)) {

                                    if(match("fecha_parenteses", execute)) {

                                        if(match("fim_linha", execute)) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        erro("cmdExecute");
        return false;
    }

    
    public boolean cmdExpressao(Node pai){

        Node cmdExpressao = pai.addNode("cmdExpressao");

        if(match("id", cmdExpressao)) {

            if(match("op_atribuicao", cmdExpressao)) {

                if(expressao(cmdExpressao)) {

                    if(match("fim_linha", cmdExpressao)) {
                        return true;
                    }
                }
            }
        }

        erro("cmdExpressao");
        return false;
    }


    public boolean cmdLeitura(Node pai){

        Node leitura = pai.addNode("cmdLeitura");

        if(match("reservada_leia", leitura)) {

            if(match("abre_parenteses", leitura)) {

                if(token.tipo.equals("id")) {

                    if(match("id", leitura)) {

                        if(match("fecha_parenteses", leitura)) {

                            if(match("fim_linha", leitura)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        erro("cmdLeitura");
        return false;
    }



    public boolean cmdDeclara(Node pai){

        Node declara = pai.addNode("cmdDeclara");

        String tipoVar = "";

        if(token.tipo.equals("reservada_inteiro")) {
            tipoVar = "inteiro";
        }
        else if(token.tipo.equals("reservada_decimal")) {
            tipoVar = "decimal";
        }
        else if(token.tipo.equals("reservada_texto")) {
            tipoVar = "texto";
        }
        else {
            erro("tipo");
            return false;
        }

        declara.addNode(token.lexema);

        token = getNextToken();

        if(token.tipo.equals("id")) {

            String nomeVar = token.lexema;

            declara.addNode(nomeVar);

            token = getNextToken();

            if(match("fim_linha", declara)) {
                return true;
            }
        }

        erro("cmdDeclara");
        return false;
    }


    public boolean tipo(Node pai){

        Node tipo = pai.addNode("tipo");

        if(match("reservada_inteiro", tipo)) {
            return true;
        }

        else if(match("reservada_decimal", tipo)) {
            return true;
        }

        else if(match("reservada_texto", tipo)) {
            return true;
        }

        erro("tipo");
        return false;
    }



    public boolean condicao(Node pai){

        Node condicao = pai.addNode("condicao");

        if(expressao(condicao)) {

            if(match("op_relacional", condicao)) {

                if(expressao(condicao)) {
                    return true;
                }
            }
        }

        erro("condicao");
        return false;
    }



    public boolean expressao(Node pai){

        Node expressao = pai.addNode("expressao");

        if(termo(expressao)) {

            if(expressaoLinha(expressao)) {
                return true;
            }
        }

        erro("expressao");
        return false;
    }


    public boolean expressaoLinha(Node pai){

        Node expressaoLinha = pai.addNode("expressaoLinha");

        if(token.tipo.equals("adicao")) {

            if(match("adicao", expressaoLinha)) {

                if(termo(expressaoLinha)) {

                    if(expressaoLinha(expressaoLinha)) {
                        return true;
                    }
                }
            }

            erro("expressaoLinha");
            return false;
        }

        else if(token.tipo.equals("subtracao")) {

            if(match("subtracao", expressaoLinha)) {

                if(termo(expressaoLinha)) {

                    if(expressaoLinha(expressaoLinha)) {
                        return true;
                    }
                }
            }

            erro("expressaoLinha");
            return false;
        }

        // ε (vazio)
        return true;
    }


    public boolean termo(Node pai){

        Node termo = pai.addNode("termo");

        if(fator(termo)) {

            if(termoLinha(termo)) {
                return true;
            }
        }

        erro("termo");
        return false;
    }

    public boolean termoLinha(Node pai){

        Node termoLinha = pai.addNode("termoLinha");

        if(token.tipo.equals("multiplicacao")) {

            if(match("multiplicacao", termoLinha)) {

                if(fator(termoLinha)) {

                    if(termoLinha(termoLinha)) {
                        return true;
                    }
                }
            }

            erro("termoLinha");
            return false;
        }

        else if(token.tipo.equals("divisao")) {

            if(match("divisao", termoLinha)) {

                if(fator(termoLinha)) {

                    if(termoLinha(termoLinha)) {
                        return true;
                    }
                }
            }

            erro("termoLinha");
            return false;
        }

        // ε (vazio)
        return true;
    }


    public boolean fator(Node pai){

        Node fator = pai.addNode("fator");

        if(token.tipo.equals("num")) {
            if(match("num", fator)) {
                return true;
            }
        }

        if(token.tipo.equals("id")) {
            if(match("id", fator)) {
                return true;
            }
        }

        if(token.tipo.equals("abre_parenteses")) {
            if(match("abre_parenteses", fator)) {
                if(expressao(fator)) {
                    if(match("fecha_parenteses", fator)) {
                        return true;
                    }
                }
            }
        }

        erro("fator");
        return false;
    }


    public boolean match(String tipo, Node pai) {
        if(token.tipo.equals(tipo)) {

            pai.addNode(token.lexema);

            token = getNextToken();
            return true;
        }

        erro(tipo);
        return false;
    }



    public Node main(){

        Node raiz = new Node("programa");

        token = getNextToken();

        if(iniciarPrograma(raiz)) {

            Tree tree = new Tree(raiz);

            tree.printTree();

            System.out.println("Sintaticamente correto!");

            return raiz;
        }

        System.out.println("Parser error!");

        return null;
    }
}