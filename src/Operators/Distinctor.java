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
	int[] ItemPos;
	public Distinctor(Operator source, Distinct distinct) {
		this.source = source;
		this.Schema = source.getSchema();
		List<SelectItem> onItems = distinct.getOnSelectItems();
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

	}
	public Tuple getNextTuple() throws IOException{
		if(this.curTuple == null) {
			try {
				this.curTuple = this.source.getNextTuple();
				return this.curTuple;
			}
			catch(Exception e) {
				throw new IOException();
			}
		}
		while(true) {
			try {
				Tuple nextTuple = this.source.getNextTuple();
				if(nextTuple.size() == 0) {
					break;
				}
				if(this.curTuple.size() == nextTuple.size()) {
					boolean flag = true;
					for(int i = 0; i < ItemPos.length; i++) {
						if (!curTuple.get(ItemPos[i]).equals(nextTuple.get(ItemPos[i]))) {
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
	

}
