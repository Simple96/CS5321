import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class Distinctor extends Operator{
	Operator source;
	List<String> curTuple = Arrays.asList(new String[0]);
	public Distinctor(Operator source) {
		this.source = source;
		this.Schema = source.getSchema();
		try {
			this.curTuple = this.source.getNextTuple();
		}
		catch(Exception e) {
			System.out.println("Table is empty!");
		}
	}
	public List<String> getNextTuple() throws IOException{
		while(true) {
			try {
				List<String> nextTuple = this.source.getNextTuple();
				if(nextTuple.size() == 0) {
					break;
				}
				if(this.curTuple.size() == nextTuple.size()) {
					boolean flag = true;
					for(int i = 0; i < nextTuple.size(); i++) {
						if (curTuple.get(i) != nextTuple.get(i)) {
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
		return Arrays.asList(new String[0]);
	}
	public void reset() throws FileNotFoundException {
		this.source.reset();
	}

}
