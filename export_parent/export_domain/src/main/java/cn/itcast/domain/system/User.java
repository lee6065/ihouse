package cn.itcast.domain.system;

import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.GET;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class User implements Serializable {

    private String id    ;
    private String deptId    ;
    private String email     ;
    private String userName      ;
    private String station   ;
    private String password      ;
    private String state     ;
    private String companyId  ;
    private String companyName;
    private String deptName      ;
    private String managerId  ;
    private String gender    ;
    private String telephone  ;
    private String birthday      ;
    /*      0 作为内部控制，租户企业不能使用
            0-saas 管理员
            1-企业管理员
            2-管理所有下属部门和人员
            3-管理本部门
            4-普通员工
    */
    private Integer degree   ;
    private Double salary    ;
    private String joinDate      ;
    private Integer orderNo      ;
    private String createBy      ;
    private String createDept;
    private Date createTime;
    private String updateBy      ;
    private Date updateTime;
    private String remark    ;


}
