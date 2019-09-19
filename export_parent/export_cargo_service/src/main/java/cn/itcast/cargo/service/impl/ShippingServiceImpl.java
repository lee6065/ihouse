package cn.itcast.cargo.service.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.dao.cargo.ShippingDao;
import cn.itcast.domain.cargo.Shipping;
import cn.itcast.domain.cargo.ShippingExample;
import cn.itcast.service.cargo.ShippingService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingDao shippingDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ContractProductDao contractProductDao;

    @Override
    public Shipping findById(String id) {
        return shippingDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Shipping shipping) {

         shippingDao.insertSelective(shipping);
    }

    @Override
    public void update(Shipping shipping) {

        shippingDao.updateByPrimaryKeySelective(shipping);
    }

    @Override
    public void delete(String id) {
        shippingDao.deleteByPrimaryKey(id);
    }



    @Override
    public PageInfo<Shipping> findByPage(String companyId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> list = shippingDao.findAll(companyId);
        return new PageInfo<Shipping>(list);
    }


}
