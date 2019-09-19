package cn.itcast.stat;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StatProvider {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        app.start();
        System.in.read();

    }
}
