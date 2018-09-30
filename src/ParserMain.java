import java.util.List;

import Var.Tuple;

import java.io.FileReader;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;


public class ParserMain {
	private static final String queriesFile = "queries.sql";
	public static void main(String[] args) {
		try {			
			CCJSqlParser parser = new CCJSqlParser(new FileReader(queriesFile));
			Statement statement;
			SchemaTable schematable = new SchemaTable();
			while ((statement = parser.Statement()) != null) {
				System.out.println("Read statement: " + statement);
				Select select = (Select) statement;
				PlainSelect plain = (PlainSelect) select.getSelectBody();
				List<SelectItem> selectitems = plain.getSelectItems();
				FromItem fromItem = plain.getFromItem();
				List<Join> joinList = plain.getJoins(); 
				Expression where_clause = plain.getWhere();
				if(joinList == null) {
					String s = fromItem.toString();
					List<String> Schema = schematable.getSchema(s);
					String path = schematable.getPath(s);
					PlainReader reader = new PlainReader(path, Schema);
					Selector selector = new Selector(reader, where_clause, selectitems);
					Tuple t = selector.getNextTuple();
					System.out.println(t.get());
				}
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
	}
}