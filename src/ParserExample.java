import java.io.FileReader;

import com.sun.tools.example.debug.expr.ExpressionParser;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.util.deparser.*;

/**
 * Example class for getting started with JSQLParser. Reads SQL statements from
 * a file and prints them to screen; then extracts SelectBody from each query
 * and also prints it to screen.
 *
 * @author Lucja Kot
 */
public class ParserExample {

    private static final String queriesFile = "./samples/input/queries.sql";

    public static void main(String[] args) {
        try {
            CCJSqlParser parser = new CCJSqlParser(new FileReader(queriesFile));
            Statement statement;
            while ((statement = parser.Statement()) != null) {
                System.out.println("Read statement: " + statement);
                Select select = (Select) statement;
                System.out.println("Select body is " + select.getSelectBody());

                PlainSelect plainselect = (PlainSelect)select.getSelectBody();
                System.out.println("From item is " + plainselect.getFromItem());
                System.out.println("Joins is " + plainselect.getJoins());

                Expression expression = plainselect.getWhere();

                //ExpressionVisitor visitor = new

            }
        } catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
    }
}