import java.text.CharacterIterator;

public class Texto extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {

        if (code.current() == '"') {
            String texto = readTexto(code);

            if (texto != null && isTokenSeparator(code)) {
                return new Token("texto", texto);
            }
        }
        return null;
    }

    private String readTexto(CharacterIterator code) {
        String texto = "";

        texto += code.current();
        code.next();

        while (code.current() != '"' && code.current() != CharacterIterator.DONE) {
            texto += code.current();
            code.next();
        }

        if (code.current() == '"') {
            texto += code.current();
            code.next();
            return texto;
        }

        throw new RuntimeException("Erro: string não fechada");
    }
}