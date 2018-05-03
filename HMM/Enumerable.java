
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * The Enumerable typeclass.
 * 
 * This is for implementing "dictionary-passing style typeclass". For more
 * information on typeclasses please refer to
 * http://okmij.org/ftp/Computation/typeclass.html
 * @param <T> The true form of the enumerable type the instance described.
 * @author bctnry
 */
public interface Enumerable<T> {
    /**
     * Create an Enumerable String instance from an array of strings.
     * A shorthand for creating instances of anonymous classes.
     * @param strings
     * @return the enumerable instance.
     */
    public static Enumerable<String> fromStrings(String[] strings) {
        return new Enumerable<String>() {
            @Override
            public String toString() {
                StringBuilder resultStringBuilder = new StringBuilder();
                String[] names = this.names();
                resultStringBuilder.append("Instance{");
                for(int i = 0; i < names.length; i ++) {
                    resultStringBuilder.append("<" + i + ":" + names[i] + ">");
                }
                resultStringBuilder.append("}");
                return resultStringBuilder.toString();
            }
            @Override
            public int size() {
                return strings.length;
            }
            @Override
            public String fromName(String name) {
                return name;
            }
            @Override
            public String toName(String value) {
                return value;
            }
            @Override
            public String fromInt(int representative) {
                return strings[representative];
            }
            @Override
            public int toInt(String name) {
                for(int i = 0; i < strings.length; i++) {
                    if(strings[i].contentEquals(name)) {
                        return i;
                    }
                }
                return -1;
            }
        };
    }
    public static Enumerable<Integer> fromIntegerRange(int sizeOfRange) {
        return new Enumerable<Integer>() {
            @Override
            public int size() { return sizeOfRange; }
            @Override
            public Integer fromName(String name) { return Integer.parseInt(name); }
            @Override
            public String toName(Integer value) { return value.toString(); }
            @Override
            public Integer fromInt(int representative) { return representative; }
            @Override
            public int toInt(Integer name) { return name; }
        };
    }
    public abstract int size();
    public abstract T fromName(String name);
    public abstract String toName(T value);
    public abstract T fromInt(int representative);
    public abstract int toInt(T name);
    /**
     * Retrieve the names of the constructors.
     * @return 
     */
    public default String[] names() {
        int size = this.size();
        String[] res = new String[size];
        for(int i = 0; i < size; i++) {
            res[i] = this.toName(this.fromInt(i));
        }
        return res;
    }
    
}
