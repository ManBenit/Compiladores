import java.util.Scanner;
import javax.swing.JOptionPane;

public class elPrograma{
	/*public static void main(String[] args){
		/*Scanner br = new Scanner (System.in);
		System.out.println(br.nextLine());
		JOptionPane.showMessageDialog(null, "Mensaje XD", "Application says:", 1);
	}*/

	public elPrograma(AFD afd){
		lexic=new Analizadorlexico(afd);
	}
	public static void main(String[] args){
		JOptionPane.showMessageDialog(null, "elMensaje XD", "Application says:", 1);
	//E -> TE'
	
	}

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
		if(tok== 'or'){
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
		if(tok== '&'){
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
			case '+':
				if(Cpr(f))
					return true;
				return false;
				break;
			case '*':
				if(Cpr(f))
					return true;
				return false;
				break;
			case '?':
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

			case '(':
				if(E(f)){
					tok=lexic.yylex();
				if(tok== ')')
						return true;
				}
				return false;
				break;

			case 'num':
				tok=lexic.yylex();
				if(tok == 'num'){
						return true;
				}
				return false;
				break;

			case '[':
				tok=lexic.yylex();
				if(tok == 'SIMB'){
					tok=lexic.yylex();
				if(tok == '-'){
				tok=lexic.yylex();
				if(tok == 'SIMB'){
					tok=lexic.yylex();
				if(tok== ']')
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