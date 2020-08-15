import java.util.*;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class OuterBanks {

	public static int getMinLocations(Graph g) {
	   int total = 0;
	   Stack<Integer> fringe = new Stack<>();
	   HashSet<Integer> visited = new HashSet<>();
	   fringe.add(0);

	   for (int i= 0; i < g.vertexCount; i++) {
	       if (!visited.contains(i)) {
	           fringe.add(i);
	           DFS(g, fringe, visited);
	           total += 1;
	       }
	   }
	   return total;
	}

	private static void DFS(Graph g, Stack<Integer> fringe, HashSet<Integer> visited) {
	   while (!fringe.empty()) {
	       Integer vertex = fringe.pop();
	       if (!visited.contains(vertex)) {
	           visited.add(vertex);
	           for (Integer e : g.neighbors(vertex)) {
	               if (!visited.contains(e)) {
	                   fringe.add(e);
	               }
	           }
	       }
	   }
	}

}