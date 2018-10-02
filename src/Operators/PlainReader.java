package Operators;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Var.Tuple;
public class PlainReader extends Operator {
	BufferedReader reader;
	String path;
	public PlainReader(String path, List<String> Schema, String Alias) {
		this.path = path;
		try {
			this.reader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.Schema = new ArrayList<>(Schema);
		this.Schema.set(0, Alias);
	}
	
	public PlainReader(String path, List<String> Schema) {
		this(path, Schema, Schema.get(0));
	}

	
	public Tuple getNextTuple() throws IOException{
		return new Tuple(Arrays.asList(this.reader.readLine().split(",")));
	}
	
	
	public void reset() throws FileNotFoundException {
		this.reader = new BufferedReader(new FileReader(this.path));
	}
	
}