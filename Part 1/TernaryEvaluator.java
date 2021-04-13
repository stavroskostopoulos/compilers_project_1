import java.io.InputStream;
import java.io.IOException;


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
        
        int value = Expr();

        if (lookahead != '\r' && lookahead != '\n')
            throw new ParseError();

        return value;
    }

    private int Expr() throws IOException, ParseError {
        //first character is always a digit!!!
        
        int value = Term();
        
        return ExprTail(value);
        
    }

    private int ExprTail(int condition) throws IOException, ParseError {
        
        switch(lookahead){
            case '+': // +  #3

                consume(lookahead);
                int value_plus = Term();

                return ExprTail(condition + value_plus); 

            case '-': // -  #4

                consume(lookahead);
                int value_minus = Term();

                return ExprTail(condition - value_minus); 
            default: // e   #5
            
                return condition;
        }      

    }


    private int Term() throws IOException, ParseError {
        
        int value = Factor();
         
        return TermTail(value);
       
    }

    private int TermTail(int condition) throws IOException, ParseError {
        
        if(lookahead == '*'){// get **   #7

            consume('*');
            if(lookahead == '*'){// get the second *

                consume('*');
                int power = Factor();
                
                return TermTail((int)Math.pow(condition, power)); //math pow works with double so we cast to int

            }else{// did not receive the second *
                throw new ParseError();
            }

        }else{// e     #8
            return condition;
        }      
        
    }

    private int Factor() throws IOException, ParseError {
            
        if (isDigit(lookahead)) { // #9
            return Num();

            
        }else if(lookahead == '('){ //#10
            
            consume('(');
            int value = Expr();
            
            if(lookahead == ')'){
                //parenthesis closed properly
                consume(')');
                
                return value;

            }else{
                
                throw new ParseError();
            }
                   
        }else{
            throw new ParseError();
        }

    }

    private int Num() throws IOException, ParseError {   

        //read the first digit of our number
        int value = Digit();
        
        return NumTail(value);
        
    }

    private int NumTail(int condition) throws IOException, ParseError {
        
        if (isDigit(lookahead)) { //if there is a number #12
            
            int value = Digit();
            // value keeps our new digit,and the argument condition holds our number

            //convert both of them to strings
            String left_part = Integer.toString(condition);
            String new_digit = Integer.toString(value);

            //concat
            String final_number = left_part + new_digit;


            return NumTail(Integer.parseInt(final_number));
            

        }else{
            return condition; // there is nothing #13
        }

        
    }


    private int Digit() throws IOException, ParseError {

        int cond = evalDigit(lookahead);
        consume(lookahead);
        
        return cond;

    }

}
