package clear4j.msg.beans;

import java.io.Serializable;

import clear4j.msg.queue.beans.monitor.DefaultQueueStatus;
import clear4j.msg.queue.monitor.QueueStatus;

public class ExtendedQueueStatus<T extends Serializable> extends DefaultQueueStatus<T> {

	private final int messageCount;

	public ExtendedQueueStatus(QueueStatus<T> status, int messageCount) {
		super(status.getQueue(), status.getReceivers());
		this.messageCount = messageCount;
	}

	public int getMessageCount() {
		return messageCount;
	}

}
