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
	public abstract PlainReader Dump(String path);
	
}
