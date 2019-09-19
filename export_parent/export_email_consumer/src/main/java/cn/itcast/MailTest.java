package cn.itcast;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MailTest {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-mq.xml");
        app.start();
        System.in.read();
    }
}
