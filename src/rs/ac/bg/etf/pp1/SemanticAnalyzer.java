package rs.ac.bg.etf.pp1;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor {

	boolean errorDetected = false;
	int printCallCount = 0;
	Obj currentMethod = null;
	boolean returnFound = false;
	int nVars;
	
	Struct lastTypeUsed;
	int numFormalParameters = 0;
	boolean mainFound= false;
	
	int currentFpPos = 0;
	 
	ArrayList<ConstIdent> constDefinitions;
	ArrayList<SingleSameTypeVarDecl> varDeclarations;
	
	
	Struct boolType = new Struct(Struct.Bool);

	Logger log = Logger.getLogger(getClass());
	
	public SemanticAnalyzer() {
		Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", boolType));
	}

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public void visit(Program program) {		
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}

	public void visit(ProgName progName) {
		Obj sym = Tab.currentScope.findSymbol(progName.getPName()); //Tab.currentScope.findSymbol trazi samo u trenutnom scope-u
		if(sym==null){
			progName.obj = Tab.insert(Obj.Prog, progName.getPName(), Tab.noType);
		}
		else {
			report_error("Semanticka greska: u trenutnom scope-u vec postoji simbol sa imenom " + progName.getPName(), progName);
			progName.obj = new Obj(Obj.Prog, progName.getPName(), Tab.noType);
		}
		Tab.openScope();  
	}
	
	public void visit(ProgVarDeclarations progVarDeclarations){
		if(varDeclarations != null){
			for(SingleSameTypeVarDecl currentVarDecl : varDeclarations){
				if(currentVarDecl instanceof SingleSameTypeVarDeclNotArray){
					SingleSameTypeVarDeclNotArray varDecl = (SingleSameTypeVarDeclNotArray)currentVarDecl;
					report_info("Deklarisana je globalna promenljiva " + varDecl.getVarName(),varDecl);
				}
				else {
					SingleSameTypeVarDeclArray varDecl = (SingleSameTypeVarDeclArray)currentVarDecl;
					report_info("Deklarisana je globalna promenljiva " + varDecl.getVarName(),varDecl);
				}
			}
		}
		varDeclarations = null;
	}

	public void visit(MethodDecl methodDecl) {
		if (!returnFound && currentMethod.getType() != Tab.noType) {
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funkcija " + currentMethod.getName() + " nema return iskaz!", null);
		}
		if(numFormalParameters>0 && currentMethod.getName().equals("main"))
			report_error("Semanticka greska: main funkcija ne sme da ima formalne parametre " , null);
		currentMethod.setLevel(numFormalParameters);
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		
		returnFound = false; 
		currentMethod = null;
		numFormalParameters = 0;
	}
	
	public void visit(MethodVarDeclarations methodVarDeclarations){
		if(varDeclarations != null){
			for(SingleSameTypeVarDecl currentVarDecl : varDeclarations){
				if(currentVarDecl instanceof SingleSameTypeVarDeclNotArray){
					SingleSameTypeVarDeclNotArray varDecl = (SingleSameTypeVarDeclNotArray)currentVarDecl;
					report_info("Deklarisana je lokalna promenljiva " + varDecl.getVarName(), varDecl);
				}
				else {
					SingleSameTypeVarDeclArray varDecl = (SingleSameTypeVarDeclArray)currentVarDecl;
					report_info("Deklarisana je lokalna promenljiva " + varDecl.getVarName(), varDecl);
				}
			}
		}
		varDeclarations = null;
	}

	public void visit(MethodTypeIdent methodTypeIdent) {

		Obj sym = Tab.currentScope.findSymbol(methodTypeIdent.getMethName());
		if(sym == null){
			if(methodTypeIdent.getMethName().equals("main"))
				report_error("Semanticka greska: main metoda ne sme imati povratnu vrednost " , null);
			currentMethod = Tab.insert(Obj.Meth, methodTypeIdent.getMethName(), lastTypeUsed);
			methodTypeIdent.obj = currentMethod;
		}
		else {
			report_error("Semanticka greska: u trenutnom scope-u vec postoji simbol sa imenom " + methodTypeIdent.getMethName(), methodTypeIdent);
			methodTypeIdent.obj = Tab.insert(Obj.Meth, methodTypeIdent.getMethName(), lastTypeUsed);
		}
		if(methodTypeIdent.getMethName().equals("main")) mainFound=true;
		Tab.openScope();
	}

	public void visit(MethodTypeVoid methodTypeVoid) {

		Obj sym = Tab.currentScope.findSymbol(methodTypeVoid.getMethName());
		if(sym == null){
			currentMethod = Tab.insert(Obj.Meth, methodTypeVoid.getMethName(), Tab.noType);
			methodTypeVoid.obj = currentMethod;
		}
		else {
			report_error("Semanticka greska: u trenutnom scope-u vec postoji simbol sa imenom " + methodTypeVoid.getMethName(), methodTypeVoid);
			methodTypeVoid.obj = Tab.insert(Obj.Meth, methodTypeVoid.getMethName(), lastTypeUsed);
		}
		if(methodTypeVoid.getMethName().equals("main")) mainFound=true;
		Tab.openScope();
	}

	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());  
		if (typeNode == Tab.noObj) {
			report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola", type); 
			type.struct = Tab.noType;
		} 
		else {
			if (Obj.Type == typeNode.getKind()) {
				type.struct = typeNode.getType();
			} 
			else {
				report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip ", type);
				type.struct = Tab.noType;
			}
		}  
		lastTypeUsed = type.struct; 
	}

		
	public void visit(ConstDecl constDecl){
		if(constDefinitions != null){
			for(ConstIdent singleConstDeclaration: constDefinitions){
				if(singleConstDeclaration instanceof ConstNumDeclaration){
					ConstNumDeclaration cii= (ConstNumDeclaration)singleConstDeclaration;
					report_info("Deklarisana je konstanta " + cii.getConstIdent() ,singleConstDeclaration);
				}
				else if(singleConstDeclaration instanceof ConstBoolDeclaration){
					ConstBoolDeclaration cii= (ConstBoolDeclaration)singleConstDeclaration;
					report_info("Deklarisana je konstanta " + cii.getConstIdent() ,singleConstDeclaration);
				}
				else{
					ConstCharDeclaration cii= (ConstCharDeclaration)singleConstDeclaration;
					report_info("Deklarisana je konstanta " + cii.getConstIdent() ,singleConstDeclaration);
				}
			}
		}
			
		constDefinitions = null;
	}
	
	public void visit(ConstNumDeclaration constNumDeclaration) {
		Obj constNumber = Tab.currentScope.findSymbol(constNumDeclaration.getConstIdent());
		
		if(constNumber == null){
			Obj constObjNum = Tab.insert(Obj.Con, constNumDeclaration.getConstIdent(), lastTypeUsed);
			if(lastTypeUsed.equals(Tab.intType)) {
				constObjNum.setAdr(constNumDeclaration.getIntegerValue());
				if(constDefinitions == null) constDefinitions = new ArrayList<ConstIdent>();
				constDefinitions.add(constNumDeclaration);
			}
			else 
				report_error("Semanticka greska: promenljiva " + constNumDeclaration.getConstIdent() + " nije tipa Integer", constNumDeclaration);	
		}
		else
			report_error("Semanticka greska: u trenutnom scope-u vec postoji simbol sa imenom " + constNumDeclaration.getConstIdent(), constNumDeclaration);		
	}
	
	public void visit(ConstBoolDeclaration constBoolDeclaration) {
		Obj constNumber = Tab.currentScope.findSymbol(constBoolDeclaration.getConstIdent());
		if(constNumber == null){
			Obj constObjBool = Tab.insert(Obj.Con, constBoolDeclaration.getConstIdent(), lastTypeUsed);
			if(lastTypeUsed.equals(boolType)){ 
				constObjBool.setAdr(constBoolDeclaration.getBoolValue() ? 1 : 0);
				if(constDefinitions == null) constDefinitions = new ArrayList<ConstIdent>();
				constDefinitions.add(constBoolDeclaration);
			}
			else
				report_error("Semanticka greska: promenljiva " + constBoolDeclaration.getConstIdent() + " nije tipa Bool", constBoolDeclaration);	
		}
		else
			report_error("Semanticka greska: u trenutnom scope-u vec postoji simbol sa imenom " + constBoolDeclaration.getConstIdent(), constBoolDeclaration);		
	}
	
	public void visit(ConstCharDeclaration constCharDeclaration) {
		Obj constNumber = Tab.currentScope.findSymbol(constCharDeclaration.getConstIdent());
		if(constNumber == null){
			Obj constObjChar = Tab.insert(Obj.Con, constCharDeclaration.getConstIdent(), lastTypeUsed);	
			
			if(lastTypeUsed.equals(Tab.charType)){
				constObjChar.setAdr(constCharDeclaration.getCharValue());
				if(constDefinitions == null) constDefinitions = new ArrayList<ConstIdent>();
				constDefinitions.add(constCharDeclaration);
			}		
			else
				report_error("Semanticka greska: promenljiva " + constCharDeclaration.getConstIdent() + " nije tipa Char", constCharDeclaration);	
		}
		else
			report_error("Semanticka greska: u trenutnom scope-u vec postoji simbol sa imenom " + constCharDeclaration.getConstIdent(), constCharDeclaration);		
	}
	
	
	public void visit(SingleSameTypeVarDeclNotArray singleSameTypeVarDeclNotArray){
		Obj varDecl = Tab.currentScope.findSymbol(singleSameTypeVarDeclNotArray.getVarName());
		if(varDecl == null){
			Obj newVar = Tab.insert(Obj.Var, singleSameTypeVarDeclNotArray.getVarName(), lastTypeUsed);
			if(varDeclarations == null) varDeclarations = new ArrayList<SingleSameTypeVarDecl>();
			varDeclarations.add(singleSameTypeVarDeclNotArray);
		}
		else
			report_error("Semanticka greska: u trenutnom scope-u vec postoji simbol sa imenom " + singleSameTypeVarDeclNotArray.getVarName(), singleSameTypeVarDeclNotArray);		
	}
	
	public void visit(SingleSameTypeVarDeclArray singleSameTypeVarDeclArray){
		Obj varDecl = Tab.currentScope.findSymbol(singleSameTypeVarDeclArray.getVarName());
		if(varDecl == null){
			Obj newVar = Tab.insert(Obj.Var, singleSameTypeVarDeclArray.getVarName(), new Struct(Struct.Array, lastTypeUsed));
			if(varDeclarations == null) varDeclarations = new ArrayList<SingleSameTypeVarDecl>();
			varDeclarations.add(singleSameTypeVarDeclArray);
		}
		else
			report_error("Semanticka greska: u trenutnom scope-u vec postoji simbol sa imenom " + singleSameTypeVarDeclArray.getVarName(), singleSameTypeVarDeclArray);		
	}
	
	public void visit(FormPars formPars){
		currentFpPos=0;
	}
	
	public void visit(FormalParamDeclNotArray formalParamDeclNotArray){
		Obj varDecl = Tab.currentScope.findSymbol(formalParamDeclNotArray.getVarName());
		if(varDecl == null){
			currentFpPos++;
			report_info("Deklarisan je formalni parametar "+ formalParamDeclNotArray.getVarName(), formalParamDeclNotArray);
			Obj newVar = Tab.insert(Obj.Var, formalParamDeclNotArray.getVarName(),lastTypeUsed);
			newVar.setFpPos(currentFpPos);
			numFormalParameters++;
		}
		else
			report_error("Semanticka greska: u trenutnom scope-u vec postoji simbol sa imenom " + formalParamDeclNotArray.getVarName(), formalParamDeclNotArray);		
	}
	
	public void visit(FormalParamDeclArray formalParamDeclArray){
		Obj varDecl = Tab.currentScope.findSymbol(formalParamDeclArray.getVarName());
		if(varDecl == null){
			currentFpPos++;
			report_info("Deklarisan je formalni parametar "+ formalParamDeclArray.getVarName(), formalParamDeclArray);
			Obj newVar = Tab.insert(Obj.Var, formalParamDeclArray.getVarName(), new Struct(Struct.Array, lastTypeUsed));
			newVar.setFpPos(currentFpPos);
			numFormalParameters++;
		}
		else
			report_error("Semanticka greska: u trenutnom scope-u vec postoji simbol sa imenom " + formalParamDeclArray.getVarName(), formalParamDeclArray);		
	}

	public void visit(ErrorFormalParamDecl errorFormalParamDecl){
		report_info("Uspesan oporavak od greske pri deklarisanju formalnog parametra funkcije", errorFormalParamDecl);
	}
	
	public void visit(DesignatorEqualsStatement designatorEqualsStatement){
		int designatorKind = designatorEqualsStatement.getDesignator().obj.getKind();
		if(designatorKind == Obj.Con || designatorKind == Obj.Meth || designatorKind == Obj.Prog || designatorKind == Obj.Type)
			report_error("Semanticka greska: leva strana operatora dodele mora biti ili tipa Elem, ili Var ili Field " , designatorEqualsStatement);
		else{
			
			if(designatorEqualsStatement.getExpr().struct.equals(designatorEqualsStatement.getDesignator().obj.getType())) {
				//ok situacija: a=3; ili x[2]=a[3];
			}
			else if(designatorEqualsStatement.getDesignator().obj.getKind()==Obj.Elem && designatorEqualsStatement.getDesignator().obj.getType()==Tab.noType || designatorEqualsStatement.getDesignator().obj.getType()==Tab.noType){
				//obradjeno u Designator-u; situacija kada neko polje nije u ts ; npr a nije u ts
				//ovo je slucaj kada se u levoj  strani jednakosti koristi Designator koga nema u ts
			}
			else if(designatorEqualsStatement.getExpr().struct==Tab.noType){
				//obradjeno u Designator-u; situacija kada neko polje nije u ts ; npr a nije u ts
				//ovo je slucaj kada se u levoj  strani jednakosti koristi Designator koga nema u ts
			}
			else 
				report_error("Semanticka greska aaa: leva strana operatora dodele mora biti istog tipa kao desna strana " , designatorEqualsStatement);
			}
	}
	
	public void visit(ErrorDesignatorEqualsStatement errorDesignatorEqualsStatement){
		 report_info("Uspesan oporavak od greske pri iskazu dodele",errorDesignatorEqualsStatement);
	}
	
	public void visit(ErrorSingleSameTypeVarDecl errorSingleSameTypeVarDecl){
		report_info("Uspesan oporavak od greske pri deklarisanju globalne promenljive",errorSingleSameTypeVarDecl);
	}
	
	
	public void visit(DesignatorIncrement designatorIncrement){
		
		if(!designatorIncrement.getDesignator().obj.getType().equals(Tab.intType))
			report_error("Semanticka greska: designator " + designatorIncrement.getDesignator().obj.getName() + " mora da bude tipa Integer", designatorIncrement);
		
	}
	
	public void visit(DesignatorDecrement designatorDecrement){
		//dodaj lvalue
		if(!designatorDecrement.getDesignator().obj.getType().equals(Tab.intType))
			report_error("Semanticka greska: designator " + designatorDecrement.getDesignator().obj.getName() + " mora da bude tipa Integer", designatorDecrement);
		
	}
	
	public void visit(DesignatorIdent designatorIdent){
		Obj designator = Tab.find(designatorIdent.getName());
		if(designator == Tab.noObj){
			report_error("Semanticka greska: u tabeli simbola ne postoji nista sa imenom " + designatorIdent.getName(), designatorIdent);
		}
		designatorIdent.obj = designator;
	}
	
	public void visit(DesignatorArrayAccess designatorArrayAccess){
		designatorArrayAccess.obj = new Obj(Obj.Elem, "",designatorArrayAccess.getDesignatorName().getDesignator().obj.getType().getElemType()); 
		
		if(designatorArrayAccess.getDesignatorName().getDesignator().obj==Tab.noObj){	
			designatorArrayAccess.obj = new Obj(Obj.Elem, "",Tab.noType); 
		}
		else if(designatorArrayAccess.getDesignatorName().getDesignator().obj.getType().getKind()!=Struct.Array)
			report_error("Semanticka greska: tip od " + designatorArrayAccess.getDesignatorName().getDesignator().obj.getName() + " mora biti Array", designatorArrayAccess);
		if(!designatorArrayAccess.getExpr().struct.equals(Tab.intType))	
			report_error("Semanticka greska: tip Expression-a sa desne strane mora biti Int " , designatorArrayAccess);	
	}
	
	
	
	public void visit(MultipleTermList multipleTermList){
		Struct term = multipleTermList.getTerm().struct;
		Struct fact = multipleTermList.getFactor().struct;
		if(term.equals(fact) && term== Tab.intType) 
			multipleTermList.struct = term;
		else {
			if(term==Tab.noType || fact==Tab.noType){	
			}
			else
				report_error("Semanticka greska: tipovi sa obe strane operatora Mulop moraju biti jednaki i tipa Integer "  , multipleTermList);
			multipleTermList.struct = Tab.noType;
		}		
	}
	
	public void visit(SingleTerm singleTerm){
		singleTerm.struct = singleTerm.getFactor().struct;
	}
	
	public void visit(AddExpr addExpr){
		Struct term = addExpr.getTerm().struct;
		Struct expr = addExpr.getExprPos().struct;
		if(term.equals(expr) && term== Tab.intType) 
			addExpr.struct = term;
		else {
			if(term==Tab.noType || expr==Tab.noType){	
			}
			else
				report_error("Semanticka greska: tipovi sa obe strane operatora Addop moraju biti jednaki i tipa Integer " , addExpr);
			addExpr.struct = Tab.noType;
		}	
	}
	
	public void visit(TermExpr termExpr){
		termExpr.struct = termExpr.getTerm().struct;
	}
	
	public void visit(ExprNegative exprNegative){
		if(exprNegative.getExprPos().struct!= Tab.intType){
			report_error("Semanticka greska: tip izraza mora biti Integer " , exprNegative);
			exprNegative.struct = Tab.noType;
		}
		else
			exprNegative.struct = exprNegative.getExprPos().struct;
	}
	
	public void visit(ExprPositive exprPositive){
		exprPositive.struct = exprPositive.getExprPos().struct;
	}
	
	public void visit(FactorVar factorVar){
		factorVar.struct = factorVar.getDesignator().obj.getType();
	}
	
	public void visit(FactorNumber factorNumber){
		factorNumber.struct = Tab.intType;
	}
	
	public void visit(FactorBool factorBool){
		factorBool.struct = boolType;
	}

	public void visit(FactorChar factorChar){
		factorChar.struct = Tab.charType;
	}

	public void visit(FactorExprGrouping factorExprGrouping){
		factorExprGrouping.struct = factorExprGrouping.getExpr().struct;
	}
	
	public void visit(FactorNewArray factorNewArray){
		if(!factorNewArray.getExpr().struct.equals(Tab.intType)){
			report_error("Semanticka greska: tip izraza izmedju para kockastih zagrada mora biti Int "  , factorNewArray);
			factorNewArray.struct = Tab.noType;
		}
		else
			factorNewArray.struct = new Struct(Struct.Array, lastTypeUsed);
	}
	

	public void visit(FactorFuncCall factorFuncCall){
		factorFuncCall.struct = factorFuncCall.getDesignator().obj.getType();

		Obj func = factorFuncCall.getDesignator().obj;
		if (Obj.Meth == func.getKind()) { 
			report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + factorFuncCall.getLine(), null);
			if(func.getType()==Tab.noType)
				report_error("Greska na liniji " + factorFuncCall.getLine()+" : funkcija " + func.getName() + " mora imati povratni tip!", null);
			
			
			Collection<Obj> localParameters = factorFuncCall.getDesignator().obj.getLocalSymbols();
			ArrayList<Obj> formalParameters = new ArrayList<Obj>();
			for(Obj localParam: localParameters){
				if(localParam.getFpPos()!=0)
					formalParameters.add(localParam);
				else break;
			}
			
			ArrayList<Struct> actualParamsList = new ArrayList<Struct>();
			
			ActualParsOptional apo = factorFuncCall.getActualParsOptional();
			if(apo instanceof Actuals) {
				ActualParamList apc = ((Actuals) apo).getActualParamList();
				while(apc != null){
					if(apc instanceof MultipleActualParams){
						actualParamsList.add(((MultipleActualParams)apc).getExpr().struct);
						apc = ((MultipleActualParams)apc).getActualParamList();
					}	
					else{
						actualParamsList.add(((SingleActualParam)apc).getExpr().struct);
						apc= null;
					}
				}
			}

			if(formalParameters.size() != actualParamsList.size())
				report_error("Greska na liniji " + factorFuncCall.getLine()+ " :broj formalnih i stvarnih argumenata u funkciji "+ factorFuncCall.getDesignator().obj.getName() + " nije isti", null);
			else {
				for(int i=0; i<formalParameters.size(); i++){
					if(actualParamsList.get(i)!= formalParameters.get(i).getType()){
						report_error("Greska na liniji " + factorFuncCall.getLine()+ " :ne slazu se tipovi formalnih i stvarnih argumenata u pozivu funkcije " + factorFuncCall.getDesignator().obj.getName(), null);
						break;
					}
				}
			}
		} 
		else {
			if(func!=Tab.noObj) 
			 report_error("Greska na liniji " + factorFuncCall.getLine()+" : ime " + func.getName() + " nije funkcija!", null);
		}
	}
	
	public void visit(ReturnStatementVoid returnStatementVoid){
		returnFound = true;
		Struct currMethType = currentMethod.getType();
		if (!currMethType.equals(Tab.noType)) {
			report_error("Semanticka greska: tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
		}	
	}
	
	public void visit(ReturnStatementExpression returnStatementExpression){
		returnFound = true;
		Struct currMethType = currentMethod.getType();
		if (!currMethType.equals(returnStatementExpression.getExpr().struct)) {
			report_error("Semanticka greska: tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
		}			  	     	
	}
	
	public void visit(PrintStatementExprOnly printStatementExprOnly){
		Struct exprType = printStatementExprOnly.getExpr().struct;
		if(exprType != Tab.intType && exprType != Tab.charType  && exprType != boolType && exprType!=Tab.noType)
			report_error("Greska na liniji " + printStatementExprOnly.getLine() + " : " + "tip izraza za koji se poziva print mora biti Int, Char ili Bool ", null);		  	     	
	}

	public void visit(PrintStatementExprAndConst printStatementExprAndConst){
		Struct exprType = printStatementExprAndConst.getExpr().struct;
		if(exprType != Tab.intType && exprType != Tab.charType  && exprType != boolType && exprType!=Tab.noType)
			report_error("Greska na liniji " + printStatementExprAndConst.getLine() + " : " + "tip izraza za koji se poziva print mora biti Int, Char ili Bool ", null);		  	     	
	}
	
	public void visit(ReadDesignator readDesignator){
		Obj designator = readDesignator.getDesignator().obj;
		if(designator.getKind()!= Obj.Var && designator.getKind()!= Obj.Elem && designator.getKind()!= Obj.Fld)
			report_error("Greska na liniji " + readDesignator.getLine() + " : " + "designator mora biti Elem, Var ili Fld ", null);
		if(designator==Tab.noObj || (designator.getKind()==Obj.Elem && designator.getType()==Tab.noType)){
			//uradjeno u Designator-u
		}
		else
		if(!designator.getType().equals(Tab.intType) && !designator.getType().equals(Tab.charType) && !designator.getType().equals(boolType))
			report_error("Greska na liniji " + readDesignator.getLine() + " : " + "tip mora biti Int, Char ili Bool ", null);
	}
	
	public void visit(DesignatorFunctionCall designatorFunctionCall){
		Obj func = designatorFunctionCall.getDesignator().obj;
		if (Obj.Meth == func.getKind()) { 
			report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + designatorFunctionCall.getLine(), null);
			
			
			Collection<Obj> localParameters = designatorFunctionCall.getDesignator().obj.getLocalSymbols();
			ArrayList<Obj> formalParameters = new ArrayList<Obj>();
			for(Obj localParam: localParameters){
				if(localParam.getFpPos()!=0)
					formalParameters.add(localParam);
				else break;
			}
			
			ArrayList<Struct> actualParamsList = new ArrayList<Struct>();
			
			ActualParsOptional apo = designatorFunctionCall.getActualParsOptional();
			if(apo instanceof Actuals) {
				ActualParamList apc = ((Actuals) apo).getActualParamList();
				while(apc != null){
					if(apc instanceof MultipleActualParams){
						actualParamsList.add(((MultipleActualParams)apc).getExpr().struct);
						apc = ((MultipleActualParams)apc).getActualParamList();
					}	
					else{
						actualParamsList.add(((SingleActualParam)apc).getExpr().struct);
						apc= null;
					}
				}
			}

			if(formalParameters.size() != actualParamsList.size())
				report_error("Greska na liniji " + designatorFunctionCall.getLine()+ " :broj formalnih i stvarnih argumenata u funkciji " + designatorFunctionCall.getDesignator().obj.getName() + " nije isti", null);
			else {
				for(int i=0; i<formalParameters.size(); i++){
					if(actualParamsList.get(i)!= formalParameters.get(i).getType()){
						report_error("Greska na liniji " + designatorFunctionCall.getLine()+ " :ne slazu se tipovi formalnih i stvarnih argumenata u pozivu funkcije " + designatorFunctionCall.getDesignator().obj.getName(), null);
						break;
					}
				}
			}

		} 
		else {
			if(func!=Tab.noObj) 
			 report_error("Greska na liniji " + designatorFunctionCall.getLine()+ " : ime " + func.getName() + " nije funkcija!", null);
		}
	}
	
	
	
	
	
	
	
	public void visit(ConditionFactor conditionFactor){
		if(conditionFactor.getExpr().struct != boolType){
			report_error("Semanticka greska: tip izraza u Condition-u mora biti bool" , conditionFactor);
			conditionFactor.struct = Tab.noType;
		}
		else 
			conditionFactor.struct = boolType;
	}
	
	public void visit(ConditionFactorRelop conditionFactorRelop){
		conditionFactorRelop.struct = boolType;
		if(conditionFactorRelop.getExpr().struct.getKind() == Struct.Array && conditionFactorRelop.getExpr1().struct.getKind() == Struct.Array
				&& (conditionFactorRelop.getRelop() instanceof RelEQ || conditionFactorRelop.getRelop() instanceof RelNotEQ)){
			//ok situacija
		}
		else if(conditionFactorRelop.getExpr().struct != conditionFactorRelop.getExpr1().struct){
			report_error("Semanticka greska: tipovi izraza u Condition-u moraju biti kompatibilni" , conditionFactorRelop);
			conditionFactorRelop.struct = Tab.noType;
		}
	}
	


	public boolean passed() {
		return !errorDetected;
	}
	
}

