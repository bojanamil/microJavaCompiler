// generated with ast extension for cup
// version 0.8
// 26/4/2018 13:30:43


package rs.ac.bg.etf.pp1.ast;

public class ErrorSingleSameTypeVarDecl extends SingleSameTypeVarDecl {

    public ErrorSingleSameTypeVarDecl () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ErrorSingleSameTypeVarDecl(\n");

        buffer.append(tab);
        buffer.append(") [ErrorSingleSameTypeVarDecl]");
        return buffer.toString();
    }
}
