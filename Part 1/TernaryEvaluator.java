import java.io.InputStream;
import java.io.IOException;
/*
* -------------------------------------------------------------------------
* 	        |     '0' .. '9'     |  ':'    |       '?'          |  $    |
* -------------------------------------------------------------------------
* 	        |		             |	       |	                |       |
* Tern      | '0'..'9' TernTail  |  error  |       error        | error |
*           | 	   	             |	       |    	            |       |
* -------------------------------------------------------------------------
*           |		             |	       |		            |       |
* TernTail  |       error	     |    e    |  '?' Tern ':' Tern |   e   |
* 	        |	  	             |	       |    	     	    |       |
* -------------------------------------------------------------------------
*/


class TernaryEvaluator {
    private final InputStream in;

    private int lookahead;

    public TernaryEvaluator(InputStream in) throws IOException {
        this.in = in;
        lookahead = in.read();
    }

    private void consume(int symbol) throws IOException, ParseError {
        if (lookahead == symbol)
            lookahead = in.read();
        else
            throw new ParseError();
    }

    private boolean isDigit(int c) {
        return '0' <= c && c <= '9';
    }

    private int evalDigit(int c) {
        return c - '0';
    }

    public int eval() throws IOException, ParseError {
        System.out.println("eval");
        int value = Expr();

        if (lookahead != '\r' && lookahead != '\n')
            throw new ParseError();

        return value;
    }

    private int Expr() throws IOException, ParseError {
        //first character is always a digit!!!
        System.out.println("expr");

        int value = Term();
        
        

        return ExprTail(value);
        //throw new ParseError();
    }

    private int ExprTail(int condition) throws IOException, ParseError {
        
        switch(lookahead){
            case '+': // +

                consume(lookahead);
                int value_plus = Term();

                return ExprTail(condition + value_plus); 

            case '-': // -

                consume(lookahead);
                int value_minus = Term();

                return ExprTail(condition - value_minus); 
            default: //e
            
                return condition;
        }

           

        //throw new ParseError();
    }


    private int Term() throws IOException, ParseError {
        System.out.println("term");

        int value = Factor();
        
        return value;
        //throw new ParseError();
    }

    private int Factor() throws IOException, ParseError {
        System.out.println("factor");
        
        if (isDigit(lookahead)) { // #9
            return Num();

            
        }else if(lookahead == '('){ //#10
            System.out.println("mh please");

            int cond = '(';
            consume(lookahead);
                     
        }

        throw new ParseError();
    }

    private int Num() throws IOException, ParseError {
        System.out.println("num");

        return Digit();
        

        //throw new ParseError();
    }

    private int Digit() throws IOException, ParseError {
        System.out.println("digit");

        int cond = evalDigit(lookahead);
        consume(lookahead);
        return cond;

        //throw new ParseError();
    }

}
