package clear4j.msg.beans.monitor;

import clear4j.msg.Monitor;
import clear4j.msg.queue.monitor.Callback;
import clear4j.msg.queue.monitor.QueueStatus;

import javax.swing.*;
import java.util.Set;

public class MonitorFrame extends JFrame {

    private JTextArea area;
    private final Monitor monitor;

    public MonitorFrame(){
        setTitle("Clear4j Queue Monitor");
        this.setSize(300,500);
        area  = new JTextArea();
        this.getContentPane().add(area);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        monitor = new Monitor(new Callback() {
            @Override
            public void call(Set<QueueStatus> status) {
                StringBuilder sb = new StringBuilder();
                for (QueueStatus queueStatus : status){
                    sb.append(queueStatus.getQueue()).append("(").append(queueStatus.getReceivers().size()).append(")").append("\n");
                }
                area.setText(sb.toString());
            }
        });
        monitor.start();
    }

}
