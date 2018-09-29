package Var;

import java.util.List;
import java.lang.String;

public class Tuple {
    private List <String> tuple;

    public void Tuple(List<String> input_tuple){
        this.tuple = input_tuple;
    }

    public String get_element(int index){
        return tuple.get(index);
    }

    public int length(){
        return tuple.size();
    }

}
