package Operators;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sf.jsqlparser.statement.select.OrderByElement;
import Var.Tuple;

public class Sorter extends Operator{
	PlainReader source;
	PlainReader reader;
	private class ItemComparator implements Comparator<Tuple> {
		int[] compare_order;
		String s;
	    public ItemComparator(List<String> Schema, List<OrderByElement> orderbyItems){
	    	compare_order = new int[orderbyItems.size()];
	    	for(int i = 0; i < orderbyItems.size(); i++) {
	    		s = orderbyItems.get(i).getExpression().toString();
	    		for(int j = 1; j < Schema.size(); j++) {
	    			String[] sa = Schema.get(j).split("\\.");
	    			if(s.equals(Schema.get(j)) | (sa.length > 1 & s.equals(sa[1].toString()))) {
	    				compare_order[i] = j - 1;
	    			}
	    		}	    			
	    	}
	    	System.out.println(compare_order[0]);
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
		this.Schema = source.getSchema();
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
		this.source = new PlainReader(path, source.getSchema());
		//dump tuples into original file
	}
	
	
	public Tuple getNextTuple() throws IOException{
		return this.source.getNextTuple();
	}

	
	public void reset() throws IOException {
		this.source.reset();
	}
	
	
	public PlainReader Dump(String path) {
		return this.source;
	}
}
