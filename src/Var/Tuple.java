package Var;

import java.util.List;
import java.lang.String;

public class Tuple {
    private List <String> tuple;

    public Tuple(List<String> input_tuple){
        this.tuple = input_tuple;
    }

    public String get(int index){
        return tuple.get(index);
    }
    
    public List<String> get() {
    	return this.tuple;
    }
    
    public int size() {
    	return tuple.size();
    }

    public int length(){
        return tuple.size();
    }
    
    public void add(Tuple tuple) {
    	this.tuple.addAll(tuple.get());
    }
    
    public void set(int index, String s) {
    	this.tuple.set(index, s);
    }

}
