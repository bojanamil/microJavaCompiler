// generated with ast extension for cup
// version 0.8
// 26/4/2018 13:30:43


package rs.ac.bg.etf.pp1.ast;

public class ExprPositive extends Expr {

    private ExprPos ExprPos;

    public ExprPositive (ExprPos ExprPos) {
        this.ExprPos=ExprPos;
        if(ExprPos!=null) ExprPos.setParent(this);
    }

    public ExprPos getExprPos() {
        return ExprPos;
    }

    public void setExprPos(ExprPos ExprPos) {
        this.ExprPos=ExprPos;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprPos!=null) ExprPos.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprPos!=null) ExprPos.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprPos!=null) ExprPos.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprPositive(\n");

        if(ExprPos!=null)
            buffer.append(ExprPos.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprPositive]");
        return buffer.toString();
    }
}
