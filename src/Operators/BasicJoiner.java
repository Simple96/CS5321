package Operators;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	private Tuple Dump(Tuple tuple1, Tuple tuple2) {
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
		if (selectitems.get(0).toString() != "*") {
			this.ItemPos = new int[selectitems.size()][2];
			for (int i = 0; i < selectitems.size(); i++) {
				Item = selectitems.get(i).toString().split("\\.");
				if(Item.length > 2) {
					String column_name = Item[1] + '.' + Item[2];
					this.Schema.add(column_name);
				}
				else if(Item.length > 1){
					if(Item[0].equals(this.Schema1.get(0))) {
						String column_name = Item[1];
						for (int j = 1; j < this.Schema1.size(); j++) {
							if (this.Schema1.get(j).equals(column_name)) {
								this.ItemPos[i][0] = 1;
								this.ItemPos[i][1] = j - 1;
							}
						}
					}
					else if(Item[0].equals(this.Schema1.get(0))){
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
			//Initialize ItemPos
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
			this.tuple1 = source1.getNextTuple();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] nulltuple = new String[this.source2.getSchema().size()];
		for(int i = 0;i < this.source2.getSchema().size();i++) {
			nulltuple[i] = "null";
		}
		this.Nulltuple = new Tuple(Arrays.asList(nulltuple));

		System.out.println(source1.getSchema());
		System.out.println(source2.getSchema());
	}
	
	
	public BasicJoiner(Operator source1, Operator source2, Expression on_clause, int Join_Type, List<SelectItem> selectitems) {
		this(source1, source2, on_clause, Join_Type, selectitems, "null");
	}
	
	
	public Tuple getNextTuple() throws IOException{
		Tuple result = new Tuple(Arrays.asList(new String[0]));
		while(true) {
			if(tuple1.size() == 0) {
				break;
			}
			Tuple tuple2 = new Tuple(Arrays.asList(new String[0]));
			try {
				tuple2 = this.source2.getNextTuple();
				System.out.println(tuple1.get());
				System.out.println(tuple2.get());
				//On conditions go here!
				visitor.set(tuple1, tuple2);
				on_clause.accept(visitor);
				if(visitor.getStatus()) {
					this.matched = true;
					if(Join_Type != 2) {
						result = Dump(tuple1, tuple2);
					}
					else {
						result = Dump(tuple2, tuple1);
					}
					return result;
				}
			}
			catch(Exception e2) {
				try {
					if(this.matched == false) {
						if(Join_Type == 1) {
							result = Dump(tuple1, tuple2);
						}
						else if(Join_Type == 2) {
							result = Dump(tuple2, tuple1);
						}
					}
					this.source2.reset();
					this.tuple1 = this.source1.getNextTuple();
					this.matched = false;
				}
				catch(Exception e1) {
					break;
				}
			}	
		}
		throw new IOException();
	}
	public void reset() throws FileNotFoundException {
		this.source1.reset();
		this.source2.reset();
	}
	

}