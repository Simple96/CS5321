package Operators;
import Var.Tuple;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Operator {
	protected List<String> Schema = new ArrayList<String>();
	public Tuple getNextTuple() throws IOException{
		return new Tuple(Arrays.asList(new String[0]));
	}

	public void reset() throws FileNotFoundException {
		
	}
	public List<String> getSchema() {
		return Schema;
	}
}
