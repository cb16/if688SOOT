import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.util.dot.DotGraph;
import soot.util.dot.DotGraphConstants;


public class Principal extends SceneTransformer implements DotGraphConstants {

	protected void internalTransform(String arg0, Map arg1) {
		CallGraph callGraph = Scene.v().getCallGraph();
				
		//coloca no set as arestas que são relacionadas (chamadas pelo) main 
		//Set<SootMethod> mts = new HashSet<SootMethod>(); // não usado
		Set<Edge> usedEdges = new HashSet<Edge>();
		
		for(SootClass myClasses : Scene.v().getApplicationClasses()) {
			System.out.println("main class methods with no use?");
			for(SootMethod met : myClasses.getMethods()) {
				Iterator<Edge> edges = callGraph.edgesOutOf(met);
				while (edges.hasNext()) {
					Edge edge = edges.next();
					usedEdges.add(edge);
					SootMethod calleeMethod = (SootMethod) edge.getTgt();
					//mts.add(calleeMethod);
					//mts.add((SootMethod) edge.getTgt());
				}
			}
		}
		
		createGraph(usedEdges);
	}
	
	public void createGraph(Set<Edge> edges) {
		DotGraph d = new DotGraph("Call Graph Compiladores");
		
		for(Edge e : edges) {
			d.drawNode(((SootMethod) e.getSrc()).getSignature());
			d.drawNode(((SootMethod) e.getTgt()).getSignature());
			d.drawEdge(((SootMethod) e.getSrc()).getSignature(), ((SootMethod) e.getTgt()).getSignature());
			//System.out.println("Making edge: " + ((SootMethod) e.getSrc()).getSignature() + " -> " + ((SootMethod) e.getTgt()).getSignature());
		}
		
		OutputStream op;
		try {
			op = new FileOutputStream("result.dot");
			d.render(op, 0);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Principal compareCallGraph = new Principal();
		Transform instrumentTransformer = new Transform("wjtp.Principal", compareCallGraph);
		PackManager.v().getPack("wjtp").add(instrumentTransformer);
		soot.Main.main(args);
	}

}
