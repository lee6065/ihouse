package cn.itcast.service.cargo;

import cn.itcast.domain.cargo.Shipping;
import cn.itcast.domain.cargo.ShippingExample;
import com.github.pagehelper.PageInfo;


public interface ShippingService {

    Shipping findById(String id);

    void save(Shipping shipping);

    void update(Shipping shipping);

    void delete(String id);




    PageInfo<Shipping> findByPage(String companyId, Integer pageNum, Integer pageSize);
}
