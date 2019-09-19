package cn.itcast.domain.system;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Dept implements Serializable {
    private String id;
    private String deptName;
//    private String parentId;
    private Dept parent; //父部门对象   当前部门和父部门的关系是多对一
    private Integer state;
    private String companyId;
    private String companyName;
}
