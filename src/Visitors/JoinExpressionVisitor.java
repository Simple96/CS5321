package Visitors;

import Var.Tuple;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;

import java.util.List;


public abstract class JoinExpressionVisitor implements ExpressionVisitor {
    private Tuple tp1 = null, tp2 = null;
    private List<String> sche1, sche2;
    private long Num;
    private boolean status;

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

    public void set_Tuple(Tuple tp1_in, Tuple tp2_in){
        setTp1(tp1_in);
        setTp2(tp2_in);
    }

    private void setSche1(List<String> sche1){
        this.sche1 = sche1;
    }

    private void setSche2(List<String> sche2){
        this.sche2 = sche2;
    }

    public void setSchema(List<String> sche1, List<String> sche2){
        setSche1(sche1);
        setSche2(sche2);
    }


    @Override
    public void visit(Column column_exit) {
        String col_name = column_exit.getColumnName();
        String whole_col_name = column_exit.getWholeColumnName();
        int index, index1 = -1, index2 = -1;
        index1 = sche1.indexOf(col_name);
        if (index1 != -1) this.Num = Long.parseLong(tp1.get_element(index1));
        else{
            index2 = sche2.indexOf(col_name);
            if (index2 == -1) {
                System.out.print("Class JoinExpressionVisitor: schema input wrong");
            }
            else this.Num = Long.parseLong(tp2.get_element(index2));
        }
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
