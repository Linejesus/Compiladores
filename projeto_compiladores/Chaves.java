import java.text.CharacterIterator;

public class Chaves extends AFD {
    
    @Override
    public Token evaluate(CharacterIterator code){

        switch(code.current()){
            case '{':
                code.next();
                return new Token("abre_chaves", "{");
            
            case '}':
                code.next();
                return new Token("fecha_chaves", "}");

            default:
                return null;
        }
    }
}
