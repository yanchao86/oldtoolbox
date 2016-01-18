package com.pixshow.framework.dtask.internal.adapter.activemq;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.pixshow.framework.config.Config;
import com.pixshow.framework.dtask.api.DWorker;
import com.pixshow.framework.dtask.api.DWorkerLaunch;
import com.pixshow.framework.exception.api.SysException;
import com.pixshow.framework.plugin.api.PluginDefinition;
import com.pixshow.framework.plugin.api.PluginRegisterManager;
import com.pixshow.framework.utils.StringUtility;

public class ActiveMQDWorkerLaunch implements DWorkerLaunch {

    private Log                   log                 = LogFactory.getLog(getClass());

    private ApplicationContext    applicationContext;

    private List<MessageConsumer> messageConsumerList = new ArrayList<MessageConsumer>();
    private List<Session>         sessionList         = new ArrayList<Session>();
    private List<Connection>      connectionList      = new ArrayList<Connection>();

    @Override
    public void init(ApplicationContext applicationContext) {
        try {
            this.applicationContext = applicationContext;

            String brokerURL = Config.getInstance().getString("dtask.server.activemq.brokerURL");
            int perSessionCount = Config.getInstance().getInteger("dtask.server.activemq.perSessionCount", 10);

            if (StringUtility.isEmpty(brokerURL)) {
                log.warn("【任务执行端】启动ActiveMQ分布式任务系统失败，未设置服务器地址!");
                return;
            }

            List<PluginDefinition> defs = PluginRegisterManager.getInstance().getDefinitions(DWorker.class);
            if (defs == null || defs.isEmpty()) {
                log.info("【任务执行端】没有搜索到执行任务类。");
                return;
            }
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);

            for (PluginDefinition def : defs) {
                DWorker worker = (DWorker) this.applicationContext.getBean(def.getBeanName());
                onMessage(connectionFactory, def.getAnnotation().name(), new ExecuterMessageListener(worker), perSessionCount);

                log.info("【任务执行端】监听分布式任务 " + def.getAnnotation().name() + "," + def.getBeanName());
            }

            log.info("【任务执行端】启动ActiveMQ分布式任务系统 ! 连接到" + brokerURL);

        } catch (Exception e) {
            shutdown();
            throw new SysException(e);
        }
    }

    @Override
    public void shutdown() {
        for (MessageConsumer consumer : messageConsumerList) {
            try {
                consumer.close();
            } catch (JMSException e) {
            }
        }

        for (Session session : sessionList) {
            try {
                session.close();
            } catch (JMSException e) {
            }
        }

        for (Connection connection : connectionList) {
            try {
                connection.close();
            } catch (JMSException e) {
            }
        }
    }

    private void onMessage(ActiveMQConnectionFactory connectionFactory, String queueName, MessageListener listener, int perSessionCount) throws Exception {
        // Create a Connection
        Connection connection = connectionFactory.createConnection();
        connection.start();
        connectionList.add(connection);

        for (int i = 0; i < perSessionCount; i++) {
            // Create a Session
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(queueName);
            // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(listener);
            messageConsumerList.add(consumer);
            sessionList.add(session);
        }

    }

}
