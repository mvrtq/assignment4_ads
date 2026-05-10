public final class Edge {
    private final Vertex source;
    private final Vertex destination;

    public Edge(Vertex source, Vertex destination) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Source and destination must not be null");
        }
        this.source = source;
        this.destination = destination;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "Edge{" + "source=" + source.getId() + ", destination=" + destination.getId() + '}';
    }
}
