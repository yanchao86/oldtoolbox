package com.pixshow.framework.dtask.internal.adapter.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.pixshow.framework.dtask.api.DWorker;

public class ExecuterMessageListener implements MessageListener {

    private DWorker worker;

    public ExecuterMessageListener(DWorker worker) {
        this.worker = worker;
    }

    @Override
    public void onMessage(Message message) {
        
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                String text = textMessage.getText();
                this.worker.execute(text);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        
        try {
            message.acknowledge();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
