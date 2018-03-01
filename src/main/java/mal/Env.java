package mal;

import java.util.HashMap;
import java.util.Map;

public class Env {
    static class Environment{
        Environment parent;
        Map<String, Types.MalType> data = new HashMap<>();

        public Environment(Environment parent) {
            this.parent = parent;
        }

        public Environment(Environment parent, Types.MalList binds, Types.MalList exprs) {
            this.parent = parent;
            for (int i = 0; i < binds.malTypeList.size(); i++) {
                String symbol = ((Types.MalSymbol)binds.malTypeList.get(i)).value;
                if(symbol.equals("&")){
                    data.put(((Types.MalSymbol)binds.malTypeList.get(i + 1)).value,exprs.slice(i));
                    break;
                }else {
                    data.put(symbol,exprs.malTypeList.get(i));
                }
            }
        }


        Environment find(Types.MalSymbol sym){
            if(data.containsKey(sym)){
                return this;
            }else if(parent != null){
                return parent.find(sym);
            }else {
                return null;
            }
        }

        Types.MalType get(Types.MalSymbol symbol){
            Environment env = find(symbol);
            if(env != null){
                return env.data.get(symbol);
            }else{
                return null;
            }
        }

        Environment set(Types.MalSymbol symbol, Types.MalType val){
            data.put(symbol.value,val);
            return this;
        }
    }
}
