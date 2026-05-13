import java.text.CharacterIterator;

public class FimLinha extends AFD {
    
    @Override
    public Token evaluate(CharacterIterator code){

        switch(code.current()){
            case '.':
                code.next();
                return new Token("fim_linha", ".");

            default:
                return null;
        }
    }
}
