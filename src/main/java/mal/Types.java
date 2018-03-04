package mal;

import java.util.*;

public class Types {
    interface ILambda {
         MalType apply(MalList args) throws Exception;
    }
    interface MalType {

    }
    static class MalList implements MalType{
        List<MalType> malTypeList;

        public Boolean list_Q() { return true; }
        public int size(){
            return malTypeList.size();
        }

        public MalType nth(Integer idx) {
            return malTypeList.get(idx);
        }

        public MalList(MalType... malTypes) {
            this.malTypeList = new ArrayList<>();
            malTypeList.addAll(Arrays.asList(malTypes));
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
    static class MalVector extends MalList{
        public Boolean list_Q() { return false; }

        public MalVector(List<MalType> malTypeList) {
            this.malTypeList = malTypeList;
        }
        public MalVector(MalType... malTypes) {
            this.malTypeList = new ArrayList<>();
            malTypeList.addAll(Arrays.asList(malTypes));
        }

        @Override
        public String toString() {
            return malTypeList + " => {MalVector}";
        }
        public MalList slice(Integer start, Integer end) {
            return new MalVector(malTypeList.subList(start, end));
        }
        public MalList slice(Integer start) {
            return slice(start, malTypeList.size());
        }
    }
    static class MalHashMap implements MalType{
        HashMap<MalType,MalType> map;

        public MalHashMap() {
            map = new HashMap<>();
        }

        void put(MalType key,MalType value){
            map.put(key,value);
        }

        MalType get(MalType key){
            return map.get(key);
        }

        Set<Map.Entry<MalType, MalType>> entrySet(){
            return map.entrySet();
        }

        @Override
        public String toString() {
            return map + " => {MalHashMap}";
        }
    }
    static class MalInt implements MalType{
        int value;

        public MalInt(int value) {
            this.value = value;
        }

        public MalInt add(MalInt malInt){
            return new MalInt(value + malInt.value);
        }
        public MalInt mul(MalInt malInt){
            return new MalInt(value * malInt.value);
        }
        public MalInt div(MalInt malInt){
            return new MalInt(value / malInt.value);
        }
        public MalInt plus(MalInt malInt){
            return new MalInt(value - malInt.value);
        }
        public MalBoolean lt(MalInt malInt){
            return new MalBoolean(value < malInt.value);
        }
        public MalBoolean gt(MalInt malInt){
            return new MalBoolean(value > malInt.value);
        }
        public MalBoolean lte(MalInt malInt){
            return new MalBoolean(value <= malInt.value);
        }
        public MalBoolean gte(MalInt malInt){
            return new MalBoolean(value >= malInt.value);
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

    static class MalString implements MalType{
        String value;

        public MalString(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value + " => {MalString}";
        }
    }

    @FunctionalInterface
    interface MalFunc extends MalType, ILambda {
        MalType apply(MalList a) throws Exception;
    }

    static class MalBoolean implements MalType{
        boolean value;

        public MalBoolean(boolean value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return Boolean.toString(value);
        }
    }


}
