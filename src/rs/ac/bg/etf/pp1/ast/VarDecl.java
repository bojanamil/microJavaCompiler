// generated with ast extension for cup
// version 0.8
// 26/4/2018 13:30:43


package rs.ac.bg.etf.pp1.ast;

public class VarDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private SameTypeVarDeclList SameTypeVarDeclList;

    public VarDecl (Type Type, SameTypeVarDeclList SameTypeVarDeclList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.SameTypeVarDeclList=SameTypeVarDeclList;
        if(SameTypeVarDeclList!=null) SameTypeVarDeclList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public SameTypeVarDeclList getSameTypeVarDeclList() {
        return SameTypeVarDeclList;
    }

    public void setSameTypeVarDeclList(SameTypeVarDeclList SameTypeVarDeclList) {
        this.SameTypeVarDeclList=SameTypeVarDeclList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(SameTypeVarDeclList!=null) SameTypeVarDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(SameTypeVarDeclList!=null) SameTypeVarDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(SameTypeVarDeclList!=null) SameTypeVarDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SameTypeVarDeclList!=null)
            buffer.append(SameTypeVarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDecl]");
        return buffer.toString();
    }
}
