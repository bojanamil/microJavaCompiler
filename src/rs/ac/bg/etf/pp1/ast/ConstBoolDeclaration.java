// generated with ast extension for cup
// version 0.8
// 26/4/2018 13:30:43


package rs.ac.bg.etf.pp1.ast;

public class ConstBoolDeclaration extends ConstIdent {

    private String constIdent;
    private Boolean boolValue;

    public ConstBoolDeclaration (String constIdent, Boolean boolValue) {
        this.constIdent=constIdent;
        this.boolValue=boolValue;
    }

    public String getConstIdent() {
        return constIdent;
    }

    public void setConstIdent(String constIdent) {
        this.constIdent=constIdent;
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public void setBoolValue(Boolean boolValue) {
        this.boolValue=boolValue;
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
        buffer.append("ConstBoolDeclaration(\n");

        buffer.append(" "+tab+constIdent);
        buffer.append("\n");

        buffer.append(" "+tab+boolValue);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstBoolDeclaration]");
        return buffer.toString();
    }
}
