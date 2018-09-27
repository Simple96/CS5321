import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Operator {
	protected List<String> Schema;
	public List<String> getNextTuple() throws IOException{
		return Arrays.asList(new String[0]);
	}
	public void reset() throws FileNotFoundException {
		
	}
	public List<String> getSchema() {
		return Schema;
	}
}
