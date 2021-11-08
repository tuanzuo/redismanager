package com.tz.redismanager.service;

import com.tz.redismanager.dao.domain.dto.RedisConfigAnalysisDTO;
import com.tz.redismanager.dao.domain.dto.RedisConfigDTO;
import com.tz.redismanager.dao.domain.po.RedisConfigPO;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RedisConfigPageParam;
import com.tz.redismanager.domain.vo.RedisConfigPageVO;
import com.tz.redismanager.domain.vo.RedisConfigVO;
import com.tz.redismanager.security.domain.AuthContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * redis连接配置接口
 *
 * @author tuanzuo
 */
public interface IRedisConfigService {

    /**
     * 查询redis连接配置
     *
     * @return
     */
    ApiResult<RedisConfigPageVO> searchList(RedisConfigPageParam param);

    /**
     * 查询redis连接配置
     */
    RedisConfigPO queryPO(Long id);

    /**
     * 查询redis连接配置
     */
    RedisConfigDTO queryDTO(Long id);

    /**
     * 批量查询redis连接配置
     */
    List<RedisConfigPO> queryList(Set<Long> ids);

    /**
     * 查询redis连接配置分析页数据
     *
     * @return
     */
    List<RedisConfigAnalysisDTO> queryRedisConfigAnalysis();

    /**
     * 失效redis连接配置缓存
     */
    void invalidateCache(Long id);

    /**
     * 失效redis连接配置PO缓存
     */
    void invalidatePOCache(Long id);

    /**
     * 失效redis连接配置DTO缓存
     */
    void invalidateDTOCache(Long id);

    /**
     * 添加redis连接配置
     *
     * @param vo
     */
    RedisConfigPO add(RedisConfigVO vo, AuthContext authContext);

    /**
     * 删除redis连接配置
     *
     * @param id
     */
    ApiResult<?> delete(Long id, AuthContext authContext);

    /**
     * 修改redis连接配置
     *
     * @param vo
     */
    ApiResult<?> update(RedisConfigVO vo, AuthContext authContext);

    /**
     * 上传文件
     *
     * @param file
     * @param authContext
     * @return
     */
    ApiResult<?> upload(MultipartFile file, AuthContext authContext);

    /**
     * 下载文件
     *
     * @param fileName    文件名称
     * @param authContext
     * @return
     */
    ResponseEntity<byte[]> download(String fileName, AuthContext authContext);
}
