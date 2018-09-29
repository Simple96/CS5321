package Visitors;

import Var.Tuple;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

import java.util.List;


public abstract class JoinExpressionVisitor implements ExpressionVisitor {
    private Tuple tp1 = null, tp2 = null;
    private List<String> sche1, sche2;
    Expression conjunct_expression;

    public JoinExpressionVisitor(List<String> sc1, List<String> sc2){
        this.sche1 = sc1;
        this.sche2 = sc2;
    }

    void setTp1(Tuple tp1_in){
        this.tp1 = tp1_in;
    }

    void setTp2(Tuple tp2_in){
        this.tp2 = tp2_in;
    }

    void set_Tuple(Tuple tp1_in, Tuple tp2_in){
        setTp1(tp1_in);
        setTp2(tp2_in);
    }

    void setSche1(List<String> sche1){
        this.sche1 = sche1;
    }

    void setSche2(List<String> sche2){
        this.sche2 = sche2;
    }

    void setSchema(List<String> sche1, List<String> sche2){
        setSche1(sche1);
        setSche2(sche2);
    }


}
