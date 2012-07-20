package clear4j.msg.beans.monitor;

import clear4j.msg.Monitor;
import clear4j.msg.beans.ExtendedQueueStatus;
import clear4j.msg.queue.monitor.Callback;

import javax.swing.*;
import java.util.Set;

public class MonitorFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JTextArea area;
    private final Monitor<ExtendedQueueStatus> monitor;

    public MonitorFrame(int frequency, final String... queues){
        setTitle("Clear4j Queue Monitor");
        this.setSize(300,500);
        area  = new JTextArea();
        this.getContentPane().add(area);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        monitor = new Monitor<ExtendedQueueStatus>(frequency, queues, new Callback<ExtendedQueueStatus>() {
            @Override
            public void call(Set<ExtendedQueueStatus> status) {
                StringBuilder sb = new StringBuilder();
                for (ExtendedQueueStatus queueStatus : status){
                    sb.append(queueStatus.getQueue())
                    	.append("(").append(queueStatus.getReceivers().size()).append(")")
                    	.append("(").append(queueStatus.getMessageCount()).append(")")
                    	.append("\n");
                }
                area.setText(sb.toString());
            }
        });
        monitor.start();
    }

    public void dispose(){
        monitor.stop();
        super.dispose();
    }

}
