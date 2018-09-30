import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class SchemaTable {
	private static final String tableSchemas = "./db/schema.txt";
	private static final String tablePath = "./db/data";
	List<String[]> Schemas;
	BufferedReader reader;
	class tableEntry {
		List<String> Schema;
		String path;
		public tableEntry(List<String> Schema, String path) {
			this.Schema = Schema;
			this.path = path;
		}
		public List<String> getSchema(){
			return this.Schema;
		}
		public String getPath() {
			return this.path;
		}
	}
	Hashtable<String, tableEntry> hashtable = new Hashtable<String, tableEntry>();
	
	public SchemaTable() throws FileNotFoundException {
		this.reader = new BufferedReader(new FileReader(tableSchemas));
		while(true) {
			try {
				List<String> Schema = Arrays.asList(this.reader.readLine().split(" "));
				String path = tablePath + File.separatorChar + Schema.get(0);
				this.hashtable.put(Schema.get(0), new tableEntry(Schema, path));
			}
			catch(Exception e) {
				break;
			}
		}
	}
	
	public List<String> getSchema(String s){
		tableEntry a = hashtable.get(s);
		return a.getSchema();
	}
	
	public String getPath(String s) {
		return hashtable.get(s).getPath();
	}
}
