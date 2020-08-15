import java.util.*;
/**
 * Solution for CS61BL Lab21, summer 2015.
 * @author Armani Ferrante
 */
public class Graph implements Iterable<Integer> {

    /**
     *  The adjacency list for this graph.
     */
    private LinkedList<Edge>[] adjLists;

    /**
     *  The number of vertices in this graph.
     */
    public int vertexCount;
    public int numVertices;

    /**
     *  My starting vertex for a dfs traversal.
     */
    private int startVertex;

    /**
     *  Mapping of vertex number (array index) to
     *  in degree.
     */
    private int[] verticesToInDegree;

    /**
     *  Initialize a graph with the given number of vertices and no edges.
     */
    public Graph(int numVertices) {
        adjLists = new LinkedList[numVertices];
        startVertex = 0;
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
        this.numVertices = numVertices;
        verticesToInDegree = new int[numVertices];
    }

    /**
     *  Change the vertex the iterator will start DFS from
     */
    public void setStartVertex(int v){
        if (v < vertexCount && v >= 0){
            startVertex = v;
        } else {
            throw new IllegalArgumentException("Cannot set iteration "
                    + "start vertex to " + v
                    + ".");
        }
    }

    /**
     *  Removes vertex from this graph and all incident edges.
     *  Removing a vertex doesn't change the numbers of any
     *  other vertex.
     */
    public void removeVertex(int vertex) {
        for (int v = 0; v < adjLists.length; v += 1) {
            if (v != vertex && adjLists[v] != null) {
                for (Edge e : adjLists[v]) {
                    if (e.to() == vertex) {
                        adjLists[v].remove(e);
                    }
                }
            } else if (adjLists[v] != null) {
                for (Edge e : adjLists[v]) {
                    verticesToInDegree[e.to()] -= 1;
                }
            }
        }
        verticesToInDegree[vertex] = 0;
        adjLists[vertex] = null;
    }

    /**
     *  Add to the graph a directed edge from vertex v1 to vertex v2.
     */
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, null);
    }

    /**
     *  Add to the graph an undirected edge from vertex v1 to vertex v2.
     */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, null);
    }

    /**
     *  Add to the graph a directed edge from vertex v1 to vertex v2,
     *  with the given edge information. If the edge already exists,
     *  replaces the current edge with a new edge with edgeInfo.
     */
    public void addEdge(int v1, int v2, Object edgeInfo) {
        if (isAdjacent(v1, v2)) {
            addEdgeReplace(v1, v2, edgeInfo);
        } else {
            addEdgeNew(v1, v2, edgeInfo);
            verticesToInDegree[v2] += 1;
        }
    }

    /**
     *  Replaces (v1, v2) info with a new edge with edgeInfo,
     *  assumes the edge already exists in this graph.
     */
    private void addEdgeReplace(int v1, int v2, Object edgeInfo) {
        Edge e = new Edge(v1, v2, edgeInfo);
        for (int k = 0; k < adjLists[v1].size(); k += 1) {
            Edge oldEdge = adjLists[v1].get(k);
            if (oldEdge.to() == v2) {
                adjLists[v1].remove(oldEdge);
            }
        }
        adjLists[v1].add(e);
    }

    /**
     *  Adds edge (v1, v2) to this graph with edgeInfo.
     *  Assumes such an edge is not in this graph.
     */
    private void addEdgeNew(int v1, int v2, Object edgeInfo) {
        Edge e = new Edge(v1, v2, edgeInfo);
        adjLists[v1].add(e);
    }

    /**
     *  Add to the graph an undirected edge from vertex v1 to vertex v2,
     *  with the given edge information. If the edge already exists,
     *  replaces the current edge with a new edge with edgeInfo.
     */
    public void addUndirectedEdge(int v1, int v2, Object edgeInfo) {
        addEdge(v1, v2, edgeInfo);
        addEdge(v2, v1, edgeInfo);
    }

    /**
     *  Return true if there is an edge from vertex "from" to vertex "to";
     *  return false otherwise.
     */
    public boolean isAdjacent(int from, int to) {
        for (Edge e : adjLists[from]) {
            if (e.to() == to) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Returns a list of all the vertex neighbors of VERTEX.
     */
    public List<Integer> neighbors(int vertex) {
        ArrayList<Integer> neighbors = new ArrayList<>();
        for (Edge e : adjLists[vertex]) {
            neighbors.add(e.to());
        }
        return neighbors;
    }

    /**
     *  Return the number of incoming vertices for the given vertex,
     *  i.e. the number of vertices v such that (v, vertex) is an edge.
     */
    public int inDegree(int vertex) {
        return verticesToInDegree[vertex];
    }

    /**
     * Maps vertex to backwards cost.
     */
    double[] vertexToCost;

    /**
     * Maps vertex to previous in dijkstra shortest path.
     */
    int[] vertexToPrev;

    /**
     * Returns an array list of the shortestx path from STARTVERTEX
     * to ENDVERTEX, using Dijkstra's algorithm.
     */
    public ArrayList<Integer> shortestPath (int startVertex,
                                            int endVertex) {

        vertexToCost = new double[adjLists.length];
        vertexToPrev = new int[adjLists.length];

        PriorityQueue<Integer> fringe
                = new PriorityQueue(adjLists.length, new DijComparator());

        for (int k = 0; k < adjLists.length; k += 1) {
            if (k != startVertex) {
                vertexToCost[k] = Double.POSITIVE_INFINITY;
            } else {
                vertexToCost[k] = 0;
            }
            fringe.add(k);
            vertexToPrev[k] = -1;
        }

        while (!fringe.isEmpty()) {
            int node = fringe.poll();

            if (endVertex == node) {
                break;
            }

            for (int neighbor : neighbors(node)) {
                int edgeWeight
                        = (Integer) getEdge(node, neighbor).edgeInfo;
                if (vertexToCost[node] + edgeWeight
                        < vertexToCost[neighbor]) {
                    vertexToCost[neighbor]
                            = vertexToCost[node] + edgeWeight;
                    vertexToPrev[neighbor] = node;
                    fringe.remove(neighbor);
                    fringe.add(neighbor);
                }
            }
        }

        return prevToPath(vertexToPrev, endVertex, startVertex);
    }

    private Edge getEdge(int v1, int v2) {
        for (Edge e : adjLists[v1]) {
            if (e.to == v2) {
                return e;
            }
        }
        return null;
    }

    public ArrayList<Integer> prevToPath(int[] vertexToPrev,
                                         int endVertex,
                                         int startVertex) {
        LinkedList<Integer> lst = new LinkedList<>();
        int nextVertex = endVertex;
        while (nextVertex != startVertex) {
            lst.addFirst(nextVertex);
            nextVertex = vertexToPrev[nextVertex];
        }
        lst.addFirst(nextVertex);
        return new ArrayList<Integer>(lst);
    }

    private class DijComparator implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            if (vertexToCost[a] < vertexToCost[b]) {
                return -1;
            } else if (vertexToCost[a] == vertexToCost[b]) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    /**
     * Returns an iterator over all vertices of my graph in topolgical
     * sort order.
     */
    public Iterator<Integer> iterator(){
        return new TopologicalIterator();
    }

    /**
     *  A class that iterates through the vertices of this graph,
     *  starting with a given vertex. Does not necessarily iterate
     *  through all vertices in the graph: if the iteration starts
     *  at a vertex v, and there is no path from v to a vertex w,
     *  then the iteration will not include w.
     */
    private class DFSIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;

        public DFSIterator(Integer start) {
            fringe = new Stack<>();
            visited = new HashSet<>();
            fringe.push(start);
        }

        public boolean hasNext() {
            if (!fringe.isEmpty()) {
                int i = fringe.pop();
                while (visited.contains(i)) {
                    if (fringe.isEmpty()) {
                        return false;
                    }
                    i = fringe.pop();
                }
                fringe.push(i);
                return true;
            }
            return false;
        }

        public Integer next() {
            int curr = fringe.pop();
            ArrayList<Integer> lst = new ArrayList<>();
            for (int i : neighbors(curr)) {
                lst.add(i);
            }
            lst.sort((Integer i1, Integer i2) -> -(i1 - i2));
            for (Integer e : lst) {
                fringe.push(e);
            }
            visited.add(curr);
            return curr;
        }

        //ignore this method
        public void remove() {
            throw new UnsupportedOperationException(
                    "vertex removal not implemented");
        }

    }

    /**
     *  Return the collected result of iterating through this graph's
     *  vertices as an ArrayList.
     */
    public ArrayList<Integer> visitAll(int startVertex) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(startVertex);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /**
     * Returns true iff there exists a DFS path from STARTVERTEX
     * to STOPVERTEX.
     */
    public boolean pathExists(int startVertex, int stopVertex) {
        for (int vertex : visitAll(startVertex)) {
            if (vertex == stopVertex) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an arraylist of the DFS path from STARTVERTEX to STOPVERTEX.
     */
    public ArrayList<Integer> path(int startVertex, int stopVertex) {
        if (!pathExists(startVertex, stopVertex)) {
            return new ArrayList<Integer>();
        } else {
            ArrayList<Integer> visited = new ArrayList<>();
            Iterator<Integer> iter = new DFSIterator(startVertex);

            while (iter.hasNext()) {
                int next = iter.next();
                visited.add(next);
                if (next == stopVertex) {
                    break;
                }
            }

            return constructPath(startVertex, stopVertex, visited);
        }
    }

    /**
     * Utility method for the path(int, int) method.
     */
    private ArrayList<Integer> constructPath(int start, int stop,
                                             ArrayList<Integer> visited) {


        if (start == stop) {
            ArrayList<Integer> path = new ArrayList<Integer>();
            path.add(start);
            return path;
        }

        LinkedList<Integer> thePath = new LinkedList<>();
        int previous = stop;
        thePath.addFirst(previous);
        do {
            for (int vertex : visited) {
                if (isAdjacent(vertex, previous)) {
                    thePath.addFirst(vertex);
                    previous = vertex;
                    break;
                }
            }
        } while (previous != start);
        return new ArrayList<Integer>(thePath);
    }

    /**
     * Returns an arraylist containing the topological sort
     * on this graph.
     */
    public ArrayList<Integer> topologicalSort() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        /**
         * Unexplored vertices in the topolgical srot.
         */
        private Stack<Integer> fringe;

        /**
         * Mapping from vertex number to in degree.
         */
        private int[] currentInDegree;

        /**
         * Constructs a new topolgical iterator.
         */
        public TopologicalIterator() {
            fringe = new Stack<Integer>();
            currentInDegree = new int[vertexCount];
            for (int v = 0; v < currentInDegree.length; v += 1) {
                int degree = inDegree(v);
                currentInDegree[v] = degree;
                if (degree == 0) {
                    fringe.push(v);
                }
            }
        }

        /**
         * Returns true iff there is another vertex next in the
         * topological sort.
         */
        public boolean hasNext() {
            return !fringe.isEmpty();
        }

        /**
         * Returns the next vertex in the topological sort.
         */
        public Integer next() {
            int next = fringe.pop();
            for (int neighbor : neighbors(next)) {
                currentInDegree[neighbor] -= 1;
                if (currentInDegree[neighbor] == 0) {
                    fringe.push(neighbor);
                }
            }
            return next;
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "vertex removal not implemented");
        }

    }

    /**
     * Edges (u, v) in my graph edgeInfo is used for Dijkstra's.
     */
    private class Edge {

        /**
         * The vertex u in this edge, (u, v).
         */
        private Integer from;

        /**
         * The vertex u in this edge, (u, v).
         */
        private Integer to;

        /**
         * The information, or "weight", of this edge.
         */
        private Object edgeInfo;

        /**
         * Designated constructor for an edge.
         */
        public Edge(int from, int to, Object info) {
            this.from = new Integer(from);
            this.to = new Integer(to);
            this.edgeInfo = info;
        }

        /**
         * Getter for u in the edge (u, v).
         */
        public Integer to() {
            return to;
        }

        /**
         * Getter for my edges info.
         */
        public Object info() {
            return edgeInfo;
        }

        @Override
        public String toString() {
            return "(" + from + "," + to + ",dist=" + edgeInfo + ")";
        }

    }
}