//package ReachingDefinition;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Pack;
import soot.PackManager;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootResolver;
import soot.Transform;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.options.Options;
import soot.tagkit.SourceLnPosTag;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.ExceptionalBlockGraph;
import soot.toolkits.graph.LoopNestTree;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Driver1 {
	
//	Reference: https://stackoverflow.com/a/6792566/6883821
	static int findMethods(String funcname, SootClass sc) {
        Body body = null;
        for(SootMethod m: sc.getMethods())
        {
        	if(m.getName().equals(funcname))
        	{
                if(m.isConcrete())
                {
                    body = m.retrieveActiveBody(); break;
                }
            }
        }
        
        int count = 0;
        LoopNestTree loopNestTree = new LoopNestTree(body);
        for (Loop loop : loopNestTree) {
        	int sz = loop.getLoopStatements().toString().length()/2;
        	count++;
//        	System.out.println(body);
        	System.out.println("loopstmts");
        	String str = loop.getLoopStatements().toString();
        	System.out.println(loop.getLoopStatements());
        	int pos = loop.getLoopStatements().toString().indexOf("goto");
        	if(pos>sz) {
        		System.out.println("Guess loop: do-while (sure)");
        	}
        	else {
        		if(str.indexOf("if")==1) {
        			System.out.println("Guess loop: For/While");
        		} else 
        		System.out.println("Guess loop: while -- with variable initialised somewhere else");
        	}
        }
        return count;
	}

	public static void main(String[] args) {
		
		Options.v().setPhaseOption("jb", "use-original-names:true");
		
		SootClass sc = Scene.v().loadClassAndSupport("Testing.Tester");

		System.out.println("Names of all the classes in an application");
		Chain<SootClass> l = Scene.v().getClasses();
		ArrayList<String> classes = new ArrayList<String>();
		
		for(SootClass sclass: l) {
			if(sclass.getName().contains("Testing")) {
				classes.add(sclass.getName());
			}
		}
		for(String str: classes) {
			System.out.print(str + " ");
		}
		
		System.out.println('\n');
//		System.out.println(Scene.v().getClasses().toString());
		for(String str: classes) {
			
			System.out.println("Class Name: " + str);
			sc = Scene.v().loadClassAndSupport(str);
			System.out.println();
			
	        List<String> listofmethods = new ArrayList<String>();
	        for (SootMethod method : sc.getMethods()) {
	        	if(!method.getName().contains("<")) {
	        		listofmethods.add(method.getName());
	        	}
	        }
	        
			System.out.println("Names of all the methods in a class.");
			System.out.println(listofmethods.toString());
			
			System.out.println();
			
			System.out.println("Print all data members of a class.");
			System.out.println(sc.getFields().toString());
	
	        System.out.println();
	        
	        System.out.println("Type of the arguments of methods.");
	        for (SootMethod method : sc.getMethods()) {
	        	if(!listofmethods.contains(method.getName())) {
	        		continue;
	        	}
	        	System.out.print("Function " + method.getName() + ": ");
	        	System.out.println(method.getParameterTypes().toString());
	        }
	        
	        
	        
	        System.out.println();
	        
			System.out.println("Loops: ");
	        for (SootMethod method : sc.getMethods()) {
	        	if(!listofmethods.contains(method.getName())) {
	        		continue;
	        	}
	        	System.out.print(method.getName() + ": ");
	        	System.out.println(findMethods(method.getName(), sc));
	        }
	        
	        System.out.println();
	        
	        System.out.println("Identify return statements and their return types.");
	        
	        for (SootMethod method : sc.getMethods()) {
	        	if(!listofmethods.contains(method.getName())) {
	        		continue;
	        	}
	        	System.out.println(method.getName() + ": " + method.getReturnType());
	        }

	        
	        System.out.println();
	        // References: https://gist.github.com/monperrus/1354641
	        System.out.println("Method calls with arguments.");
	        for (SootMethod method : sc.getMethods()) {
	        	if(!listofmethods.contains(method.getName())) {
	        		continue;
	        	}
	        	System.out.println(method.getName());
	        	Body mbody = method.getActiveBody();
	//        	System.out.println(mbody.toString());
	        	PatchingChain<Unit> units = mbody.getUnits();
	        	List invokeList = new ArrayList();
	        	List userdefinvoke = new ArrayList();
//	        	Chain<InvokeExpr> invokeList = new Chain<InvokeExpr>();
	        	
	        	for (Unit u: units) { // for each statement
	                Stmt s = (Stmt)u;
	                //System.out.println(s+"-"+s.getClass());
	//                if(s instanceof InvokeStmt) {
	//                	System.out.println(s);
	//                }
	                if (s.containsInvokeExpr()) {
	                  InvokeExpr invokeExpr = s.getInvokeExpr();
	                  invokeList.add(invokeExpr);
//	                  System.out.println(invokeExpr);
//	                  System.out.println(invokeExpr.getType() + " invokeexprtype " + invokeExpr);
	                  if (invokeExpr instanceof StaticInvokeExpr) {
//	                    InstanceInvokeExpr instanceInvokeExpr = (InstanceInvokeExpr) invokeExpr;
	                    userdefinvoke.add(invokeExpr);
//	                    System.out.print("Method: " + invokeExpr.getMethod().getName());
//	                    System.out.print(invokeExpr.getArgs().toString());
//	                    System.out.println();
	                    
	                    // we can add this method call
	//                    aVariable.methodCalls.add(instanceInvokeExpr.getMethod().getName());
	                  }
	                }
	        	}
	        	
//	        	System.out.println("User-defined methods: ");
	        	for(Object ink: invokeList) {
	        		InvokeExpr obj = (InvokeExpr) ink;
//	        		if(obj.getType() instanceof SpecialInvokeExpr) {
	        		String methodtype = obj.toString().split(" ")[0];
	        		if(methodtype.equals("staticinvoke"))
	        			System.out.println("user-def: " +obj.toString());
	        		else
	        			System.out.println(obj.toString());
	        	}
//	        	System.out.println();
//	        	System.out.println("User-defined method invocation: ");
//	        	for(Object ink: userdefinvoke) {
//	        		InvokeExpr obj = (InvokeExpr) ink;
////	        		if(obj.getType() instanceof SpecialInvokeExpr) {
//	        		String methodtype = obj.toString().split(" ")[0];
//	        		if(methodtype.equals("staticinvoke"))
//	        			System.out.println(obj.toString());
//	        	}
	        	System.out.println();
	        }
	        
	        System.out.println();
	        
	        System.out.println("Identify the local variables that were defined in every method.");
	        
	        int i=0;
	        for (SootMethod method : sc.getMethods()) {
	        	if(!listofmethods.contains(method.getName())) {
	        		continue;
	        	}
//	        	UnitGraph g = new BriefUnitGraph(method.getActiveBody());
	        	Body b = method.getActiveBody();
	        	System.out.println("Method: " + method.getName());
	        	Chain<Local> local = b.getLocals();
	        	for(Local loc: local) {
	        		System.out.println(loc.getName());
	        	}
	        }
	        System.out.println();
		}
	}
}
