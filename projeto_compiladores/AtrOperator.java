import java.text.CharacterIterator;


public class AtrOperator extends AFD {
    
    @Override
    public Token evaluate(CharacterIterator code){
        int pos = code.getIndex();

        //Atribuicao
        if(code.current() == ':'){
            code.next();
            if(code.current() == '='){
                code.next();
                return new Token("op_atribuicao", ":=");
            }
        }
        code.setIndex(pos);


        return null;
    }


}
