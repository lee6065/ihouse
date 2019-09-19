package cn.itcast.cargo.service.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.dao.cargo.PackingDao;
import cn.itcast.domain.cargo.Packing;
import cn.itcast.domain.cargo.PackingExample;
import cn.itcast.service.cargo.PackingService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class PackingServiceImpl implements PackingService {

    @Autowired
    private PackingDao packingDao;


    @Override
    public Packing findById(String id) {
        return packingDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Packing packing) {

         packingDao.insertSelective(packing);
    }

    @Override
    public void update(Packing packing) {

        packingDao.updateByPrimaryKeySelective(packing);
    }

    @Override
    public void delete(String id) {
        packingDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo findAll(PackingExample example, int page, int size) {
        PageHelper.startPage(page,size);
        List<Packing> list = packingDao.selectByExample(example);
        return new PageInfo(list);
    }

//    @Override
//    public List<Packing> findPackingByState(String companyId ,int i) {
//        return packingDao.findPackingByState(companyId,i);
//    }

    @Override
    public PageInfo<Packing> findByPage(String companyId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Packing> packingList = packingDao.findPackingByState(companyId,1);
        return new PageInfo<Packing>(packingList);
    }


}
