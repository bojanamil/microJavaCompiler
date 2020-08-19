// generated with ast extension for cup
// version 0.8
// 26/4/2018 13:30:43


package rs.ac.bg.etf.pp1.ast;

public class FactorFuncCall extends Factor {

    private Designator Designator;
    private ActualParsOptional ActualParsOptional;

    public FactorFuncCall (Designator Designator, ActualParsOptional ActualParsOptional) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.ActualParsOptional=ActualParsOptional;
        if(ActualParsOptional!=null) ActualParsOptional.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public ActualParsOptional getActualParsOptional() {
        return ActualParsOptional;
    }

    public void setActualParsOptional(ActualParsOptional ActualParsOptional) {
        this.ActualParsOptional=ActualParsOptional;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(ActualParsOptional!=null) ActualParsOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(ActualParsOptional!=null) ActualParsOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(ActualParsOptional!=null) ActualParsOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorFuncCall(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActualParsOptional!=null)
            buffer.append(ActualParsOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorFuncCall]");
        return buffer.toString();
    }
}
