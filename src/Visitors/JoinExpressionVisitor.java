package Visitors;

import Var.Tuple;
import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.InverseExpression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.util.List;


public class JoinExpressionVisitor implements ExpressionVisitor {
    private Tuple tp1 = null, tp2 = null;
    private List<String> sche1, sche2;
    private long Num;
    private boolean status = true;

    Expression conjunct_expression;

    public JoinExpressionVisitor(List<String> sc1, List<String> sc2){
        this.sche1 = sc1;
        this.sche2 = sc2;
    }

    private void setTp1(Tuple tp1_in){
        this.tp1 = tp1_in;
    }

    private void setTp2(Tuple tp2_in){
        this.tp2 = tp2_in;
    }

    public void set(Tuple tp1_in, Tuple tp2_in){
        setTp1(tp1_in);
        setTp2(tp2_in);
    }

    private void setSche1(List<String> sche1){
        this.sche1 = sche1;
    }

    private void setSche2(List<String> sche2){
        this.sche2 = sche2;
    }

    public void set(List<String> sche1, List<String> sche2){
        setSche1(sche1);
        setSche2(sche2);
    }

    public boolean getStatus() {
    	return status;
    }
    
    @Override
    public void visit(Column column_exit) {
        String col_name = column_exit.getColumnName();
        int index1 = -1, index2 = -1;
        index1 = sche1.indexOf(col_name) - 1;
        String table_name = column_exit.getTable().getName();
        String sche_name1 = sche1.get(0);
        String sche_name2 = sche2.get(0);
        //System.out.println(table_name);
        //System.out.println(sche_name1);
        if (sche_name1.equals(table_name)) this.Num = Long.parseLong(tp1.get(index1));
        else{
            index2 = sche2.indexOf(col_name) - 1;
            if (!sche_name2.equals(table_name)) {
                System.out.print("Class JoinExpressionVisitor: schema input wrong");
            }
            else this.Num = Long.parseLong(tp2.get(index2));
        }
        //System.out.printf("visitor pattern exit, find value: ");
        //System.out.println(Num);
    }

    @Override
    public void visit(LongValue value_exit) {
        this.Num = value_exit.getValue();
        //System.out.printf("visitor pattern exit, find longValue: ");
        //System.out.println(this.Num);
    }

    @Override
    public void visit(AndExpression andExpression) {
        andExpression.getLeftExpression().accept(this);
        boolean left_status = status;
        andExpression.getRightExpression().accept(this);
        boolean right_status = status;
        status = left_status && right_status;
    }

    @Override
    public void visit(EqualsTo equalsTo) {
        equalsTo.getLeftExpression().accept(this);
        long left_Num = this.Num;
        equalsTo.getRightExpression().accept(this);
        long right_Num = this.Num;
        status = left_Num == right_Num;
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        notEqualsTo.getLeftExpression().accept(this);
        long left_Num = this.Num;
        notEqualsTo.getRightExpression().accept(this);
        long right_Num = this.Num;
        status = !(left_Num == right_Num);
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        greaterThan.getLeftExpression().accept(this);
        long left_Num = this.Num;
        greaterThan.getRightExpression().accept(this);
        long right_Num = this.Num;
        status = left_Num > right_Num;
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        greaterThanEquals.getLeftExpression().accept(this);
        long left_Num = this.Num;
        greaterThanEquals.getRightExpression().accept(this);
        long right_Num = this.Num;
        status = left_Num >= right_Num;
    }

    @Override
    public void visit(MinorThan minorThan) {
        minorThan.getLeftExpression().accept(this);
        long left_Num = this.Num;
        minorThan.getRightExpression().accept(this);
        long right_Num = this.Num;
        status = left_Num < right_Num;
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        minorThanEquals.getLeftExpression().accept(this);
        long left_Num = this.Num;
        minorThanEquals.getRightExpression().accept(this);
        long right_Num = this.Num;
        status = left_Num <= right_Num;
    }

	@Override
	public void visit(NullValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Function arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(InverseExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(JdbcParameter arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DoubleValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DateValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TimeValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(TimestampValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Parenthesis arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StringValue arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Addition arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Division arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Multiplication arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Subtraction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(OrExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Between arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(InExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IsNullExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LikeExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SubSelect arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CaseExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(WhenClause arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExistsExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AllComparisonExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AnyComparisonExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Concat arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Matches arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BitwiseAnd arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BitwiseOr arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BitwiseXor arg0) {
		// TODO Auto-generated method stub
		
	}

}
