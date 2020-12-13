package AnalizadorSintactico;

import AnalizadorLexico.AFD;

public class AccionSemantica{
    /*private AFD Lexic;
    
    public AccionSemantica(AFD afd){
        Lexic=afd;
    }

    //E -> TE'
    private boolean E(){
        if(T())
            if(Epr())
                return true;
        return false;	
    }

    //E' -> ORTE' | Ep
    private boolean Epr(){
        int tok=0;
        tok=Lexic.yylex();
        if(tok==OR){
            if(T())
                if(Epr())
                    return true;
            return false;	
        }
        Lexic.regresarToken(tok);
        return true;
    }

    //T -> CT'
    private boolean T(){
        if(C())
            if(Tpr())
                return true;
        return false;	
    }

    //T' -> &CT' | Ep
    private boolean Tpr(){
        int tok;
        tok=Lexic.yylex();
        if(tok==CONC){
            if(C())
                if(Tpr())
                    return true;
            return false;
        }
        Lexic.regresarToken(tok);
        return true;
    }


    //C' -> +C' | *C' | ?C' | Ep
    private boolean Cpr(){
        int tok;
        tok=Lexic.yylex();
        switch(tok){
            case '+':
                if(Cp())
                    return true;
                return false;
                break;

            case '*':
                if(Cp())
                    return true;
                return false;
                break;

            case '?':
                if(Cp())
                    return true;
                return false;
                break;

            default:
                Lexic.regresarToken(tok);
                return true;
                break;
        }
    }

    //C -> FC'
    private boolean C(){
            if(F())
                    if(Cpr())
                            return true;
            return false;
    }

    //F -> (E) | [SIMB-SIMB] | SIMB
    private boolean F(){
        int tok=0;
        int tok1, tok2, tok3;
        tok1=Lexic.yylex();
        switch(tok1){
            case '(':
                if(E()){
                        tok1 = Lexic.yylex();
                        if(tok1 == ')')
                                return true;
                }
                return false;
                break;

            case '[':
                tok1 = Lexic.yylex();
                if(tok1 == SIMB){
                    tok2 = Lexic.yylex();
                    if(tok2 == '-'){
                        tok2 = Lexic.yylex();
                        if(tok2 == SIMB){
                            tok3=Lexic.yylex();
                            if(tok3==']')
                                return true;
                        }
                    }
                }
                return false;
                break;

            case 'a': //SIMB
                tok=Lexic.yylex();
                if(tok==SIMB)
                        return true;
                return false;
                break;

            default:
                Lexic.regresarToken(tok);
                return true;	
                break;
        }
        
        return true; //Se supone que este no va
    }*/
}
