package AnalizadorSintactico;

//Se usa gramática de los autómatas

import AnalizadorLexico.AnalizadorLexico;
import AnalizadorLexico.AFN;
import AnalizadorLexico.AFD;
import AnalizadorLexico.ClaseLexica;
import java.util.HashMap;
import java.util.Map;

public class CreadorAutomatas {
    private Map<Integer, Integer> claseslexicasExprReg;
    private String[] expresionesRegulares;
    private AnalizadorLexico lexic;
    private int posicionClase=0;
    //Se ponen aquí los valores de las clases léxicas y tokens de las expresiones regualres apra los autóamtas, porque en
    //sí mismas no tienen expresión regular, así que no es necesario un archivo. (BETA)
    public static final int OR=1, CONC=2, CERR_POS=3, CERR_KLEENE=4, OPC=5, PAR_I=6, PAR_D=7, CORCH_I=8, CORCH_D=9, GUION=10, SIMB=11, FIN=12;
    
    public CreadorAutomatas(String cargaDeClaseslexicas, AFD auxiliar){
        claseslexicasExprReg= new HashMap();
        ClaseLexica miclaselex= new ClaseLexica( adaptarRuta(cargaDeClaseslexicas) );
        lexic= new AnalizadorLexico(auxiliar);
        
        
//        for(AFN o: miclaselex.afnBasicos()){
//            System.out.println(o);
//        }
//        for(String o: miclaselex.listaRegex()){
//            System.out.println(o);
//        }
        
    }
    
    public boolean iniciar(AFN f){
        int token=0;
        if(E(f)){
            token=lexic.yylex();
            if(token==FIN)
                return true;
        }
        return false;
    }
    
    //E -> TE'
    private boolean E(AFN f){
        if(T(f))
            if(Epr(f))
                return true;
        return false;
    }
    
    //E' -> orTE' | ep
    private boolean Epr(AFN f){
        int tok= lexic.yylex();
        AFN f2= null; //Lo requiere T
        
        if(tok==OR){
            if(T(f2)){
                f.unir(f2);
                if(Epr(f))
                    return true;
            }
            return false;
        }
        lexic.regresarToken(); //Épsilon
        return true;
    }
    
    //T -> CT'
    private boolean T(AFN f){
        if(C(f))
            if(Tpr(f))
                return true;
        return false;
    }
    
    //T' -> &CT* | ep
    private boolean Tpr(AFN f){
        int tok=lexic.yylex();
        AFN f2= null;
        
        if(tok==CONC){
            if(C(f2)){
                f.concatenar(f2);
                if(Tpr(f))
                    return true;
            }
            return false;
        }
        lexic.regresarToken();
        return true;
    }
    
    //C -> FC'
    private boolean C(AFN f){
        if(F(f))
            if(Cpr(f))
                return true;
        return false;
    }
    
    //C' -> +C' | *C' | ?C' | Ep
    private boolean Cpr(AFN f){
        int tok= lexic.yylex();
        switch(tok){
            case CERR_POS:
                f.cTransitiva();
                if(Cpr(f))
                    return true;
                return false;

            case CERR_KLEENE:
                f.cEstrella();
                if(Cpr(f))
                    return true;
                return false;

            case OPC:
                f.opcional();
                if(Cpr(f))
                    return true;
                return false;

            default:
                lexic.regresarToken();
                return true;
        }
    }
    
    //F -> (E) | [SIMB-SIMB] | SIMB
    private boolean F(AFN f){
            int tok=lexic.yylex();
            String lexem1="", lexem2="";
            switch(tok){
                case PAR_I:
                    if(E(f)){
                        tok = lexic.yylex();
                        if(tok == PAR_D)
                            return true;
                    }
                    return false;

                case CORCH_I:
                    tok = lexic.yylex();
                    if(tok == SIMB){
                        lexem1= lexic.yytex();
                        tok = lexic.yylex();
                        if(tok == GUION){
                            tok = lexic.yylex();
                            if(tok == SIMB){
                                lexem2= lexic.yytex();
                                tok=lexic.yylex();
                                if(tok==CORCH_D){
                                    f.crearBasico(lexem1.charAt(0), lexem2.charAt(0));
                                    return true;
                                }
                                    
                            }
                        }
                    }
                    return false;

                case SIMB:
                    f.crearBasico(lexic.yytex().charAt(0));
                    return true;
            }
            return false;
    }
    
    private static String adaptarRuta(String ruta){
        if(System.getProperty("os.name").equals("Windows"))
            return ruta.replace("/", "\\");
        else
            return ruta;
    }

}
