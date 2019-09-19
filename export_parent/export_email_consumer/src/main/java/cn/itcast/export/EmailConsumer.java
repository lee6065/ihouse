package cn.itcast.export;

import cn.itcast.utils.MailUtil;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class EmailConsumer implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            MapMessage mapMessage = (MapMessage) message;
            String to = mapMessage.getString("to");
            String subject = mapMessage.getString("subject");
            String content = mapMessage.getString("content");
            MailUtil.sendMsg(to,subject,content);
            System.out.println("邮件发送成功............");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
