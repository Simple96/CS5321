package Visitors;

import net.sf.jsqlparser.schema.*;
import Var.Tuple;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;

import java.util.List;
//import net.sf.jsqlparser.expression.operators.conditional.AndExpression;


public abstract class SelectionExpressionVisitor implements ExpressionVisitor {
    protected long Num = 0;
    protected boolean status = false;
    //protected Tuple current_tuple= null;
    protected List<String> schema;
    protected Tuple tuple= null;

    //protected

    public long getNum() {
        return Num;
    }

    public void setTuple(Tuple input_tuple){
        this.tuple = input_tuple;
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
        String whole_col_name = column_exit.getWholeColumnName();
        int index = schema.indexOf(col_name);
        if (index == -1) {System.out.print("Class SelectionExpressionVisitor: schema input wrong");}
        this.Num = Long.parseLong(tuple.get_element(index));
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

}

