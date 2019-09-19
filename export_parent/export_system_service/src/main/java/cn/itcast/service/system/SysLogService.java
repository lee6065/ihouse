package cn.itcast.service.system;

import cn.itcast.domain.system.SysLog;
import com.github.pagehelper.PageInfo;


public interface SysLogService {

    void save(SysLog sysLog);

    PageInfo<SysLog> findByPage(String companyId, Integer pageNum, Integer pageSize);
}
