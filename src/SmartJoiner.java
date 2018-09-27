import net.sf.jsqlparser.expression.Expression;

public class SmartJoiner extends Operator{
	Operator[] sources;
	Expression on_clause;
	Expression where_clause;
}
