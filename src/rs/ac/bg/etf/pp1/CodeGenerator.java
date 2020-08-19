package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

import rs.etf.pp1.symboltable.concepts.Obj;

public class CodeGenerator extends VisitorAdaptor {
	
	private int mainPc;
	
	public int getMainPc() {
		return mainPc;
	}
	
	@Override
	public void visit(FactorNumber factorNumber){
		Obj constInt = new Obj(Obj.Con, "$", factorNumber.struct, factorNumber.getIntValue(), 0);
		Code.load(constInt);
	}
	
	@Override
	public void visit(FactorBool factorBool){
		Obj constBool = new Obj(Obj.Con, "", factorBool.struct, factorBool.getBoolValue() ? 1 : 0, 0);
		Code.load(constBool);
	}

	@Override
	public void visit(FactorChar factorChar){
		Obj constChar = new Obj(Obj.Con, "", factorChar.struct, factorChar.getCharValue(), 0);
		Code.load(constChar);
	}

	
	@Override
	public void visit(PrintStatementExprOnly printStatementExprOnly) {
		if(printStatementExprOnly.getExpr().struct==Tab.charType){
			Code.put(Code.const_1);
			Code.put(Code.bprint);
		}
		else{
			Code.put(Code.const_5);
			Code.put(Code.print);
		}
	}
	
	@Override
	public void visit(PrintStatementExprAndConst printStatementExprAndConst) {
		Code.loadConst(printStatementExprAndConst.getN2());
		if(printStatementExprAndConst.getExpr().struct==Tab.charType){
			Code.put(Code.bprint);
		}
		else {
			Code.put(Code.print);
		}
	}
	
	@Override
	public void visit(ReadDesignator readDesignator) {
		if(readDesignator.getDesignator().obj.getType()==Tab.charType){
			Code.put(Code.bread);
		}
		else {
			Code.put(Code.read);
		}
		Code.store(readDesignator.getDesignator().obj);
	}
	
	public void visit(MethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(ReturnStatementExpression ReturnExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(ReturnStatementVoid ReturnNoExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(MethodTypeIdent methodTypeIdent){
		if ("main".equalsIgnoreCase(methodTypeIdent.getMethName())) {
			mainPc = Code.pc;
		}
		methodTypeIdent.obj.setAdr(Code.pc);
		
		// Collect arguments and local variables.
		SyntaxNode methodNode = methodTypeIdent.getParent();
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		
		// Generate the entry.
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(varCnt.getCount() + fpCnt.getCount());
	}
	
	public void visit(MethodTypeVoid methodTypeVoid){
		if ("main".equalsIgnoreCase(methodTypeVoid.getMethName())) {
			mainPc = Code.pc;
		}
		methodTypeVoid.obj.setAdr(Code.pc);
		
		// Collect arguments and local variables.
		SyntaxNode methodNode = methodTypeVoid.getParent();
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		
		// Generate the entry.
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(varCnt.getCount() + fpCnt.getCount());
	}
	
	public void visit(DesignatorEqualsStatement designatorEqualsStatement){
		Code.store(designatorEqualsStatement.getDesignator().obj);
	}
	
	public void visit(FactorVar factorVar){
		Code.load(factorVar.getDesignator().obj);
	}
	
	public void visit(FactorNewArray factorNewArray){
		Code.put(Code.newarray);
		if(factorNewArray.getType().struct.equals(Tab.charType))
			Code.put(0);
		else
			Code.put(1);
	}
	
	public void visit(DesignatorName designatorName){
		Code.load(designatorName.getDesignator().obj);
	}
	
	public void visit(DesignatorIncrement designatorIncrement){
		if(designatorIncrement.getDesignator().obj.getKind()== Obj.Elem){
			Code.put(Code.dup2);
		}
		Code.load(designatorIncrement.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(designatorIncrement.getDesignator().obj);
	}
	
	public void visit(DesignatorDecrement designatorDecrement){
		if(designatorDecrement.getDesignator().obj.getKind()== Obj.Elem){
			Code.put(Code.dup2);
		}
		Code.load(designatorDecrement.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(designatorDecrement.getDesignator().obj);
	}

	public void visit(DesignatorFunctionCall designatorFunctionCall){
		int destinationAddress = designatorFunctionCall.getDesignator().obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(destinationAddress);
		
		if(designatorFunctionCall.getDesignator().obj.getType()!= Tab.noType)
			Code.put(Code.pop);
	}
	
	public void visit(FactorFuncCall factorFuncCall){
		int destinationAddress = factorFuncCall.getDesignator().obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(destinationAddress);
	}

	public void visit(AddExpr addExpr){
		if(addExpr.getAddop() instanceof AddopPLus)
			Code.put(Code.add);
		else
			Code.put(Code.sub);
	}
	
	public void visit(MultipleTermList MultipleTermList){
		if(MultipleTermList.getMulop() instanceof MulopMul)
			Code.put(Code.mul);
		else if(MultipleTermList.getMulop() instanceof MulopDiv)
			Code.put(Code.div);
		else
			Code.put(Code.rem);
	}
}
