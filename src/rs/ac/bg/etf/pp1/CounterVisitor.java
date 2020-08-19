package rs.ac.bg.etf.pp1;

import java.util.ArrayList;

import rs.ac.bg.etf.pp1.ast.ActualParamList;
import rs.ac.bg.etf.pp1.ast.FormalParamDecl;
import rs.ac.bg.etf.pp1.ast.MultipleActualParams;
import rs.ac.bg.etf.pp1.ast.MultipleFormalParamDeclarations;
import rs.ac.bg.etf.pp1.ast.SingleActualParam;
import rs.ac.bg.etf.pp1.ast.SingleFormalParamDeclaration;
import rs.ac.bg.etf.pp1.ast.SingleSameTypeVarDeclArray;
import rs.ac.bg.etf.pp1.ast.SingleSameTypeVarDeclNotArray;
import rs.ac.bg.etf.pp1.ast.VarDecl;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CounterVisitor extends VisitorAdaptor {
	
	protected int count;
	
	
	public int getCount() {
		return count;
	}
	
	public static class FormParamCounter extends CounterVisitor {

		@Override
		public void visit(MultipleFormalParamDeclarations formParamDecl1) {
			count++;
		}		

		@Override
		public void visit(SingleFormalParamDeclaration formParamDecl1) {
			count++;
		}		
	}

	
	public static class VarCounter extends CounterVisitor {		
		
		@Override
		public void visit(SingleSameTypeVarDeclNotArray VarDecl) {
			count++;
		}	
		
		@Override
		public void visit(SingleSameTypeVarDeclArray VarDecl) {
			count++;
		}
	}
	
	
}
