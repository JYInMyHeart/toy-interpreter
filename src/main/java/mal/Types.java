package mal;

import java.util.ArrayList;
import java.util.List;

public class Types {
    interface ILambda {

         MalType apply(MalList args);

    }
    interface MalType {

    }
    static class MalList implements MalType{
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
            return malTypeList + " => {MalList}";
        }

        public MalList slice(Integer start, Integer end) {
            return new MalList(malTypeList.subList(start, end));
        }
        public MalList slice(Integer start) {
            return slice(start, malTypeList.size());
        }
    }
    static class MalInt implements MalType{
        int value;

        public MalInt(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value + " => {MalInt}";
        }
    }
    static class MalSymbol implements MalType{
        String value;

        public MalSymbol(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value + " => {MalSymbol}";
        }
    }

    static class MalConstant implements MalType{
        String constant;

        public MalConstant(String constant) {
            this.constant = constant;
        }

        @Override
        public String toString() {
            return constant + " => {MalConstant}";
        }
    }


}
