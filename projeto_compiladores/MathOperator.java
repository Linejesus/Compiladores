import java.text.CharacterIterator;

public class MathOperator extends AFD {
    
    @Override
    public Token evaluate(CharacterIterator code){

        switch(code.current()){
            case '+':
                code.next();
                return new Token("adicao", "+");
            
            case '-':
                code.next();
                return new Token("subtracao", "-");
            
            case '*':
                code.next();
                return new Token("multiplicacao", "*");
            
            case '/':
                code.next();
                return new Token("divisao", "/");

            case CharacterIterator.DONE:
                return new Token("EOF", "$");

            default:
                return null;
        }
    }
}
