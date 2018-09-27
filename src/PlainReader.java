import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
public class PlainReader extends Operator {
	BufferedReader reader;
	String path;
	public PlainReader(String path, List<String> Schema) throws IOException {
		this.reader = new BufferedReader(new FileReader(path));
		this.Schema = Schema;
	}
	public List<String> getNextTuple() throws IOException{
		return Arrays.asList(this.reader.readLine().split(","));
	}
	public void reset() throws FileNotFoundException {
		this.reader = new BufferedReader(new FileReader(this.path));
	}
}