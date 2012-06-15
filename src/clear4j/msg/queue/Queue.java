package clear4j.msg.queue;

/**
 * User: alexb
 * Date: 15/06/12
 * Time: 15:56
 */
public class Queue {

    Node start;

    private static class Node {
        private Object next;
    }

    pop(){
        return start;
        start = next;
    }

}
