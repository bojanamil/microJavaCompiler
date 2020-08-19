// generated with ast extension for cup
// version 0.8
// 26/4/2018 13:30:43


package rs.ac.bg.etf.pp1.ast;

public class ClassDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ClassIdent ClassIdent;
    private ClassParamDeclarations ClassParamDeclarations;

    public ClassDecl (ClassIdent ClassIdent, ClassParamDeclarations ClassParamDeclarations) {
        this.ClassIdent=ClassIdent;
        if(ClassIdent!=null) ClassIdent.setParent(this);
        this.ClassParamDeclarations=ClassParamDeclarations;
        if(ClassParamDeclarations!=null) ClassParamDeclarations.setParent(this);
    }

    public ClassIdent getClassIdent() {
        return ClassIdent;
    }

    public void setClassIdent(ClassIdent ClassIdent) {
        this.ClassIdent=ClassIdent;
    }

    public ClassParamDeclarations getClassParamDeclarations() {
        return ClassParamDeclarations;
    }

    public void setClassParamDeclarations(ClassParamDeclarations ClassParamDeclarations) {
        this.ClassParamDeclarations=ClassParamDeclarations;
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
        if(ClassIdent!=null) ClassIdent.accept(visitor);
        if(ClassParamDeclarations!=null) ClassParamDeclarations.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassIdent!=null) ClassIdent.traverseTopDown(visitor);
        if(ClassParamDeclarations!=null) ClassParamDeclarations.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassIdent!=null) ClassIdent.traverseBottomUp(visitor);
        if(ClassParamDeclarations!=null) ClassParamDeclarations.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDecl(\n");

        if(ClassIdent!=null)
            buffer.append(ClassIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassParamDeclarations!=null)
            buffer.append(ClassParamDeclarations.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDecl]");
        return buffer.toString();
    }
}
