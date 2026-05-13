import java.text.CharacterIterator;

public class Parenteses extends AFD {
    
    @Override
    public Token evaluate(CharacterIterator code){

        switch(code.current()){
            case '(':
                code.next();
                return new Token("abre_parenteses", "(");
            
            case ')':
                code.next();
                return new Token("fecha_parenteses", ")");

            default:
                return null;
        }
    }
}
