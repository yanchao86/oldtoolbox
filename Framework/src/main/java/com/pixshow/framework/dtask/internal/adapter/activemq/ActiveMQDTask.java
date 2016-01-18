package com.pixshow.framework.dtask.internal.adapter.activemq;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;

import com.pixshow.framework.dtask.api.DTask;
import com.pixshow.framework.exception.api.SysException;

public class ActiveMQDTask implements DTask {

    private PooledConnectionFactory pooled;

    public ActiveMQDTask(String brokerURL, int idleTimeout, int maxConnections) {
        // ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
        this.pooled = new PooledConnectionFactory(new ActiveMQConnectionFactory(brokerURL));
        if (idleTimeout != -1) this.pooled.setIdleTimeout(idleTimeout);
        if (maxConnections != -1) this.pooled.setMaxConnections(maxConnections);
        this.pooled.start();
    }

    public void stop() {
        try {
            // Clean up
            this.pooled.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String createJob(String jobName, String data) {
        return send(jobName, data, null);
    }

    @Override
    public String createJob(String jobName, String data, String groupId) {
        return send(jobName, data, groupId);
    }

    private String send(String name, String text, String groupId) {
        Connection connection = null;
        Session session = null;
        try {

            connection = pooled.createConnection();
            // connection.start();
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(name);
            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            // Create a messages
            TextMessage message = session.createTextMessage(text);
            if (groupId != null) {
                message.setStringProperty("JMSXGroupID", groupId);
            }
            // producer.send(message, DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
            producer.send(message);
            return message.getJMSMessageID();
        } catch (Exception e) {
            throw new SysException(e);
        } finally {
            try {
                if (session != null) session.close();
            } catch (JMSException e) {
            }
            try {
                if (connection != null) connection.close();
            } catch (JMSException e) {
            }
        }
    }

}
