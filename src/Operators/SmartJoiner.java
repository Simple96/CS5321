package Operators;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Var.Tuple;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.SelectItem;

public class SmartJoiner extends Operator{
	Operator[] sources;
	Expression on_clause;
	Expression where_clause;
	PlainReader[] readers;
	
	public SmartJoiner(Operator[] sources, Expression on_clause, List<SelectItem> selectitems, String alias) {
		
	}
	public Tuple getNextTuple() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void reset() throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
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
				writer.write(String.join(" ", tuple.get())+"\n");
			}
			writer.close();
		}
		catch(Exception e) {
			
		}
		return new PlainReader(path, Schema);
	}
}
