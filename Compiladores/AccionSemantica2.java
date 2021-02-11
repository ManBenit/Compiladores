package AnalizadorSintactico; 

import Analizadorlexico.AFD;

import Analizadorlexico.AFN;

public class AccionSemantica2{

	Analizadorlexico lexic;

	public static final int OR=1, CONC=2, CERR_POS=3, CERR_KLEENE=4, OPC=5, PAR_I=6, PAR_D=7, CORCH_I=8, CORCH_D=9, GUION=10, SIMB=11, FIN=12, NUM=13;

	public AccionSemantica2(AFD afd){
		lexic=new Analizadorlexico(afd);
	}
	public static void main(String[] args){
}
	//E -> TE'
	private boolean E(AFN f){
		if(T(f))
			if(Epr(f))
				return true;
		return false;
	}

	//E' -> orTE'| ep
	 private boolean Epr(AFN f){
		int tok;
		tok=lexic.yylex();
		if(tok==OR ){
			if(T(f))
				if(Epr(f))
					return true;
			return false;
		}
		lexic.regresarToken();
		return true;
	}

	//T -> CT'
	private boolean T(AFN f){
		if(C(f))
			if(Tpr(f))
				return true;
		return false;
	}

	//T' -> &CT'| ep
	 private boolean Tpr(AFN f){
		int tok;
		tok=lexic.yylex();
		if(tok==CONC ){
			if(C(f))
				if(Tpr(f))
					return true;
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

	//C' -> +C' | *C' | ?C' | ep
	 private boolean Cpr(AFN f){
		int tok;
		tok=lexic.yylex();
		switch(tok){

			case CERR_POS:
				if(Cpr(f))
					return true;
				return false;
				break;

			case CERR_KLEENE:
				if(Cpr(f))
					return true;
				return false;
				break;

			case OPC:
				if(Cpr(f))
					return true;
				return false;
				break;
			default:
				lexic.regresarToken();
				return true;
				break;
		}
	}

	//F -> (E) | num | [SIMB-SIMB]
	 private boolean F(AFN f){
		int tok;
		tok=lexic.yylex();
		switch(tok){

			case PAR_D:
				if(E(f)){
					tok=lexic.yylex();
				if(tok== PAR_I)
						return true;
				}
				return false;
				break;

			case NUM:
				tok=lexic.yylex();
				if(tok == NUM){
						return true;
				}
				return false;
				break;

			case CORCH_D:
				tok=lexic.yylex();
				if(tok == SIMB){
					tok=lexic.yylex();
				if(tok == GUION){
				tok=lexic.yylex();
				if(tok == SIMB){
					tok=lexic.yylex();
				if(tok== CORCH_I)
						return true;
				}
				}
				}
				return false;
				break;

			default:
				lexic.regresarToken(tok);
				return true;
				break;
		}
		}
	}