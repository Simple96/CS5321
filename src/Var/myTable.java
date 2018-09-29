package Var;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import net.sf.jsqlparser.schema.Table;
import java.io.IOException;

public class myTable extends Table{
    public List<String> schema = null;
    private BufferedReader bufferedReader= null;

    public Tuple nextTuple(){
        try{
            String nextline = bufferedReader.readLine();
            if (nextline == null) return null;
            String [] elements = nextline.split(",");
            int length = schema.size();
            int []cols = new int [length];

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public myTable(String name, List<String> schema, BufferedReader br) {
        this.setName(name);
        this.schema = schema;
        this.bufferedReader = br;
    }
}

