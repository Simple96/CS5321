package Operators;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
		String s;
	    public ItemComparator(List<String> Schema, List<OrderByElement> orderbyItems){
	    	compare_order = new int[orderbyItems.size()];
	    	for(int i = 0; i < orderbyItems.size(); i++) {
	    		s = orderbyItems.get(i).getExpression().toString();
	    		for(int j = 1; j < Schema.size(); j++) {
	    			if(s.equals(Schema.get(j))) 
	    				compare_order[i] = j - 1;
	    		}	    			
	    	}
	    }
	    public int compare(Tuple a, Tuple b) {
	    	for(int i = 0; i < compare_order.length; i++) {
	    		long l1 = Long.parseLong(a.get(compare_order[i]));
	    		long l2 = Long.parseLong(b.get(compare_order[i]));
	    		if(l1 < l2) {
	    			return -1;
	    		}
	    	}
	    	return 1;
	    }
	}
	
	
	public Sorter(Operator source, List<OrderByElement> orderbyItems, String path) throws IOException {
		List<Tuple> tuples = new ArrayList<Tuple>();
		source.reset();
		Tuple t;
		while(true) {
			try {
				t = source.getNextTuple();
			}
			catch(Exception e) {
				//tuples.remove(tuples.size()-1);
				break;
			}
			tuples.add(t);
		}
		
		ItemComparator comparator = new ItemComparator(source.getSchema(), orderbyItems);
		
		Collections.sort(tuples, comparator);
		FileWriter writer = new FileWriter(path);
		for(Tuple tuple: tuples) {
			//System.out.println(String.join(" ", tuple.get()));
			writer.write(String.join(" ", tuple.get())+"\n");
		}
		writer.close();
		this.reader = new PlainReader(path, source.getSchema());
		//dump tuples into original file
	}
	
	
	public Tuple getNextTuple() throws IOException{
		return this.reader.getNextTuple();
	}

	
	public void reset() throws FileNotFoundException {
		this.reader.reset();
	}
}
