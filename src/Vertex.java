public final class Vertex {
    private final int id;

    public Vertex(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Vertex id must be non-negative");
        }

        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}