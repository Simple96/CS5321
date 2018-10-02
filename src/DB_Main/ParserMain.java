package DB_Main;
import Operators.*;
import java.util.List;

import Var.SchemaTable;
import Var.Tuple;

import java.io.FileReader;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.schema.Table;
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
				Table fromItem = (Table) plain.getFromItem();
				Table joinItem;
				List<Join> joinList = plain.getJoins(); 
				Expression where_clause = plain.getWhere();
				String firstItem;
				String secondItem;
				String Alias;
				firstItem = fromItem.getName().toString();
				List<OrderByElement> orderbyList = plain.getOrderByElements();
				Schema = schematable.getSchema(firstItem);
				path = schematable.getPath(firstItem);
				Operator Cur;
				
				if(fromItem.getAlias() != null) {
					Alias = fromItem.getAlias().toString();
					Cur = new PlainReader(path, Schema, Alias);
				}
				else 
					Cur = new PlainReader(path, Schema);
				
				if(joinList == null) 
					Cur = new Selector(Cur, where_clause, selectitems);

				else {
					joinItem = (Table) joinList.get(0).getRightItem();
					boolean isLeft = joinList.get(0).isLeft();
					boolean isRight = joinList.get(0).isRight();
					secondItem = joinItem.getName().toString();
					Schema2 = schematable.getSchema(secondItem);
					path2 = schematable.getPath(secondItem);
					int JoinType = 0;
					if(isLeft)
						JoinType = 1;
					else if(isRight)
						JoinType = 2;
					if(joinItem.getAlias() != null) {
						Alias = joinItem.getAlias().toString();
						PlainReader reader2 = new PlainReader(path2, Schema2, Alias);
						Cur = new BasicJoiner(Cur, reader2, where_clause, JoinType, selectitems);
					}
					else {
						PlainReader reader2 = new PlainReader(path2, Schema2);
						Cur = new BasicJoiner(Cur, reader2, where_clause, JoinType, selectitems);
					}
					Cur = Cur.Dump("db/tempTables/tempTable2");
				}
				if(orderbyList != null) {
					Cur = new Sorter(Cur, orderbyList, "db/tempTables/tempTable1");
				}
				if(distinct != null) {
					Cur = new Distinctor(Cur,distinct);
				}
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
	}
}