
class milBools{

	//E -> TE'
	bool E(){
		if(T())
			if(Epr())
				return true;
		return false;	
	}
	
	//E' -> ORTE' | Ep
	bool Epr(){
		int Tok;
		Tok=Lexic.yylex();
		if(Tok==OR){
			if(T())
				if(Epr())
					return true;
			return false;	
		}
		Lexic.RegresaToken();
		return true;
	}
	
	//T -> CT'
	bool T(){
		if(C())
			if(Tpr())
				return true;
		return false;	
	}
	
	//T' -> &CT' | Ep
	bool Tpr(){
		int Tok;
		Tok=Lexic.yylex();
		if(Tok==CONC){
			if(C())
				if(Tpr())
					return true;
			return false;
		}
		Lexic.RegresaToken();
		return true;
	}
	
	
	//C' -> +C' | *C' | ?C' | Ep
	bool Cpr(){
		int Tok;
		Tok=Lexic.yylex();
		switch(Tok):
			case +:
				if(Cp())
					return true;
				return false;
		
			case *:
				if(Cp())
					return true;
				return false;
				
			case ?:
				if(Cp())
					return true;
				return false;
		
			default:
				Lexic.RegresaToken();
				return true;
	}
	
	//C -> FC'
	bool C(){
		if(F())
			if(Cpr())
				return true;
		return false;
	}
	
	//F -> (E) | [SIMB-SIMB] | SIMB
	bool F(){
		int Tok1, Tok2, Tok3;
		Tok1=Lexic.yylex();
		switch(Tok1):
			case Par_I:
				if(E()){
					Tok1 = Lexic.yylex();
					if(Tok1 == Par_D)
						return true;
				}
				return false;
				
			case Corch_Izq:
				Tok1 = Lexic.yylex();
				if(Tok1 == SIMB){
					Tok2 = Lexic.yylex();
					if(Tok2 == Guion){
						Tok2 == Lexic.yylex()
						if(Tok2 == SIMB){
							Tok3=Lexic.yylex();
							if(Tok3==CORCH_DER)
								return true;
						}
					}
				}
				return false;
			
			case SIMB:
				int Tok;
				Tok=Lexic.yylex();
				if(Tok==SIMB)
					return true;
				return false;
				
			default:
				Lexic.RegresaToken();
				return true;	
	}	
}