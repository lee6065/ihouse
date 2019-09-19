package cn.itcast.dao.cargo;

import cn.itcast.domain.cargo.Packing;
import cn.itcast.domain.cargo.PackingExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PackingDao {
    long countByExample(PackingExample example);

    int deleteByExample(PackingExample example);

    int deleteByPrimaryKey(String packingListId);

    int insert(Packing record);

    int insertSelective(Packing record);

    List<Packing> selectByExample(PackingExample example);

    Packing selectByPrimaryKey(String packingListId);

    int updateByExampleSelective(@Param("record") Packing record, @Param("example") PackingExample example);

    int updateByExample(@Param("record") Packing record, @Param("example") PackingExample example);

    int updateByPrimaryKeySelective(Packing record);

    int updateByPrimaryKey(Packing record);

    List<Packing> findPackingByState(@Param("companyId") String companyId, @Param("state") int state);
}