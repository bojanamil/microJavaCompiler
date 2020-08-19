// generated with ast extension for cup
// version 0.8
// 26/4/2018 13:30:43


package rs.ac.bg.etf.pp1.ast;

public class SingleSameTypeVarDeclaration extends SameTypeVarDeclList {

    private SingleSameTypeVarDecl SingleSameTypeVarDecl;

    public SingleSameTypeVarDeclaration (SingleSameTypeVarDecl SingleSameTypeVarDecl) {
        this.SingleSameTypeVarDecl=SingleSameTypeVarDecl;
        if(SingleSameTypeVarDecl!=null) SingleSameTypeVarDecl.setParent(this);
    }

    public SingleSameTypeVarDecl getSingleSameTypeVarDecl() {
        return SingleSameTypeVarDecl;
    }

    public void setSingleSameTypeVarDecl(SingleSameTypeVarDecl SingleSameTypeVarDecl) {
        this.SingleSameTypeVarDecl=SingleSameTypeVarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SingleSameTypeVarDecl!=null) SingleSameTypeVarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SingleSameTypeVarDecl!=null) SingleSameTypeVarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SingleSameTypeVarDecl!=null) SingleSameTypeVarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleSameTypeVarDeclaration(\n");

        if(SingleSameTypeVarDecl!=null)
            buffer.append(SingleSameTypeVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleSameTypeVarDeclaration]");
        return buffer.toString();
    }
}
