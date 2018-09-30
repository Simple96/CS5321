package Visitors;
import Var.Tuple;
import net.sf.jsqlparser.expression.*;
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
//import net.sf.jsqlparser.expression.operators.conditional.AndExpression;


public class SelectionExpressionVisitor implements ExpressionVisitor {
    protected long Num = 0;
    protected boolean status = false;
    //protected Tuple current_tuple= null;
    protected List<String> schema;
    protected Tuple tuple= null;

    //protected

    public SelectionExpressionVisitor() {
    	
    }
    
    public long getNum() {
        return Num;
    }

    public void set(Tuple input_tuple){
        this.tuple = input_tuple;
    }
    
    public void set(List<String> Schema){
        this.schema = Schema;
    }
    
    public void set(Tuple input_tuple, List<String> Schema){
    	this.tuple = input_tuple;
        this.schema = Schema;
    }
    //public void setStatus(boolean status) {
    //    this.status = status;
    //}

    public boolean getStatus(){
        return this.status;
    }

    @Override
    public void visit(Column column_exit) {
        String col_name = column_exit.getColumnName();
        //String whole_col_name = column_exit.getWholeColumnName();
        int index = schema.indexOf(col_name) - 1;
        if (index == -1) {System.out.print("Class SelectionExpressionVisitor: schema input wrong");}
        this.Num = Long.parseLong(tuple.get(index));
        System.out.printf("visitor pattern exit, find Column Number: ");
        System.out.println(this.Num);
    }

    @Override
    public void visit(LongValue value_exit) {
        this.Num = value_exit.getValue();
        System.out.printf("visitor pattern exit, find longValue: ");
        System.out.println(this.Num);
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

