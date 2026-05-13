import java.text.CharacterIterator;


public class RelacionalOperator extends AFD {
    
    @Override
    public Token evaluate(CharacterIterator code){
        int pos = code.getIndex();

        //Menor ou igual
        if(code.current() == '<'){
            code.next();
            if(code.current() == '='){
                code.next();
                return new Token("op_relacional", "<=");
            }
        }
        code.setIndex(pos);

        //Maior ou igual
        if(code.current() == '>'){
            code.next();
            if(code.current() == '='){
                code.next();
                return new Token("op_relacional", ">=");
            }
        }
        code.setIndex(pos);

        //Menor
        if(code.current() == '<'){
            code.next();
            return new Token("op_relacional", "<");
        }
        code.setIndex(pos);

        //Maior
        if(code.current() == '>'){
            code.next();
            return new Token("op_relacional", ">");
        }
        code.setIndex(pos);

        //Igual
        if(code.current() == '='){
            code.next();
            if(code.current() == '='){
                code.next();
                return new Token("op_relacional", "==");
            }
        }
        code.setIndex(pos);

        //Diferente
        if(code.current() == '!'){
            code.next();
            if(code.current() == '='){
                code.next();
                return new Token("op_relacional", "!=");
            }
        }
        code.setIndex(pos);


        return null;
    }


}
