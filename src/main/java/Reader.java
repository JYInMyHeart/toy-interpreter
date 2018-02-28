import java.util.List;

/**
 * @author xck
 */
public class Reader {
    int pos;
    List<Token> tokens;
    class Token{

    }
    Token next(){
        return tokens.get(pos++);
    }
    Token peek(){
        return tokens.get(pos);
    }
    void readStr(){
        tokenizer();
        Reader reader = new Reader();
        reader.readForm();
    }

    void readForm() {
        Token token = peek();
        switch (token){
            case
        }
    }

    /**
     *[\s,]*(~@|[\[\]{}()'`~^@]|"(?:\\.|[^\\"])*"|;.*|[^\s\[\]{}('"`,;)]*)
     */
    List<Token> tokenizer(String exp) {
        return null;
    }
}
