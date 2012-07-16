package clear4j.msg.beans;

import clear4j.msg.queue.beans.monitor.DefaultQueueStatus;
import clear4j.msg.queue.monitor.QueueStatus;

import java.io.Serializable;

public class ExtendedQueueStatus<T extends Serializable> extends DefaultQueueStatus<T> implements QueueStatus<T> {

	private final int messageCount;

	public ExtendedQueueStatus(QueueStatus<T> status, int messageCount) {
		super(status.getQueue(), status.getReceivers());
		this.messageCount = messageCount;
	}

	public int getMessageCount() {
		return messageCount;
	}

}
