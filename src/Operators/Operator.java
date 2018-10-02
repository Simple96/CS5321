package Operators;
import Var.Tuple;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Operator {
	protected List<String> Schema = new ArrayList<String>();
	public abstract Tuple getNextTuple() throws IOException;

	public abstract void reset() throws IOException;
	
	public List<String> getSchema() {
		return Schema;
	}
	
	public PlainReader Dump(String path) {
		List<Tuple> tuples = new ArrayList<Tuple>();
		Tuple t;
		while(true) {
			try {
				t = this.getNextTuple();
			}
			catch(Exception e) {
				//tuples.remove(tuples.size()-1);
				break;
			}
			tuples.add(t);
		}
		try {
			FileWriter writer = new FileWriter(path);
			for(Tuple tuple: tuples) {
				writer.write(String.join(",", tuple.get())+"\n");
			}
			writer.close();
		}
		catch(Exception e) {
			
		}
		return new PlainReader(path, Schema);
	}
}
