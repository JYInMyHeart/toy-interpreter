package mal;

import java.util.HashMap;

import mal.Types.*;

import java.util.Map;

@SuppressWarnings("unchecked")
public class Core {
    public static Map ns = new HashMap();
    private static  MalFunc add = new MalFunc() {
        @Override
        public MalType apply(MalList a) throws Exception {
            return ((MalInt)a.nth(0)).add ((MalInt) a.nth(1));
        }
    };
    private static  MalFunc mul = new MalFunc() {
        @Override
        public MalType apply(MalList a) throws Exception {
            return ((MalInt)a.nth(0)).mul ((MalInt) a.nth(1));
        }
    };
    private static  MalFunc div = new MalFunc() {
        @Override
        public MalType apply(MalList a) throws Exception {
            return ((MalInt)a.nth(0)).div ((MalInt) a.nth(1));
        }
    };
    private static  MalFunc plus = new MalFunc() {
        @Override
        public MalType apply(MalList a) throws Exception {
            return ((MalInt)a.nth(0)).plus ((MalInt) a.nth(1));
        }
    };
    private static  MalFunc isList = new MalFunc() {
        @Override
        public MalType apply(MalList a) throws Exception {
            return new MalBoolean(a.list_Q()) ;
        }
    };
    private static  MalFunc gt = new MalFunc() {
        @Override
        public MalType apply(MalList a) throws Exception {
            return ((MalInt)a.nth(0)).gt ((MalInt) a.nth(1));
        }
    };
    private static  MalFunc lt = new MalFunc() {
        @Override
        public MalType apply(MalList a) throws Exception {
            return ((MalInt)a.nth(0)).lt ((MalInt) a.nth(1));
        }
    };
    private static  MalFunc gte = new MalFunc() {
        @Override
        public MalType apply(MalList a) throws Exception {
            return ((MalInt)a.nth(0)).gte ((MalInt) a.nth(1));
        }
    };
    private static  MalFunc lte = new MalFunc() {
        @Override
        public MalType apply(MalList a) throws Exception {
            return ((MalInt)a.nth(0)).lte ((MalInt) a.nth(1));
        }
    };
    private static  MalFunc eq = new MalFunc() {
        @Override
        public MalType apply(MalList a) throws Exception {
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
        }
    };


    private static  MalFunc empty = new MalFunc() {
        @Override
        public MalType apply(MalList a) throws Exception {
            return new MalBoolean(a.size() == 0);
        }
    };
    private static  MalFunc count = new MalFunc() {
        @Override
        public MalType apply(MalList a) throws Exception {
            if(a != null && a.nth(0) instanceof MalList){
                MalList l1 = (MalList) a.nth(0);
                return new MalInt(l1.size());
            }else{
                return new MalInt(0);
            }
        }
    };

    private static MalFunc list = new MalFunc() {
        @Override
        public MalType apply(MalList a) throws Exception {
            return a;
        }
    };



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
