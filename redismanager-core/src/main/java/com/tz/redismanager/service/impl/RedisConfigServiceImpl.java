package com.tz.redismanager.service.impl;

import com.baidu.fsg.uid.UidGenerator;
import com.tz.redismanager.cacher.annotation.CacheEvict;
import com.tz.redismanager.cacher.annotation.Cacheable;
import com.tz.redismanager.cacher.annotation.L1Cache;
import com.tz.redismanager.cacher.annotation.L2Cache;
import com.tz.redismanager.cacher.util.SpringUtils;
import com.tz.redismanager.config.EncryptConfig;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.domain.dto.RedisConfigAnalysisDTO;
import com.tz.redismanager.dao.domain.dto.RedisConfigDTO;
import com.tz.redismanager.dao.domain.dto.RedisConfigExtDTO;
import com.tz.redismanager.dao.domain.po.RedisConfigExtPO;
import com.tz.redismanager.dao.domain.po.RedisConfigPO;
import com.tz.redismanager.dao.mapper.RedisConfigExtPOMapper;
import com.tz.redismanager.dao.mapper.RedisConfigPOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RedisConfigPageParam;
import com.tz.redismanager.domain.vo.RedisConfigPageVO;
import com.tz.redismanager.domain.vo.RedisConfigVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.service.IRedisConfigService;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.RSAUtils;
import com.tz.redismanager.util.RsaException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * redis连接配置服务
 *
 * @author tuanzuo
 */
@Service
public class RedisConfigServiceImpl implements IRedisConfigService {

    private static final Logger logger = TraceLoggerFactory.getLogger(RedisConfigServiceImpl.class);

    @Value("${upload.file.path.prefix}")
    private String uploadPrefix;

    @Autowired
    private UidGenerator uidGenerator;
    @Autowired
    private EncryptConfig encryptConfig;
    @Autowired
    private SpringUtils springUtils;
    @Autowired
    private IRedisContextService redisContextService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private RedisConfigPOMapper redisConfigPOMapper;
    @Autowired
    private RedisConfigExtPOMapper redisConfigExtPOMapper;

    @Override
    public ApiResult<RedisConfigPageVO> searchList(RedisConfigPageParam param) {
        //1、查询数据
        RedisConfigPageVO pageVO = new RedisConfigPageVO();
        List<RedisConfigPO> list = redisConfigPOMapper.selectPage(this.buidPageParams(param));
        if (CollectionUtils.isEmpty(list)) {
            return new ApiResult<>(ResultCode.SUCCESS, pageVO);
        }
        //2、构建返回的List
        List<RedisConfigDTO> resultList = this.buildRedisConfigDTOList(pageVO, list);
        //3、查询扩展配置
        List<RedisConfigExtPO> extList = this.queryRedisConfigExts(list);
        if (CollectionUtils.isEmpty(extList)) {
            return new ApiResult<>(ResultCode.SUCCESS, pageVO);
        }
        //4、设置扩展配置数据
        Map<String, List<RedisConfigExtPO>> map = extList.stream().collect(Collectors.groupingBy(temp -> String.valueOf(temp.getRconfigId())));
        resultList.forEach(temp -> {
            temp.setExtList(map.get(temp.getId()));
        });
        return new ApiResult<>(ResultCode.SUCCESS, pageVO);
    }

    @Cacheable(name = "redis连接配置信息PO缓存", key = ConstInterface.CacheKey.REDIS_CONFIG, var = "#id")
    @Override
    public RedisConfigPO queryPO(Long id) {
        return redisConfigPOMapper.selectByPrimaryKey(id);
    }

    @Cacheable(name = "redis连接配置信息DTO缓存", key = ConstInterface.CacheKey.REDIS_CONFIG_DTO, var = "#id")
    @Override
    public RedisConfigDTO queryDTO(Long id) {
        RedisConfigPO po = redisConfigPOMapper.selectByPrimaryKey(id);
        RedisConfigDTO result = this.convertRedisConfigPOToDTO(po);
        List<RedisConfigExtPO> extPOS = redisConfigExtPOMapper.selectList(this.buildQueryRedisConfigExtDTO(po));
        result.setExtList(extPOS);
        return result;
    }

    @Override
    public List<RedisConfigPO> queryList(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return redisConfigPOMapper.selectByIds(ids);
    }

    @Cacheable(name = "redis连接配置分析页缓存", key = ConstInterface.CacheKey.ANALYSIS_REDIS_CONFIG, l1Cache = @L1Cache(expireDuration = 60, expireUnit = TimeUnit.SECONDS), l2Cache = @L2Cache(expireDuration = 120, expireUnit = TimeUnit.SECONDS))
    @Override
    public List<RedisConfigAnalysisDTO> queryRedisConfigAnalysis() {
        return redisConfigPOMapper.selectToAnalysis(ConstInterface.IF_DEL.NO);
    }

    @Override
    public void invalidateCache(Long id) {
        springUtils.getBean(IRedisConfigService.class).invalidateDTOCache(id);
        springUtils.getBean(IRedisConfigService.class).invalidatePOCache(id);
    }

    @CacheEvict(name = "redis连接配置信息PO失效", key = ConstInterface.CacheKey.REDIS_CONFIG, var = "#id")
    @Override
    public void invalidatePOCache(Long id) {

    }

    @CacheEvict(name = "redis连接配置信息DTO失效", key = ConstInterface.CacheKey.REDIS_CONFIG_DTO, var = "#id")
    @Override
    public void invalidateDTOCache(Long id) {

    }

    @Override
    public RedisConfigPO add(RedisConfigVO vo, AuthContext authContext) {
        RedisConfigPO po = this.buildAddRedisConfigPO(vo, authContext);
        List<RedisConfigExtPO> extList = this.buildAddRedisConfigExtPOs(vo, po, authContext);
        transactionTemplate.execute((transactionStatus) -> {
            redisConfigPOMapper.insertSelective(po);
            if (CollectionUtils.isNotEmpty(extList)) {
                redisConfigExtPOMapper.insertBatch(extList);
            }
            return null;
        });
        return po;
    }

    @Override
    public ApiResult<?> delete(Long id, AuthContext authContext) {
        RedisConfigPO po = this.buildDelRedisConfigPO(id, authContext);
        RedisConfigExtDTO extDTO = this.buildDelRedisConfigExtDTO(id, authContext);
        transactionTemplate.execute((transactionStatus) -> {
            redisConfigPOMapper.updateByPrimaryKeySelective(po);
            redisConfigExtPOMapper.updateByRconfigIds(extDTO);
            return null;
        });
        //删除缓存中的RedisTemplate
        redisContextService.getRedisTemplateMap().remove(id);
        springUtils.getBean(IRedisConfigService.class).invalidateCache(id);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> update(RedisConfigVO vo, AuthContext authContext) {
        RedisConfigPO po = this.buildUpdateRedisConfigPO(vo, authContext);
        RedisConfigExtDTO delExtDTO = this.buildDelRedisConfigExtDTO(vo.getId(), authContext);
        List<RedisConfigExtPO> extList = this.buildAddRedisConfigExtPOs(vo, po, authContext);
        transactionTemplate.execute((transactionStatus) -> {
            redisConfigPOMapper.updateByPrimaryKeySelective(po);
            redisConfigExtPOMapper.updateByRconfigIds(delExtDTO);
            if (CollectionUtils.isNotEmpty(extList)) {
                redisConfigExtPOMapper.insertBatch(extList);
            }
            return null;
        });
        //删除缓存中的RedisTemplate
        redisContextService.getRedisTemplateMap().remove(vo.getId());
        springUtils.getBean(IRedisConfigService.class).invalidateCache(vo.getId());
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> upload(MultipartFile file, AuthContext authContext) {
        if (file.isEmpty()) {
            return new ApiResult<>(ResultCode.FILE_UPLOAD_ERROR.getCode(), "上传失败，文件不能为空");
        }
        String sufFile = "jar";
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(sufFile)) {
            return new ApiResult<>(ResultCode.FILE_UPLOAD_ERROR.getCode(), "上传失败，请上传" + sufFile + "文件");
        }
        String relativePath = StringUtils.join(ConstInterface.Symbol.SLASH, uidGenerator.getUID(), ConstInterface.Symbol.SLASH, fileName);
        String filePath = StringUtils.join(uploadPrefix.endsWith(ConstInterface.Symbol.SLASH) ? uploadPrefix.substring(0, uploadPrefix.length()) : uploadPrefix, relativePath);
        File targetFile = new File(filePath);
        try {
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            file.transferTo(targetFile);
            logger.info("上传成功，userName:{},filePath:{}", authContext.getUserName(), filePath);
            return new ApiResult<>(ResultCode.SUCCESS.getCode(), "上传成功", relativePath);
        } catch (IOException e) {
            logger.error("上传文件异常，filePath:{}", filePath, e);
        }
        return new ApiResult<>(ResultCode.FILE_UPLOAD_ERROR);
    }

    @Override
    public ResponseEntity<byte[]> download(String fileName, AuthContext authContext) {
        String filePath = null;
        ResponseEntity<byte[]> entity = null;
        try {
            filePath = StringUtils.join(uploadPrefix.endsWith(ConstInterface.Symbol.SLASH) ? uploadPrefix.substring(0, uploadPrefix.length()) : uploadPrefix, fileName);
            File downFile = new File(filePath);
            if (downFile.exists() == false) {
                logger.error("download file not found.filePath:{}", filePath);
                return entity;
            }
            byte[] body = null;
            InputStream is = new FileInputStream(downFile);
            body = new byte[is.available()];
            is.read(body);
            HttpHeaders headers = new HttpHeaders();
            headers.add("content-type", "application/octet-stream");
            headers.add("Content-Disposition", "attchement;filename=" + fileName);
            entity = new ResponseEntity<>(body, headers, HttpStatus.OK);
            logger.info("下载成功，userName:{},filePath:{}", authContext.getUserName(), filePath);
        } catch (Exception e) {
            logger.error("下载文件异常，filePath:{}", filePath, e);
        }
        return entity;
    }

    private List<RedisConfigDTO> buildRedisConfigDTOList(RedisConfigPageVO pageVO, List<RedisConfigPO> list) {
        Long currentMinId = 0L;
        List<RedisConfigDTO> resultList = new ArrayList<>();
        int i = 0;
        for (RedisConfigPO temp : list) {
            //得到本次查询最小的id
            if (i == 0) {
                currentMinId = temp.getId();
            }
            i++;
            if (temp.getId() < currentMinId) {
                currentMinId = temp.getId();
            }

            resultList.add(this.convertRedisConfigPOToDTO(temp));
        }
        /**设置本次查询最小的id--转换id-->Long转成String：防止Long的值超过了js的number类型最大值的问题  v1.7.0-20211109*/
        pageVO.setCurrentMinId(String.valueOf(currentMinId));
        pageVO.setConfigList(resultList);
        return resultList;
    }

    private RedisConfigDTO convertRedisConfigPOToDTO(RedisConfigPO po) {
        RedisConfigDTO dto = new RedisConfigDTO();
        BeanUtils.copyProperties(po, dto);
        /**转换id-->Long转成String：防止Long的值超过了js的number类型最大值的问题  v1.7.0-20211109*/
        dto.setId(String.valueOf(po.getId()));
        return dto;
    }

    private List<RedisConfigExtPO> queryRedisConfigExts(List<RedisConfigPO> list) {
        return redisConfigExtPOMapper.selectList(this.buildRedisConfigExtDTO(list));
    }

    private Map<String, Object> buidPageParams(RedisConfigPageParam param) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchKey", param.getSearchKey());
        params.put("isPublic", param.getIsPublic());
        params.put("yesPublic", ConstInterface.IS_PUBLIC.YES);
        params.put("noPublic", ConstInterface.IS_PUBLIC.NO);
        params.put("userName", param.getUserName());
        params.put("isSuperAdmin", param.getIsSuperAdmin());
        params.put("yesSuperAdmin", ConstInterface.IS_SUPER_ADMIN.YES);
        params.put("noSuperAdmin", ConstInterface.IS_SUPER_ADMIN.NO);
        params.put("preMinId", param.getPreMinId());
        //params.put("offset", param.getOffset());
        params.put("rows", param.getRows());
        params.put("ifDel", ConstInterface.IF_DEL.NO);
        return params;
    }

    private RedisConfigExtDTO buildRedisConfigExtDTO(List<RedisConfigPO> list) {
        Set<Long> configIds = list.stream().map(temp -> temp.getId()).collect(Collectors.toSet());
        RedisConfigExtDTO queryDTO = new RedisConfigExtDTO();
        queryDTO.setRconfigIds(configIds);
        queryDTO.setExtKey(RedisConfigExtPO.EXT_KEY_JAR_PATH);
        queryDTO.setIfDel(ConstInterface.IF_DEL.NO);
        return queryDTO;
    }

    private RedisConfigPO buildAddRedisConfigPO(RedisConfigVO vo, AuthContext authContext) {
        String userName = authContext.getUserName();
        RedisConfigPO po = new RedisConfigPO();
        BeanUtils.copyProperties(vo, po);
        this.encryptPassWord(po);
        //po.setId(UUIDUtils.generateId());
        po.setId(uidGenerator.getUID());
        po.setCreater(userName);
        po.setCreateTime(new Date());
        po.setUpdater(userName);
        po.setUpdateTime(new Date());
        po.setIfDel(ConstInterface.IF_DEL.NO);
        return po;
    }

    private List<RedisConfigExtPO> buildAddRedisConfigExtPOs(RedisConfigVO vo, RedisConfigPO po, AuthContext authContext) {
        String userName = authContext.getUserName();
        List<RedisConfigExtPO> extList = new ArrayList<>();
        vo.getExts().forEach(temp -> {
            RedisConfigExtPO extPO = new RedisConfigExtPO();
            extPO.setRconfigId(po.getId());
            extPO.setExtKey(RedisConfigExtPO.EXT_KEY_JAR_PATH);
            extPO.setExtName(temp.getExtName());
            extPO.setExtValue(temp.getExtValue());
            extPO.setCreater(userName);
            extPO.setCreateTime(new Date());
            extPO.setUpdater(userName);
            extPO.setUpdateTime(new Date());
            extPO.setIfDel(ConstInterface.IF_DEL.NO);
            extList.add(extPO);
        });
        return extList;
    }

    private RedisConfigPO buildDelRedisConfigPO(Long id, AuthContext authContext) {
        String userName = authContext.getUserName();
        RedisConfigPO po = new RedisConfigPO();
        po.setId(id);
        po.setUpdater(userName);
        po.setUpdateTime(new Date());
        po.setIfDel(ConstInterface.IF_DEL.YES);
        return po;
    }

    private RedisConfigExtDTO buildDelRedisConfigExtDTO(Long id, AuthContext authContext) {
        RedisConfigExtDTO extDTO = new RedisConfigExtDTO();
        extDTO.setRconfigId(id);
        extDTO.setIfDel(ConstInterface.IF_DEL.YES);
        extDTO.setUpdater(authContext.getUserName());
        extDTO.setUpdateTime(new Date());
        return extDTO;
    }

    private RedisConfigPO buildUpdateRedisConfigPO(RedisConfigVO vo, AuthContext authContext) {
        String userName = authContext.getUserName();
        RedisConfigPO oldPO = springUtils.getBean(IRedisConfigService.class).queryPO(vo.getId());
        RedisConfigPO po = new RedisConfigPO();
        BeanUtils.copyProperties(vo, po);
        if (!StringUtils.equals(po.getPassword(), oldPO.getPassword())) {
            this.encryptPassWord(po);
        } else {
            po.setPassword(null);
        }
        po.setUpdater(userName);
        po.setUpdateTime(new Date());
        return po;
    }

    private RedisConfigExtDTO buildQueryRedisConfigExtDTO(RedisConfigPO po) {
        RedisConfigExtDTO queryExt = new RedisConfigExtDTO();
        queryExt.setRconfigId(po.getId());
        queryExt.setExtKey(RedisConfigExtDTO.EXT_KEY_JAR_PATH);
        queryExt.setIfDel(ConstInterface.IF_DEL.NO);
        return queryExt;
    }

    /**
     * 密码加密
     *
     * @param po
     * @throws RsaException
     */
    private void encryptPassWord(RedisConfigPO po) {
        if (StringUtils.isNotBlank(po.getPassword())) {
            po.setPassword(RSAUtils.rsaPublicEncrypt(po.getPassword(), encryptConfig.getPublicKey(), RSAUtils.CHARSET_UTF8));
        }
    }

}
