package clear4j.msg.queue;

import java.util.Iterator;

public final class Queue<T> implements Iterable<T> {
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
    public String toString() {
        return String.format("<<Queue:%s\n%s>>\n", name, last);
    }

    @Override
    public Iterator<T> iterator() {
        return new QueueIterator();
    }

    private final class QueueIterator implements Iterator<T> {

        Node<T> current = last;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            T payload = current.getPayload();
            current = current.getPrevious();
            return payload;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
