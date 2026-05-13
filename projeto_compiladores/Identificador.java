import java.text.CharacterIterator;

public class Identificador extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        // Primeiro caractere tem que ser uma letra
        if (Character.isLetter(code.current())) {
            String id = readIdentifier(code);

            if (isTokenSeparator(code)) {
                return new Token("id", id);
            }
        }
        return null;
    }

    private String readIdentifier(CharacterIterator code) {
        String id = "";

        id += code.current();
        code.next();

        // letras, dígitos ou _
        while (Character.isLetterOrDigit(code.current()) || code.current() == '_') {
            id += code.current();
            code.next();
        }

        return id;
    }
}