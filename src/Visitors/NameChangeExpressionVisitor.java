package Visitors;

import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;

public abstract class NameChangeExpressionVisitor implements ExpressionVisitor {
    String old_name1 = null;
    String old_name2 = null;
    String new_table_name = null;

    public NameChangeExpressionVisitor(String old_name1, String old_name2, String new_name){
        this.old_name1 = old_name1;
        this.old_name2 = old_name2;
        this.new_table_name = new_name;
    }

    public void set_old_name1(String old1){
        this.old_name1 = old1;
    }

    public void set_old_name2(String old2){
        this.old_name2 = old2;
    }

    public void set_new_name(String newname_in){
        this.new_table_name = newname_in;
    }

    @Override
    public void visit(Column column_exit) {
        String new_colunm_name = column_exit.toString();
        String old_colunm_name = column_exit.getColumnName().toString();
        if(old_colunm_name.equals(old_name1) || (old_colunm_name.equals(old_name2))){
            column_exit.setColumnName(new_colunm_name);
            column_exit.getTable().setName(new_table_name);
        }
    }

    @Override
    public void visit(LongValue value_exit) {
        return;
    }

    @Override
    public void visit(AndExpression andExpression) {
        andExpression.getLeftExpression().accept(this);
        andExpression.getRightExpression().accept(this);
    }

    @Override
    public void visit(EqualsTo equalsTo) {
        equalsTo.getLeftExpression().accept(this);
        equalsTo.getRightExpression().accept(this);
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        notEqualsTo.getLeftExpression().accept(this);
        notEqualsTo.getRightExpression().accept(this);
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        greaterThan.getLeftExpression().accept(this);
        greaterThan.getRightExpression().accept(this);
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        greaterThanEquals.getLeftExpression().accept(this);
        greaterThanEquals.getRightExpression().accept(this);
    }

    @Override
    public void visit(MinorThan minorThan) {
        minorThan.getLeftExpression().accept(this);
        minorThan.getRightExpression().accept(this);
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        minorThanEquals.getLeftExpression().accept(this);
        minorThanEquals.getRightExpression().accept(this);
    }
}
