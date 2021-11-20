package com.tz.redismanager.dao.mapper;

import com.tz.redismanager.dao.domain.dto.PostmanConfigDTO;
import com.tz.redismanager.dao.domain.po.PostmanConfigPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>postman配置Mapper</p>
 *
 * @author tuanzuo
 * @version 1.7.1
 * @time 2021-11-13 20:59
 **/
@Mapper
public interface PostmanConfigPOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PostmanConfigPO record);

    int insertSelective(PostmanConfigPO record);

    PostmanConfigPO selectByPrimaryKey(Long id);

    List<PostmanConfigPO> selectByParams(PostmanConfigDTO dto);

    int updateByPrimaryKeySelective(PostmanConfigPO record);

    int updateByPrimaryKey(PostmanConfigPO record);
}
