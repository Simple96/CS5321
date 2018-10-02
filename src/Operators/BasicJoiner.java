package Operators;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import Var.Tuple;
import Visitors.JoinExpressionVisitor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.SelectItem;

public class BasicJoiner extends Operator{
	Operator source1;
	Operator source2;
	List<String> Schema1;
	List<String> Schema2;
	Expression on_clause;
	int Join_Type;
	Tuple Nulltuple;
	Tuple tuple1;
	boolean matched = false;
	int[][] ItemPos;
	JoinExpressionVisitor visitor;
	private Tuple subDump(Tuple tuple1, Tuple tuple2) {
		List<String> content = new ArrayList<String>();
		for(int i = 0; i < ItemPos.length; i++) {
			if(ItemPos[i][0] == 1)
				content.add(tuple1.get(ItemPos[i][1]));
			else
				content.add(tuple2.get(ItemPos[i][1]));
		}
		return new Tuple(content);
	}
	
	public BasicJoiner(Operator source1, Operator source2, Expression on_clause, int Join_Type, List<SelectItem> selectitems, String alias) {
		this.Schema.add(alias);
		String[] Item;		
		this.Schema1 = source1.getSchema();
		this.Schema2 = source2.getSchema();
		System.out.println(Schema1);
		System.out.println(Schema2);
		if (selectitems.get(0).toString() != "*") {
			this.ItemPos = new int[selectitems.size()][2];
			for (int i = 0; i < selectitems.size(); i++) {
				Item = selectitems.get(i).toString().split("\\.");
				if(Item.length > 2) {
					String column_name = Item[1] + "." + Item[2];
					this.Schema.add(column_name);
				}
				else if(Item.length > 1){
					Schema.add(Item[0] + "." + Item[1]);
					if(Item[0].equals(this.Schema1.get(0))) {
						String column_name = Item[1];
						for (int j = 1; j < this.Schema1.size(); j++) {
							if (this.Schema1.get(j).equals(column_name)) {
								this.ItemPos[i][0] = 1;
								this.ItemPos[i][1] = j - 1;
							}
						}
					}
					else if(Item[0].equals(this.Schema2.get(0))){
						String column_name = Item[1];
						for (int j = 1; j < this.Schema2.size(); j++) {
							if (this.Schema2.get(j).equals(column_name)) {
								this.ItemPos[i][0] = 2;
								this.ItemPos[i][1] = j - 1;
							}
						}
					}
					else {
						String column_name = Item[0] + Item[1];
						for (int j = 1; j < this.Schema1.size(); j++) {
							if (this.Schema1.get(j).equals(column_name)) {
								this.ItemPos[i][0] = 1;
								this.ItemPos[i][1] = j - 1;
							}
						}
						for (int j = 1; j < this.Schema2.size(); j++) {
							if (this.Schema2.get(j).equals(column_name)) {
								this.ItemPos[i][0] = 2;
								this.ItemPos[i][1] = j - 1;
							}
						}
					}
				}
				else {
					String column_name = Item[0];
					for (int j = 1; j < this.Schema1.size(); j++) {
						if (this.Schema1.get(j).equals(column_name) | this.Schema1.get(j).split("\\.")[1].equals(column_name)) {
							this.ItemPos[i][0] = 1;
							this.ItemPos[i][1] = j - 1;
						}
					}
					for (int j = 1; j < this.Schema2.size(); j++) {
						if (this.Schema2.get(j).equals(column_name) | this.Schema2.get(j).split("\\.")[1].equals(column_name)) {
							this.ItemPos[i][0] = 2;
							this.ItemPos[i][1] = j - 1;
						}
					}
				}
			}
		}
		else {
			this.ItemPos = new int[this.Schema1.size()+this.Schema2.size()-2][2];
			int offset = this.Schema1.size()-1;
			if(this.Schema1.get(1).toString().split(".").length < 2)
				for(int i = 1; i < this.Schema1.size(); i++) {
					this.ItemPos[i-1][0] = 1;
					this.ItemPos[i-1][1] = i - 1;
					this.Schema.add(this.Schema1.get(0) + "." + this.Schema1.get(i));
				}
			else
				for(int i = 1; i < this.Schema1.size(); i++) {
					this.ItemPos[i-1][0] = 1;
					this.ItemPos[i-1][1] = i - 1;
					this.Schema.add(this.Schema1.get(i));
				}
			if(this.Schema2.get(1).toString().split(".").length < 2)
				for(int i = 1; i < this.Schema2.size(); i++) {
					this.ItemPos[i-1+offset][0] = 2;
					this.ItemPos[i-1+offset][1] = i - 1;
					this.Schema.add(this.Schema2.get(0) + "." + this.Schema2.get(i));
				}
			else
				for(int i = 1; i < this.Schema2.size(); i++) {
					this.ItemPos[i-1+offset][0] = 2;
					this.ItemPos[i-1+offset][1] = i - 1;
					this.Schema.add(this.Schema2.get(i));
				}
		}
		//0: INNER
		//1: LEFT
		//2: RIGHT
		if(Join_Type != 2) {
			this.source1 = source1;
			this.source2 = source2;
			visitor = new JoinExpressionVisitor(source1.getSchema(), source2.getSchema());
		}
		else {
			this.source1 = source2;
			this.source2 = source1;
			visitor = new JoinExpressionVisitor(source2.getSchema(), source1.getSchema());
		}
		this.on_clause = on_clause;
		this.Join_Type = Join_Type;
		
		try {
			this.tuple1 = this.source1.getNextTuple();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] nulltuple = new String[this.source2.getSchema().size()];
		for(int i = 0;i < this.source2.getSchema().size();i++) {
			nulltuple[i] = "null";
		}
		String path = "db/tempTable/"+Integer.toString(ThreadLocalRandom.current().nextInt(0, 99999));
		this.source2 = this.source2.Dump(path);
		this.Nulltuple = new Tuple(Arrays.asList(nulltuple));

		//System.out.println(source1.getSchema());
		//System.out.println(source2.getSchema());
		//System.out.println(this.Schema);
		
	}
	
	
	public BasicJoiner(Operator source1, Operator source2, Expression on_clause, int Join_Type, List<SelectItem> selectitems) {
		this(source1, source2, on_clause, Join_Type, selectitems, "null");
	}
	
	
	public Tuple getNextTuple() throws IOException{
		Tuple result = new Tuple(Arrays.asList(new String[0]));
		while(true) {
			Tuple tuple2 = new Tuple(Arrays.asList(new String[0]));
			try {
				tuple2 = this.source2.getNextTuple();
				//System.out.println(tuple1.get());
				//System.out.println(tuple2.get());
				//On conditions go here!
				visitor.set(tuple1, tuple2);
				on_clause.accept(visitor);
				if(visitor.getStatus()) {
					this.matched = true;
					if(Join_Type != 2) {
						result = subDump(tuple1, tuple2);
					}
					else {
						result = subDump(tuple2, tuple1);
					}
					return result;
				}
			}
			catch(Exception e2) {
				try {
					if(this.matched == false) {
						this.matched = true;
						if(Join_Type == 1) {
							return subDump(tuple1, Nulltuple);
						}
						else if(Join_Type == 2) {
							return subDump(Nulltuple, tuple1);
						}
					}
					this.tuple1 = this.source1.getNextTuple();
					this.source2.reset();
					this.matched = false;
				}
				catch(IOException e1) {
					break;
				}
			}	
		}
		throw new IOException();
	}
	public void reset() throws IOException {
		this.source1.reset();
		this.source2.reset();
		try {
			this.tuple1 = this.source1.getNextTuple();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
