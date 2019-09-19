package cn.itcast.service.system.impl;

import cn.itcast.dao.system.UserDao;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination mailQueue;
    @Override
    public void save(User user) {
        String password = user.getPassword();

        String password1 = user.getPassword();
       /* zhangsan      123456
         lisi         123456*/
//        密码加密
        password = new Md5Hash(password, user.getEmail(), 10).toString();//p1: 原密码  p2:盐 料  p3:散次列 加几次
        user.setPassword(password);
        userDao.save(user);

//        把发送邮件任务放入到mq中


//
//        (String to ,String subject ,String content)
//                "to",user.getEmail()
//                "subject":"入驻saas平台通知"
//                "content":"用户可以使用邮箱和密码登录项目了，密码是：XXX"
//        把发送邮件的消息放入到mq中
        jmsTemplate.send(mailQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("to",user.getEmail());
                mapMessage.setString("subject","入驻saas平台通知");
                mapMessage.setString("content","用户可以使用邮箱和密码登录项目了，密码是："+password1);
                return mapMessage;
            }
        });

    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void delete(String id) {
        userDao.delete(id);
    }

    @Override
    public User findById(String id) {
        return userDao.findById(id);
    }

    @Override
    public PageInfo findPage(String companyId, int page, int size) {
        PageHelper.startPage(page,size);
        List<User> list = userDao.findAll(companyId);
        return new PageInfo(list);
    }

    @Override
    public void changeRole(String userid, String[] roleIds) {

//        先删除此用户下之前的角色  delete from pe_role_user where user_id=#{userid}
        userDao.deleteRoleByUserid(userid);

        if(roleIds!=null&&roleIds.length>0){
            for (String roleId : roleIds) {
                //        给用户分配角色，本质上就是向中间表中插入数据
                userDao.changeRole(userid,roleId);   // insert into pe_role_user (user_id,role_id) values(#{userid},#{roleid})
            }

        }


    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public static void main(String[] args) {
        String string = new Md5Hash("123456", "xiaoer@export.com", 10).toString();
        System.out.println(string);
    }

//    22f1719dfc7634fde662a6c0c43ac6a4


   /* @Scheduled(cron = "0 0 6 * * ?")
    public void sendBirthdayInfo(){
        List<User> userList =null;
        for (User user : userList) {
            MailUtil.sendMsg(user.getEmail(),"生日祝福","");
        }
    }*/
}
