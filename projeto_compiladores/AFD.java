import java.text.CharacterIterator;

public abstract class AFD {
    public abstract Token evaluate(CharacterIterator code);

    public boolean isTokenSeparator(CharacterIterator code){
        return Character.isWhitespace(code.current()) ||
        code.current() ==  '+' ||
        code.current() ==  '-' ||
        code.current() ==  '*' ||
        code.current() ==  '/' ||
        code.current() ==  '(' ||
        code.current() ==  ')' ||
        code.current() ==  '{' ||
        code.current() ==  '}' ||
        code.current() ==  '>' ||
        code.current() ==  '<' ||
        code.current() ==  ':' ||
        code.current() ==  '=' ||
        code.current() ==  '!' ||
        code.current() ==  '.' ||
        code.current() ==  CharacterIterator.DONE;

    }

}
