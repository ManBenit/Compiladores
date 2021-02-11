package AnalizadorSintactico; 

import AnalizadorLexico.AFD;

public class AccionSemantica{

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

	//E' -> orTE'| ep
	 private boolean Epr(){
		int tok;
		tok=Lexic.yylex();
		if(Tok== 'or'){
			if(T())
				if(Epr())
					return true;
			return false;
		}
		Lexic.RegresarToken();
		return true;
	}

	//T -> CT'
	private boolean T(){
		if(C())
			if(Tpr())
				return true;
		return false;
	}

	//T' -> &CT'| ep
	 private boolean Tpr(){
		int tok;
		tok=Lexic.yylex();
		if(Tok== '&'){
			if(C())
				if(Tpr())
					return true;
			return false;
		}
		Lexic.RegresarToken();
		return true;
	}

	//C -> FC'
	private boolean C(){
		if(F())
			if(Cpr())
				return true;
		return false;
	}

	//C' -> +C' | *C' | ?C' | ep
	 private boolean Cpr(){
		int tok;
		tok=Lexic.yylex();
		switch(tok){
			case '+':
				if(Cpr())
					return true;
				return false;
				break;
			case '*':
				if(Cpr())
					return true;
				return false;
				break;
			case '?':
				if(Cpr())
					return true;
				return false;
				break;
			default:
				Lexic.RegresarToken();
				return true;
				break;
		}
	}

	//F -> (E) | num | [SIMB-SIMB]
	 private boolean F(){
		int tok;
		tok=Lexic.yylex();
		switch(tok){

			case '(':
				if(E()){
					tok=Lexic.yylex();
				if(tok== ')')
						return true;
				}
				return false;
				break;

			case 'num':
				tok=Lexic.yylex();
				if(tok == 'num'){
						return true;
				}
				return false;
				break;

			case '[':
				tok=Lexic.yylex();
				if(tok == 'SIMB'){
					tok=Lexic.yylex();
				if(tok == '-'){
				tok=Lexic.yylex();
				if(tok == 'SIMB'){
					tok=Lexic.yylex();
				if(tok== ']')
						return true;
				}
				}
				}
				return false;
				break;

			default:
				Lexic.RegresarToken(tok);
				return true;
				break;
		}
		}
	}
}