package Operators;
import java.io.IOException;

import Var.Tuple;
import net.sf.jsqlparser.expression.Expression;

public class SmartJoiner extends Operator{
	Operator[] sources;
	Expression on_clause;
	Expression where_clause;
	@Override
	public Tuple getNextTuple() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void reset() throws IOException {
		// TODO Auto-generated method stub
		
	}
}
