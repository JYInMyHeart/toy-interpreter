package mal;

import java.util.HashMap;

import mal.Types.*;

import java.util.Map;

public class Core {
    public static Map ns = new HashMap();
    private static MalFunc add = a -> ((MalInt) a.nth(0)).add ((MalInt) a.nth(1));
    private static MalFunc mul = a -> ((MalInt) a.nth(0)).mul ((MalInt) a.nth(1));
    private static MalFunc div = a -> ((MalInt) a.nth(0)).div ((MalInt) a.nth(1));
    private static MalFunc plus = a -> ((MalInt) a.nth(0)).plus ((MalInt) a.nth(1));
    private static MalFunc isList = a -> new MalBoolean(a.list_Q()) ;
    private static MalFunc gt = a -> (((MalInt) a.nth(0)).gt((MalInt) a.nth(1)));
    private static MalFunc lt = a -> (((MalInt) a.nth(0)).lt((MalInt) a.nth(1)));
    private static MalFunc gte = a -> (((MalInt) a.nth(0)).gte((MalInt) a.nth(1)));
    private static MalFunc lte = a -> (((MalInt) a.nth(0)).lte((MalInt) a.nth(1)));
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
    private static MalFunc count = a -> {
        if(a != null && a.nth(0) instanceof MalList){
            MalList l1 = (MalList) a.nth(0);
            return new MalInt(l1.size());
        }else{
            return new MalInt(0);
        }
    };
    private static MalFunc list = a -> (MalList)a;


    static {
        ns.put("+", add);
        ns.put("-", plus);
        ns.put("*", mul);
        ns.put("/", div);
        ns.put("list?", isList);
        ns.put(">", gt);
        ns.put("<", lt);
        ns.put(">=", gte);
        ns.put("<=", lte);
        ns.put("=", eq);
        ns.put("empty?", empty);
        ns.put("count", count);
        ns.put("list", list);
    }

}
