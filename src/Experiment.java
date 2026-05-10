import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public final class Experiment {
    private static final int START_VERTEX = 0;

    private final Random random;
    private final List<TestResult> results;

    public Experiment() {
        this.random = new Random();
        this.results = new ArrayList<>();
    }

    public void runTraversals(Graph graph) {
        TestResult result = testGraph("Custom", graph, true);
        results.add(result);
    }

    public void runMultipleTests() {
        results.clear();

        int smallExtraEdges = random.nextInt(11) + 10;
        int mediumExtraEdges = random.nextInt(31) + 40;
        int largeExtraEdges = random.nextInt(101) + 200;

        Graph smallGraph = createGraph(10, false, smallExtraEdges);
        Graph mediumGraph = createGraph(30, false, mediumExtraEdges);
        Graph largeGraph = createGraph(100, false, largeExtraEdges);

        TestResult smallResult = testGraph("Small", smallGraph, true);
        TestResult mediumResult = testGraph("Medium", mediumGraph, false);
        TestResult largeResult = testGraph("Large", largeGraph, false);

        results.add(smallResult);
        results.add(mediumResult);
        results.add(largeResult);

        printResults();

        System.out.println();
        System.out.println("Small graph traversal order details:");
        System.out.println("BFS order: " + smallResult.getBfsOrder());
        System.out.println("DFS order: " + smallResult.getDfsOrder());
    }

    private TestResult testGraph(String graphName, Graph graph, boolean showDetails) {
        System.out.println();
        System.out.println("=== " + graphName + " graph ===");

        if (showDetails) {
            graph.printGraph();
        } else {
            System.out.println("Graph has " + graph.getVertexCount() + " vertices and " + graph.getEdgeCount() + " edges.");
            System.out.println("Traversal order is hidden for better readability.");
        }

        long bfsStart = System.nanoTime();
        List<Integer> bfsOrder = graph.bfsTraversal(START_VERTEX);
        long bfsEnd = System.nanoTime();

        long dfsStart = System.nanoTime();
        List<Integer> dfsOrder = graph.dfsTraversal(START_VERTEX);
        long dfsEnd = System.nanoTime();

        if (showDetails) {
            System.out.println("BFS order from " + START_VERTEX + ": " + bfsOrder);
            System.out.println("DFS order from " + START_VERTEX + ": " + dfsOrder);
        }

        return new TestResult(
                graph.getVertexCount(),
                graph.getEdgeCount(),
                bfsEnd - bfsStart,
                dfsEnd - dfsStart,
                bfsOrder,
                dfsOrder
        );
    }

    public void printResults() {
        System.out.println();
        System.out.println("Execution time summary:");
        System.out.printf("%-10s %-10s %-18s %-18s%n", "Vertices", "Edges", "BFS (ns)", "DFS (ns)");

        for (TestResult result : results) {
            System.out.printf(
                    Locale.US,
                    "%-10d %-10d %-18d %-18d%n",
                    result.getVertices(),
                    result.getEdges(),
                    result.getBfsTimeNanos(),
                    result.getDfsTimeNanos()
            );
        }
    }

    public Graph createGraph(int vertexCount, boolean directed, int extraEdges) {
        if (vertexCount <= 0) {
            throw new IllegalArgumentException("Vertex count must be positive");
        }

        if (extraEdges < 0) {
            throw new IllegalArgumentException("Extra edge count must be non-negative");
        }

        Graph graph = new Graph(directed);

        for (int i = 0; i < vertexCount; i++) {
            graph.addVertex(new Vertex(i));
        }

        for (int i = 0; i < vertexCount - 1; i++) {
            graph.addEdge(i, i + 1);
        }

        int added = 0;

        while (added < extraEdges) {
            int from = random.nextInt(vertexCount);
            int to = random.nextInt(vertexCount);

            if (from == to) {
                continue;
            }

            int before = graph.getEdgeCount();
            graph.addEdge(from, to);

            if (graph.getEdgeCount() > before) {
                added++;
            }
        }

        return graph;
    }

    public static final class TestResult {
        private final int vertices;
        private final int edges;
        private final long bfsTimeNanos;
        private final long dfsTimeNanos;
        private final List<Integer> bfsOrder;
        private final List<Integer> dfsOrder;

        public TestResult(
                int vertices,
                int edges,
                long bfsTimeNanos,
                long dfsTimeNanos,
                List<Integer> bfsOrder,
                List<Integer> dfsOrder
        ) {
            this.vertices = vertices;
            this.edges = edges;
            this.bfsTimeNanos = bfsTimeNanos;
            this.dfsTimeNanos = dfsTimeNanos;
            this.bfsOrder = new ArrayList<>(bfsOrder);
            this.dfsOrder = new ArrayList<>(dfsOrder);
        }

        public int getVertices() {
            return vertices;
        }

        public int getEdges() {
            return edges;
        }

        public long getBfsTimeNanos() {
            return bfsTimeNanos;
        }

        public long getDfsTimeNanos() {
            return dfsTimeNanos;
        }

        public List<Integer> getBfsOrder() {
            return new ArrayList<>(bfsOrder);
        }

        public List<Integer> getDfsOrder() {
            return new ArrayList<>(dfsOrder);
        }
    }
}