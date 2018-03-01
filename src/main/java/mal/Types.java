package mal;

import java.util.ArrayList;
import java.util.List;

public class Types {
    public static interface ILambda {

        public MalType apply(MalList args);

    }
    abstract static class MalType {

    }
    static class MalList extends MalType{
        List<MalType> malTypeList;

        public MalList(MalType... malTypes) {
            this.malTypeList = new ArrayList<>();
            for (MalType mal:malTypes) {
                malTypeList.add(mal);
            }
        }

        public MalList(List<MalType> malTypeList) {
            this.malTypeList = malTypeList;
        }

        @Override
        public String toString() {
            return "MalList{" +
                    "malTypeList=" + malTypeList +
                    '}';
        }

        public MalList slice(Integer start, Integer end) {

            return new MalList(malTypeList.subList(start, end));

        }

        public MalList slice(Integer start) {

            return slice(start, malTypeList.size());

        }
    }
    static class MalInt extends MalType{
        int value;

        public MalInt(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "MalInt{" +
                    "value=" + value +
                    '}';
        }
    }
    static class MalSymbol extends MalType{
        String value;

        public MalSymbol(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "MalSymbol{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    static class MalConstant extends MalType{
        String constant;

        public MalConstant(String constant) {
            this.constant = constant;
        }

        @Override
        public String toString() {
            return "MalConstant{" +
                    "constant='" + constant + '\'' +
                    '}';
        }
    }


}
