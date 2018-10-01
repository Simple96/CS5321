package Operators;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import Var.Tuple;
public class PlainReader extends Operator {
	BufferedReader reader;
	String path;
	public PlainReader(String path, List<String> Schema) throws IOException {
		this.path = path;
		this.reader = new BufferedReader(new FileReader(path));
		this.Schema = Schema;
	}
	
	
	public Tuple getNextTuple() throws IOException{
		return new Tuple(Arrays.asList(this.reader.readLine().split(",")));
	}
	
	
	public void reset() throws FileNotFoundException {
		this.reader = new BufferedReader(new FileReader(this.path));
	}
}