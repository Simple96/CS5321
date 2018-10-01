package DB_Main;
import Operators.*;
import java.util.List;

import Var.Tuple;

import java.io.FileReader;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.OrderByElement;


public class ParserMain {
	private static final String queriesFile = "queries.sql";
	private static final String tempPath = "db/tempTable";
	public static void main(String[] args) {
		List<String> Schema;
		List<String> Schema2;
		String path;
		String path2;
		Tuple t;
		try {			
			CCJSqlParser parser = new CCJSqlParser(new FileReader(queriesFile));
			Statement statement;
			SchemaTable schematable = new SchemaTable();
			while ((statement = parser.Statement()) != null) {
				System.out.println("Read statement: " + statement);
				Select select = (Select) statement;
				PlainSelect plain = (PlainSelect) select.getSelectBody();	
				List<SelectItem> selectitems = plain.getSelectItems();
				Distinct distinct = plain.getDistinct();
				FromItem fromItem = plain.getFromItem();
				List<Join> joinList = plain.getJoins(); 
				Expression where_clause = plain.getWhere();
				String firstItem = fromItem.toString();
				List<OrderByElement> orderbyList = plain.getOrderByElements();
				Schema = schematable.getSchema(firstItem);
				path = schematable.getPath(firstItem);
				PlainReader reader = new PlainReader(path, Schema);
				if(joinList == null) {	
					Selector selector = new Selector(reader, where_clause, selectitems);
				}
				else if(joinList.size() == 1) {
					Join joinItem = joinList.get(0);
					String secondItem = joinItem.getRightItem().toString();
					Schema2 = schematable.getSchema(secondItem);
					path2 = schematable.getPath(secondItem);
					PlainReader reader2 = new PlainReader(path2, Schema2);
					int JoinType = 0;
					if(joinItem.isLeft())
						JoinType = 1;
					else if(joinItem.isRight())
						JoinType = 2;
					System.out.println(JoinType);
					BasicJoiner joiner = new BasicJoiner(reader, reader2, where_clause, JoinType, selectitems);	
					
				}
				if(orderbyList != null) {
					Sorter sorter = new Sorter(reader, orderbyList, tempPath);
				}
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
	}
}