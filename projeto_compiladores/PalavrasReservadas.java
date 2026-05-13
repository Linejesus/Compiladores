import java.text.CharacterIterator;


public class PalavrasReservadas extends AFD {
    
    @Override
    public Token evaluate(CharacterIterator code){
        int pos = code.getIndex();

        //senaose
        if(code.current() == 's'){
            code.next();
            if(code.current() == 'e'){
                code.next();
                if(code.current() == 'n'){
                    code.next();
                    if(code.current() == 'a'){
                        code.next();
                        if(code.current() == 'o'){
                            code.next();
                            if(code.current() == 's'){
                                code.next();
                                if(code.current() == 'e'){
                                    code.next();
                                    return new Token("reservada_senaose", "senaose");
                                }
                            }
                        }
                    }
                }
            }
        }
        code.setIndex(pos);


        //senao
        if(code.current() == 's'){
            code.next();
            if(code.current() == 'e'){
                code.next();
                if(code.current() == 'n'){
                    code.next();
                    if(code.current() == 'a'){
                        code.next();
                        if(code.current() == 'o'){
                            code.next();
                            return new Token("reservada_senao", "senao");
                        }
                    }
                }
            }
        }
        code.setIndex(pos);


        if(code.current() == 's'){
            code.next();

            if(code.current() == 'e'){
                code.next();
                return new Token("reservada_se", "se");
            }
        }
        code.setIndex(pos);


        //WHILE
        if(code.current() == 'e'){
            code.next();
            if(code.current() == 'n'){
                code.next();
                if(code.current() == 'q'){
                    code.next();
                    if(code.current() == 'u'){
                        code.next();
                        if(code.current() == 'a'){
                            code.next();
                            if(code.current() == 'n'){
                            code.next();
                                if(code.current() == 't'){
                                    code.next();
                                    if(code.current() == 'o'){
                                        code.next();
                                        return new Token("reservada_enquanto", "enquanto");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        code.setIndex(pos);

        //DO
        if(code.current() == 'e'){
            code.next();
            if(code.current() == 'x'){
                code.next();
                if(code.current() == 'e'){
                    code.next();
                    if(code.current() == 'c'){
                        code.next();
                        if(code.current() == 'u'){
                            code.next();
                            if(code.current() == 't'){
                                code.next();
                                if(code.current() == 'e'){
                                    code.next();
                                    return new Token("reservada_execute", "execute");
                                }
                            }
                        }
                    }
                }
            }
        }
        code.setIndex(pos);

        //FOR
        if(code.current() == 'p'){
            code.next();
            if(code.current() == 'a'){
                code.next();
                if(code.current() == 'r'){
                    code.next();
                    if(code.current() == 'a'){
                        code.next();
                        return new Token("reservada_para", "para");
                    }
                }
            }
        }
        code.setIndex(pos);

        

        //escreva
        if(code.current() == 'e'){
            code.next();
            if(code.current() == 's'){
                code.next();
                if(code.current() == 'c'){
                    code.next();
                    if(code.current() == 'r'){
                        code.next();
                        if(code.current() == 'e'){
                            code.next();
                            if(code.current() == 'v'){
                                code.next();
                                if(code.current() == 'a'){
                                    code.next();
                                    return new Token("reservada_escreva", "escreva");
                                }
                            }
                        }
                    }
                }
            }
        }
        code.setIndex(pos);

        //leia
        if(code.current() == 'l'){
            code.next();
            if(code.current() == 'e'){
                code.next();
                if(code.current() == 'i'){
                    code.next();
                    if(code.current() == 'a'){
                        code.next();
                        return new Token("reservada_leia", "leia");
                    }
                }
            }
        }
        code.setIndex(pos);

        //inteiro
        if(code.current() == 'i'){
            code.next();
            if(code.current() == 'n'){
                code.next();
                if(code.current() == 't'){
                    code.next();
                    if(code.current() == 'e'){
                        code.next();
                        if(code.current() == 'i'){
                            code.next();
                            if(code.current() == 'r'){
                                code.next();
                                if(code.current() == 'o'){
                                    code.next();
                                    return new Token("reservada_inteiro", "inteiro");
                                }
                            }
                        }
                    }
                }
            }
        }
        code.setIndex(pos);

        //decimal
        if(code.current() == 'd'){
            code.next();
            if(code.current() == 'e'){
                code.next();
                if(code.current() == 'c'){
                    code.next();
                    if(code.current() == 'i'){
                        code.next();
                        if(code.current() == 'm'){
                            code.next();
                            if(code.current() == 'a'){
                                code.next();
                                if(code.current() == 'l'){
                                    code.next();
                                    return new Token("reservada_decimal", "decimal");
                                }
                            }
                        }
                    }
                }
            }
        }
        code.setIndex(pos);

        //texto
        if(code.current() == 't'){
            code.next();
            if(code.current() == 'e'){
                code.next();
                if(code.current() == 'x'){
                    code.next();
                    if(code.current() == 't'){
                        code.next();
                        if(code.current() == 'o'){
                        code.next();
                        return new Token("reservada_texto", "texto");
                        }
                    }
                }
            }
        }
        code.setIndex(pos);

        //programa
        if(code.current() == 'p'){
            code.next();
            if(code.current() == 'r'){
                code.next();
                if(code.current() == 'o'){
                    code.next();
                    if(code.current() == 'g'){
                        code.next();
                        if(code.current() == 'r'){
                            code.next();
                            if(code.current() == 'a'){
                                code.next();
                                if(code.current() == 'm'){
                                    code.next();
                                    if(code.current() == 'a'){
                                        code.next();
                                        return new Token("reservada_programa", "programa");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        code.setIndex(pos);

        //fimprograma
        if(code.current() == 'f'){
            code.next();
            if(code.current() == 'i'){
                code.next();
                if(code.current() == 'm'){
                    code.next();
                    if(code.current() == 'p'){
                        code.next();
                        if(code.current() == 'r'){
                            code.next();
                            if(code.current() == 'o'){
                                code.next();
                                if(code.current() == 'g'){
                                    code.next();
                                    if(code.current() == 'r'){
                                        code.next();
                                        if(code.current() == 'a'){
                                            code.next();
                                            if(code.current() == 'm'){
                                                code.next();
                                                if(code.current() == 'a'){
                                                    code.next();
                                                    return new Token("reservada_fimprograma", "fimprograma");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        code.setIndex(pos);


        return null;
    }


}
    