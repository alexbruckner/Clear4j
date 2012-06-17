package clear4j.msg.queue;

public final class Queue<T> {
	private final String name; 
	private final Node<T> last;

	private Queue(final String name, final Node<T> last) {
		this.name = name;
		this.last = last;
	}
	
	static <T> Queue<T> create(String name) {
		return new Queue<T>(name, null);
	}
	
	static <T> Queue<T> add(Queue<T> queue, T payload) {
		if (queue.last == null) {
			return new Queue<T>(queue.name, Node.create(payload));
		} else {
			return new Queue<T>(queue.name, Node.append(queue.last, payload));
		}
	}
}
