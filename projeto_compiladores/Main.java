import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
     
        // caminho do arquivo fonte
        String caminho = "arquivo.txt";

        // lê todo o conteúdo do arquivo
        String data = Files.readString(Paths.get(caminho));

        // =========================
        // ANÁLISE LÉXICA
        // =========================

        Lexer lexer = new Lexer(data);

        List<Token> tokens = lexer.getTokens();

        System.out.println("----------------------------------");
        System.out.println("TOKENS:");

        for(Token token : tokens) {
            System.out.println(token);
        }

        System.out.println("\nLexicamente correto!");
        System.out.println("----------------------------------\n");



        // =========================
        // ANÁLISE SINTÁTICA
        // =========================

        Parser parser = new Parser(tokens);

        Node raiz = parser.main();

        if(raiz == null) {
            return;
        }

        System.out.println("_____________________________________________");



        // =========================
        // ANÁLISE SEMÂNTICA
        // =========================

        AnalisadorSemantico semantico = new AnalisadorSemantico();

        boolean semanticoOk = semantico.analisar(raiz);

        if(!semanticoOk) {
            return;
        }

        System.out.println("\nSemanticamente correto!");
        System.out.println("_____________________________________________");



        // =========================
        // GERAÇÃO DE CÓDIGO
        // =========================

        GerandoCodigo generator = new GerandoCodigo();

        generator.gerar(raiz);

        try {

            generator.salvar("saida.c");

            System.out.println("\nCódigo C gerado!\n");

        } catch(Exception e) {

            System.out.println("\nErro ao salvar arquivo C\n");
        }
    }
}