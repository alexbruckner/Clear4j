package clear4j.msg.beans.monitor;

import clear4j.msg.Monitor;
import clear4j.msg.beans.ExtendedQueueStatus;
import clear4j.msg.queue.monitor.Callback;

import javax.swing.*;
import java.util.Set;

public class MonitorFrame extends JFrame {

    private JTextArea area;
    private final Monitor monitor;

    public MonitorFrame(int frequency){
        setTitle("Clear4j Queue Monitor");
        this.setSize(300,500);
        area  = new JTextArea();
        this.getContentPane().add(area);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        monitor = new Monitor(frequency, new Callback<ExtendedQueueStatus>() {
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
