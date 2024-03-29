package com.tz.redismanager.dao.mapper;

import com.tz.redismanager.dao.domain.dto.RoleAnalysisDTO;
import com.tz.redismanager.dao.domain.po.RolePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper
 *
 * @author tuanzuo
 * @version 1.3.0
 * @time 2020-08-29 13:43
 **/
@Mapper
public interface RolePOMapper {

    int deleteByPrimaryKey(Long id);

    int insert(RolePO record);

    int insertSelective(RolePO record);

    RolePO selectByPrimaryKey(Long id);

    List<RolePO> getAll(@Param("status") Integer status, @Param("ifDel") Integer ifDel);

    List<RolePO> selectPage(@Param("name") String name, @Param("code") String code, @Param("status") Integer status,
                            @Param("offset") Integer offset, @Param("rows") Integer rows, @Param("ifDel") Integer ifDel);

    List<RoleAnalysisDTO> selectToAnalysis(@Param("ifDel") Integer ifDel);

    int countRole(RolePO record);

    int updateByPrimaryKeySelective(RolePO record);

    int updateByPrimaryKey(RolePO record);

    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status, @Param("updater") String updater);

}
