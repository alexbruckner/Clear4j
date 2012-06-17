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
	
	static <T> Queue<T> push(Queue<T> queue, T payload) {
		return new Queue<T>(
				queue.name, 
				queue.last == null ? Node.create(payload) 
				  	                : Node.append(queue.last, payload));
	}
	
	@Override
	public String toString(){
		return String.format("<<Queue:%s\n%s>>\n", name, last);
	}
	
}
