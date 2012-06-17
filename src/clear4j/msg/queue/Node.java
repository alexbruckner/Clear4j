package clear4j.msg.queue;

/**
 * User: alexb
 * Date: 15/06/12
 * Time: 15:56
 */
public final class Node<T> {

	private final T payload;
	private final Node<T> previous;
	private final Node<T> next;
	
	private Node (final Node<T> previous, final T payload, final Node<T> next) {
		this.previous = previous;
		this.payload = payload;
		this.next = next;
	}
	
	static final <T> Node<T> create(final T payload){
		return new Node<T>(null, payload, null);
	}
	
	static final <T> Node<T> append(final Node<T> node, final T payload){
		//create new interim node
		final Node<T> newNode = new Node<T>(node, payload, null);
		//create new previous node with new interim as next
		final Node<T> previousNode = new Node<T>(node.previous, node.payload, newNode);
		// return new Node with new previous node
		return new Node<T>(previousNode, payload, null);
		
		//TODO if only singly connected queue is required,
		//TODO then use: return new Node<T>(node, payload);
		
	}

	public final T getPayload() {
		return payload;
	}
	
	public final int getId(){
		return payload == null? -1 : payload.hashCode();
	}
	
	@Override
	public String toString(){
		Node<T> current = this;
		final StringBuilder sb = new StringBuilder();
		do {
			sb.append("Node[(")
				.append(current.getId()).append(")(")
				.append(current.previous == null ? -1 : current.previous.getId()).append(",")
				.append(current.getPayload()).append(",")
				.append(current.next == null ? -1 : current.next.getId())
				.append(")]\n");
			current = current.previous;
		} while(current != null);
		return sb.toString();
	}

}
