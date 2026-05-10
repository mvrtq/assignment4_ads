import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class Graph {
    private final boolean directed;
    private final Map<Integer, Vertex> vertices;
    private final Map<Integer, Set<Integer>> adjacencyList;
    private final List<Edge> edges;

    public Graph(boolean directed) {
        this.directed = directed;
        this.vertices = new LinkedHashMap<>();
        this.adjacencyList = new LinkedHashMap<>();
        this.edges = new ArrayList<>();
    }

    public void addVertex(Vertex vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex must not be null");
        }

        int id = vertex.getId();

        if (!vertices.containsKey(id)) {
            vertices.put(id, vertex);
            adjacencyList.put(id, new LinkedHashSet<>());
        }
    }

    public void addEdge(int from, int to) {
        Vertex source = vertices.get(from);
        Vertex destination = vertices.get(to);

        if (source == null || destination == null) {
            throw new IllegalArgumentException("Both vertices must exist before adding an edge");
        }

        boolean added = adjacencyList.get(from).add(to);

        if (!added) {
            return;
        }

        edges.add(new Edge(source, destination));

        if (!directed) {
            adjacencyList.get(to).add(from);
        }
    }

    public void printGraph() {
        System.out.println("Graph representation (adjacency list, directed=" + directed + "):");

        for (Map.Entry<Integer, Set<Integer>> entry : adjacencyList.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    public void bfs(int start) {
        List<Integer> order = bfsTraversal(start);
        System.out.println("BFS order from " + start + ": " + order);
    }

    public void dfs(int start) {
        List<Integer> order = dfsTraversal(start);
        System.out.println("DFS order from " + start + ": " + order);
    }

    public List<Integer> bfsTraversal(int start) {
        validateStartVertex(start);

        List<Integer> order = new ArrayList<>();
        Set<Integer> visited = new LinkedHashSet<>();
        Queue<Integer> queue = new ArrayDeque<>();

        visited.add(start);
        queue.offer(start);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            order.add(current);

            // BFS uses a queue and visits vertices level by level.
            for (int neighbor : adjacencyList.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }

        return order;
    }

    public List<Integer> dfsTraversal(int start) {
        validateStartVertex(start);

        List<Integer> order = new ArrayList<>();
        Set<Integer> visited = new LinkedHashSet<>();
        Deque<Integer> stack = new ArrayDeque<>();

        stack.push(start);

        while (!stack.isEmpty()) {
            int current = stack.pop();

            if (visited.contains(current)) {
                continue;
            }

            visited.add(current);
            order.add(current);

            // DFS uses a stack and goes as deep as possible before backtracking.
            List<Integer> neighbors = new ArrayList<>(adjacencyList.get(current));
            Collections.reverse(neighbors);

            for (int neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }

        return order;
    }

    public int getVertexCount() {
        return vertices.size();
    }

    public int getEdgeCount() {
        return edges.size();
    }

    private void validateStartVertex(int start) {
        if (!vertices.containsKey(start)) {
            throw new IllegalArgumentException("Start vertex does not exist: " + start);
        }
    }
}