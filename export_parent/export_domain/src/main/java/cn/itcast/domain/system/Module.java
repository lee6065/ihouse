package cn.itcast.domain.system;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class Module implements Serializable {

    private String id   ;
    private String parentId  ;
    private String parentName;
    private String name      ;
    private int layerNum  ;
    private int isLeaf    ;
    private String ico       ;
    private String cpermission;
    private String curl      ;
    private String ctype     ;
    private String state     ;
    private String belong    ;
    private String cwhich    ;
    private int quoteNum  ;
    private String remark    ;
    private int orderNo   ;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return Objects.equals(id, module.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
