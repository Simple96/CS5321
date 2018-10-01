package Operators;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sf.jsqlparser.statement.select.OrderByElement;
import Var.Tuple;

public class Sorter extends Operator{
	Operator source;
	PlainReader reader;
	private class ItemComparator implements Comparator<Tuple> {
		int[] compare_order;
	    public ItemComparator(List<String> Schema, List<OrderByElement> orderbyItems){
	    	//to be implement
	    }
	    public int compare(Tuple a, Tuple b) {
	    	//to be implement
	        return 1;
	    }
	}
	
	
	public Sorter(Operator source, List<OrderByElement> orderbyItems) {
		List<Tuple> tuples = new ArrayList<Tuple>();
		while(true) {
			try {
				tuples.add(source.getNextTuple());
			}
			catch(Exception e) {
				break;
			}
		}
		ItemComparator comparator = new ItemComparator(source.getSchema(), orderbyItems);
		Collections.sort(tuples, comparator);
		//dump tuples into a new file
	}
	
	
	public Tuple getNextTuple() throws IOException{
		return this.reader.getNextTuple();
	}

	
	public void reset() throws FileNotFoundException {
		this.reader.reset();
	}
}
