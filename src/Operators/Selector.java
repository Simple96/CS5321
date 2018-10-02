package Operators;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Var.Tuple;
import Visitors.SelectionExpressionVisitor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.SelectItem;

public class Selector extends Operator{
	Operator source;
	Expression where_clause;
	int[] ItemPos;
	SelectionExpressionVisitor visitor = new SelectionExpressionVisitor();
	public Selector(Operator source, Expression where_clause, List<SelectItem> selectitems, String alias) {
		this.source = source;
		this.where_clause = where_clause;
		this.Schema.add(alias);
		List<String> sourceSchema = source.getSchema();
		if (selectitems.get(0).toString() != "*") {
			this.ItemPos = new int[selectitems.size()];
			for (int i = 0; i < selectitems.size(); i++) {
				this.Schema.add(selectitems.get(i).toString());
				for (int j = 1; j < sourceSchema.size(); j++) {
					if (sourceSchema.get(j).equals(Schema.get(i+1))) {
						ItemPos[i] = j - 1;
					}
				}
			}
		}
		else {
			this.ItemPos = new int[sourceSchema.size()-1];
			for(int i = 0; i < ItemPos.length; i++) {
				ItemPos[i] = i;
				this.Schema.add(sourceSchema.get(i + 1));
			}
		}
		visitor.set(sourceSchema);
	}
	
	
	public Selector(Operator source, Expression where_clause, List<SelectItem> selectitems) {
		this(source, where_clause, selectitems, "null");
	}
	

	public Tuple getNextTuple() throws IOException{
		Tuple result = new Tuple(Arrays.asList(new String[0]));
		while(true) {		
			try {
				Tuple tuple = source.getNextTuple();
				if(tuple.size() == 0) {
					break;
				}
				this.visitor.set(tuple);
				where_clause.accept(visitor);
				if(visitor.getStatus()) {
					result = new Tuple(Arrays.asList(new String[this.ItemPos.length]));
					for(int i = 0; i < this.ItemPos.length; i++) {
						result.set(i, tuple.get(ItemPos[i]));
					}
					return result;
				}
			}
			catch(Exception e) {
				
				break;
			}
		}
		
		throw new IOException();
	}
	
	
	public void reset() throws IOException {
		this.source.reset();
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
				writer.write(String.join(" ", tuple.get())+"\n");
			}
			writer.close();
		}
		catch(Exception e) {
			
		}
		return new PlainReader(path, Schema);
	}
}
