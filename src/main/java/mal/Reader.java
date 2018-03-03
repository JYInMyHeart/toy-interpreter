package mal;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mal.Types.*;

import static mal.Printer.println;


/**
 * @author xck
 */
public class Reader {
    static int pos;
    static List<String> tokens;

    public Reader() {
        tokens = new ArrayList<>();
        pos = 0;
    }

    static String next() {
        return tokens.get(pos++);
    }

    static String peek() {
        if (pos >= tokens.size()) {
            return null;
        } else {
            return tokens.get(pos);
        }
    }

    static MalType readStr(String exp) {
        pos = 0;
        tokens = tokenizer(exp);
        return readForm();
    }

    static MalType readForm() {
        String token = peek();
        if (token == null) throw new NullPointerException();
        char head = token.charAt(0);
        MalType form;
        switch (head) {
            case '\'':
                next();
                return new MalList(new MalSymbol("quote"), readForm());
            case '`':
                next();
                return new MalList(new MalSymbol("quasiquote"), readForm());
            case '~':
                if (token.equals("~")) {
                    next();
                    return new MalList(new MalSymbol("unquote"), readForm());
                } else {
                    next();
                    return new MalList(new MalSymbol("splice-unquote"), readForm());
                }
            case '@':
                next();
                return new MalList(new MalSymbol("deref"),
                        readForm());
            case '(':
                form = readList(new MalList(), '(', ')');
                break;
            case ')':
                System.out.print("unexpected ')'");
            case '[':
                form = readVector(new MalVector(), '[', ']');
                break;
            case ']':
                System.out.print("unexpected ']'");
            default:
                form = readAtom();
        }
        return form;
    }

    static MalType readList(MalList malList, char start, char end) {
        MalList list = new MalList();
        String token = next();
        if (token.charAt(0) != start) {
            throw new NullPointerException("expected '" + start + "'");
        }
        while ((token = peek()) != null && token.charAt(0) != end) {
            list.malTypeList.add(readForm());
        }

        next();
        return list;
    }

    static MalType readVector(MalVector malList, char start, char end) {
        MalVector list = new MalVector();
        String token = next();
        if (token.charAt(0) != start) {
            throw new NullPointerException("expected '" + start + "'");
        }
        while ((token = peek()) != null && token.charAt(0) != end) {
            list.malTypeList.add(readForm());
        }

        next();
        return list;
    }

    static MalType readAtom() {
        String token = next();
        Pattern pattern = Pattern.compile("(^-?[0-9]+$)|(^-?[0-9][0-9.]*$)|(^nil$)|(^true$)|(^false$)|^\"(.*)\"$|:(.*)|(^[^\"]*$)");
        Matcher matcher = pattern.matcher(token);
        if (!matcher.find()) {
            println("unrecognized token '" + token + "'");
            return null;
        }
        if (matcher.group(1) != null) {
            return new MalInt(Integer.parseInt(matcher.group(1)));
        } else if (matcher.group(3) != null) {
            return new MalConstant("nil");
        } else if (matcher.group(4) != null) {
            return new MalConstant("true");
        } else if (matcher.group(5) != null) {
            return new MalConstant("false");
        } else if (matcher.group(6) != null) {
            return new MalString(matcher.group(6));
        } else if (matcher.group(8) != null) {
            return new MalSymbol(matcher.group(8));
        } else {
            println("unrecognized '" + matcher.group(0) + "'");
            return null;
        }
    }

    /**
     * [\s,]*(~@|[\[\]{}()'`~^@]|"(?:\\.|[^\\"])*"|;.*|[^\s\[\]{}('"`,;)]*)
     */
    static List<String> tokenizer(String exp) {
        List<String> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile("[\\s ,]*(~@|[\\[\\]{}()'`~@]|\"(?:[\\\\].|[^\\\\\"])*\"|;.*|[^\\s \\[\\]{}()'\"`~@,;]*)");
        Matcher matcher = pattern.matcher(exp);
        while (matcher.find()) {
            String token = matcher.group(1);
            if (token != null &&
                    !token.equals("") &&
                    !(token.charAt(0) == ';')) {
                tokens.add(token);
            }
        }
        return tokens;
    }
}
