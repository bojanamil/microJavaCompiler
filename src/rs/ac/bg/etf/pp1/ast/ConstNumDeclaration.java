// generated with ast extension for cup
// version 0.8
// 26/4/2018 13:30:43


package rs.ac.bg.etf.pp1.ast;

public class ConstNumDeclaration extends ConstIdent {

    private String constIdent;
    private Integer integerValue;

    public ConstNumDeclaration (String constIdent, Integer integerValue) {
        this.constIdent=constIdent;
        this.integerValue=integerValue;
    }

    public String getConstIdent() {
        return constIdent;
    }

    public void setConstIdent(String constIdent) {
        this.constIdent=constIdent;
    }

    public Integer getIntegerValue() {
        return integerValue;
    }

    public void setIntegerValue(Integer integerValue) {
        this.integerValue=integerValue;
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
        buffer.append("ConstNumDeclaration(\n");

        buffer.append(" "+tab+constIdent);
        buffer.append("\n");

        buffer.append(" "+tab+integerValue);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstNumDeclaration]");
        return buffer.toString();
    }
}
