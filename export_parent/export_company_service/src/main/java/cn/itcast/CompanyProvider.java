package cn.itcast;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CompanyProvider {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        app.start();
        System.in.read();
    }
}
