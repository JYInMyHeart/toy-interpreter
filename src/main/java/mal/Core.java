package mal;

import java.util.HashMap;

import mal.Types.*;

import java.util.Map;

public class Core {
    public static Map ns = new HashMap();
    private static MalFunc add = a -> new MalInt(((MalInt) a.nth(0)).value + ((MalInt) a.nth(1)).value);
    private static MalFunc mul = a -> new MalInt(((MalInt) a.nth(0)).value * ((MalInt) a.nth(1)).value);
    private static MalFunc div = a -> new MalInt(((MalInt) a.nth(0)).value / ((MalInt) a.nth(1)).value);
    private static MalFunc plus = a -> new MalInt(((MalInt) a.nth(0)).value - ((MalInt) a.nth(1)).value);
    private static MalFunc isList = a -> new MalBoolean(a instanceof MalList) ;
    private static MalFunc gt = a -> new MalBoolean(((MalInt) a.nth(0)).value > ((MalInt) a.nth(1)).value);
    private static MalFunc lt = a -> new MalBoolean(((MalInt) a.nth(0)).value < ((MalInt) a.nth(1)).value);
    private static MalFunc eq = a -> {
        if(a != null && a.nth(0) instanceof MalList){
            MalList l1 = (MalList) a.nth(0);
            MalList l2 = (MalList) a.nth(1);
            boolean enable = l1.size() == l2.size() ;
            if(enable){
                for (int i = 0; i < l1.size(); i++) {
                    enable &= (l1.nth(i).equals(l2.nth(i)));
                }
            }else{
                enable = false;
            }
            return new MalBoolean(enable);
        }else if(a != null && a.nth(0) instanceof MalInt){
            MalInt l1 = (MalInt) a.nth(0);
            MalInt l2 = (MalInt) a.nth(1);
            return new MalBoolean(l1.value == l2.value);
        }else{
            return new MalBoolean(false);
        }
    };
    private static MalFunc empty = a -> new MalBoolean(a.size() == 0);


    static {
        ns.put("+", add);
        ns.put("-", plus);
        ns.put("*", mul);
        ns.put("/", div);
        ns.put("list?", isList);
        ns.put(">", gt);
        ns.put("<", lt);
        ns.put("=", eq);
        ns.put("empty?", empty);
    }

}
