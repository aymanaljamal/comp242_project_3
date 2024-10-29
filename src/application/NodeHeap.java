package application;

public class NodeHeap<A extends Comparable<A>> implements Comparable<NodeHeap<A>> {
    private A date;

    public NodeHeap(A date) {
		super();
		this.date = date;
	}

	public A getDate() {
        return date;
    }

    public void setDate(A date) {
        this.date = date;
    }

    @Override
    public int compareTo(NodeHeap<A> o) {
        if (this.date instanceof Martyr && o.date instanceof Martyr) {
            Martyr m1 = (Martyr) this.date;
            Martyr m2 = (Martyr) o.date;
            return Integer.compare(m1.getAge(), m2.getAge());
        }
        throw new IllegalArgumentException("Date must be of type Martyr");
    }
}
