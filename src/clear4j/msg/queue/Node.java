package clear4j.msg.queue;

/**
 * User: alexb
 * Date: 15/06/12
 * Time: 15:56
 */
public final class Node<T> {

	private final T payload;
	private final Node<T> previous;

	private Node (final Node<T> previous, final T payload) {
		this.previous = previous;
		this.payload = payload;
	}
	
	static <T> Node<T> create(final T payload){
		return new Node<T>(null, payload);
	}
	
	static <T> Node<T> append(final Node<T> node, final T payload){
		return new Node<T>(node, payload);
	}

	public final T getPayload() {
		return payload;
	}
	
	public final int getId(){
		return payload == null? -1 : payload.hashCode();
	}

    public Node<T> getPrevious() {
        return previous;
    }

    @Override
	public String toString(){
		Node<T> current = this;
		final StringBuilder sb = new StringBuilder();
		do {
			sb.append("Node[(")
				.append(current.getId()).append(")(")
				.append(current.previous == null ? -1 : current.previous.getId()).append(",")
				.append(current.getPayload()).append(")]\n");
			current = current.previous;
		} while(current != null);
		return sb.toString();
	}

}
