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
	
	static final <T> Node<T> create(final T payload){
		return new Node<T>(null, payload);
	}
	
	static final <T> Node<T> append(final Node<T> node, final T payload){
		return new Node<T>(node, payload);
	}

	public final T getPayload() {
		return payload;
	}

}
