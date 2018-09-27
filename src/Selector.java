import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.SelectItem;

public class Selector extends Operator{
	Operator source;
	Expression where_clause;
	int[] ItemPos;
	
	public Selector(Operator source, Expression where_clause, List<SelectItem> selectitems, String alias) {
		this.source = source;
		this.where_clause = where_clause;
		this.Schema.add(alias);
		List<String> sourceSchema = source.getSchema();
		if (selectitems.get(0).toString() != "*") {
			this.ItemPos = new int[selectitems.size()];
			for (int i = 0; i < selectitems.size(); i++) {
				Schema.add(selectitems.get(i).toString());
				for (int j = 0; j < sourceSchema.size(); j++) {
					if (sourceSchema.get(j) == Schema.get(i)) {
						this.ItemPos[i] = j;
					}
				}
			}
		}
		else {
			this.ItemPos = new int[sourceSchema.size()];
			this.Schema.addAll(sourceSchema);
			for(int i = 0; i < ItemPos.length; i++) {
				ItemPos[i] = i;
			}
		}
	}
	
	public Selector(Operator source, Expression where_clause, List<SelectItem> selectitems) {
		this(source, where_clause, selectitems, "null");
	}
	

	public List<String> getNextTuple() throws IOException{
		List<String> result = Arrays.asList(new String[0]);
		while(true) {
			try {
				List<String> tuple = source.getNextTuple();
				if(tuple.size() == 0) {
					break;
				}
				boolean cond = false;
				//Conditions go here!
				if(cond) {
					for(int i = 0; i < this.ItemPos.length; i++)
						result.add(tuple.get(ItemPos[i]));
					return result;
				}
					
			}
			catch(Exception e) {
				break;
			}
		}
		return result;
	}
	public void reset() throws FileNotFoundException {
		this.source.reset();
	}
}
