package Operators;
import Var.Tuple;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Distinctor extends Operator{
	Operator source;
	Tuple curTuple;
	List<SelectItem> onItems;
	int[] ItemPos;
	public Distinctor(Operator source, Distinct distinct) {
		this.source = source;
		this.Schema = source.getSchema();
		this.onItems = distinct.getOnSelectItems();
		if(onItems != null) {
			ItemPos = new int[onItems.size()];
			for(int i = 0; i < ItemPos.length; i++) {
				for(int j = 1; j < Schema.size(); j++) {
					if(onItems.get(i).toString().equals(Schema.get(j))) {
						ItemPos[i] = j - 1;
						break;
					}		
				}
			}
		}
		else {
			ItemPos = new int[this.Schema.size()-1];
			for(int i = 0; i < ItemPos.length; i++) {
				ItemPos[i] = i;
			}
		}
		try {
			this.curTuple = this.source.getNextTuple();
		}
		catch(Exception e) {
			System.out.println("Table is empty!");
		}
	}
	public Tuple getNextTuple() throws IOException{
		while(true) {
			try {
				Tuple nextTuple = this.source.getNextTuple();
				if(nextTuple.size() == 0) {
					break;
				}
				if(this.curTuple.size() == nextTuple.size()) {
					boolean flag = true;
					for(int i = 0; i < onItems.size(); i++) {
						if (curTuple.get(ItemPos[i]) != nextTuple.get(ItemPos[i])) {
							flag = false;
							break;
						}
					}
					if(!flag) {
						this.curTuple = nextTuple;
						return nextTuple;
					}
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
				writer.write(String.join(",", tuple.get())+"\n");
			}
			writer.close();
		}
		catch(Exception e) {
			
		}
		return new PlainReader(path, Schema);
	}

}
