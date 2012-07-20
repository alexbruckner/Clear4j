package clear4j.msg.beans;

import clear4j.msg.queue.beans.monitor.DefaultQueueStatus;
import clear4j.msg.queue.monitor.QueueStatus;

public class ExtendedQueueStatus extends DefaultQueueStatus implements QueueStatus {

	private final int messageCount;

	public ExtendedQueueStatus(QueueStatus status, int messageCount) {
		super(status.getQueue(), status.getReceivers());
		this.messageCount = messageCount;
	}

	public int getMessageCount() {
		return messageCount;
	}

}
