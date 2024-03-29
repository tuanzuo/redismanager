package com.tz.redismanager.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.domain.dto.PostmanConfigDTO;
import com.tz.redismanager.dao.domain.po.PostmanConfigPO;
import com.tz.redismanager.dao.domain.po.UserPO;
import com.tz.redismanager.dao.mapper.PostmanConfigPOMapper;
import com.tz.redismanager.dao.mapper.UserPOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.PostmanConfigResp;
import com.tz.redismanager.domain.vo.PostmanConfigVO;
import com.tz.redismanager.domain.vo.RequestConfigVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.service.IPostmanConfigService;
import com.tz.redismanager.util.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>Postman配置服务实现</p>
 *
 * @author tuanzuo
 * @version 1.7.1
 * @time 2021-11-13 21:10
 **/
@Service
public class PostmanConfigServiceImpl implements IPostmanConfigService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private PostmanConfigPOMapper postmanConfigPOMapper;
    @Autowired
    private UserPOMapper userPOMapper;

    @Override
    public ApiResult<?> add(PostmanConfigVO vo, AuthContext authContext) {
        List<PostmanConfigPO> list = postmanConfigPOMapper.selectByParams(this.buildQueryCategoryDTO(vo, authContext));
        PostmanConfigPO parent = null;
        if (CollectionUtils.isNotEmpty(list)) {
            parent = list.get(0);
        }
        PostmanConfigPO finalParent = parent;
        transactionTemplate.execute((transactionStatus) -> {
            if (null == finalParent) {
                PostmanConfigVO pvo = new PostmanConfigVO();
                pvo.setConfigName(vo.getCategoryName());
                pvo.setCategory(ConstInterface.CATEGORY.INTERFACE_CATEGORY);
                pvo.setIfDel(ConstInterface.IF_DEL.NO);
                PostmanConfigPO parentPO = this.buildAddPO(pvo, authContext);
                postmanConfigPOMapper.insertSelective(parentPO);
                vo.setPid(parentPO.getId());
            } else {
                vo.setPid(finalParent.getId());
            }
            vo.setCategory(ConstInterface.CATEGORY.INTERFACE_TYPE);
            postmanConfigPOMapper.insertSelective(this.buildAddPO(vo, authContext));
            return null;
        });
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> update(PostmanConfigVO vo, AuthContext authContext) {
        PostmanConfigPO addParentPO = null;
        if (ConstInterface.CATEGORY.INTERFACE_TYPE.equals(vo.getCategory())) {
            List<PostmanConfigPO> list = postmanConfigPOMapper.selectByParams(this.buildQueryCategoryDTO(vo, authContext));
            if (CollectionUtils.isEmpty(list)) {
                PostmanConfigVO pvo = new PostmanConfigVO();
                pvo.setConfigName(vo.getCategoryName());
                pvo.setCategory(ConstInterface.CATEGORY.INTERFACE_CATEGORY);
                pvo.setIfDel(ConstInterface.IF_DEL.NO);
                addParentPO = this.buildAddPO(pvo, authContext);
            } else {
                vo.setPid(list.get(0).getId());
            }
        }
        PostmanConfigPO finalAddParentPO = addParentPO;
        transactionTemplate.execute((transactionStatus) -> {
            if (null != finalAddParentPO) {
                postmanConfigPOMapper.insertSelective(finalAddParentPO);
                vo.setPid(finalAddParentPO.getId());
            }
            postmanConfigPOMapper.updateByPrimaryKeySelective(this.buildUpdatePO(vo, authContext));
            return null;
        });
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> del(PostmanConfigVO vo, AuthContext authContext) {
        if (ConstInterface.CATEGORY.INTERFACE_CATEGORY.equals(vo.getCategory())) {
            PostmanConfigDTO dto = new PostmanConfigDTO();
            dto.setPid(vo.getId());
            dto.setIfDel(ConstInterface.IF_DEL.NO);
            List<PostmanConfigPO> list = postmanConfigPOMapper.selectByParams(dto);
            if (CollectionUtils.isNotEmpty(list)) {
                return new ApiResult<>(ResultCode.FAIL.getCode(), vo.getConfigName() + "-分类下还存在接口，所以不能删除！");
            }
        }
        PostmanConfigPO po = new PostmanConfigPO();
        po.setId(vo.getId());
        po.setUpdater(authContext.getUserName());
        po.setUpdateTime(new Date());
        po.setIfDel(vo.getIfDel());
        postmanConfigPOMapper.updateByPrimaryKeySelective(po);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> share(PostmanConfigVO vo, AuthContext authContext) {
        String[] shareUserNames = StringUtils.split(vo.getShareUserNames(), ",");
        if (ArrayUtils.isEmpty(shareUserNames)) {
            return new ApiResult<>(ResultCode.FAIL.getCode(), "无分享的用户名");
        }
        vo.setShareUserNameList(new HashSet<>(Arrays.asList(shareUserNames)));

        List<UserPO> users = userPOMapper.selectByNames(vo.getShareUserNameList());
        if (CollectionUtils.isEmpty(users)) {
            return new ApiResult<>(ResultCode.FAIL.getCode(), "分享的用户名不存在");
        }
        vo.setShareUserNameList(users.stream().map(temp -> temp.getName()).collect(Collectors.toSet()));

        if (ConstInterface.CATEGORY.INTERFACE_CATEGORY.equals(vo.getCategory())) {
            PostmanConfigDTO dto = new PostmanConfigDTO();
            dto.setId(vo.getId());
            dto.setCreater(authContext.getUserName());
            dto.setIfDel(ConstInterface.IF_DEL.NO);
            List<PostmanConfigPO> parentList = postmanConfigPOMapper.selectByParams(dto);

            PostmanConfigDTO subDto = new PostmanConfigDTO();
            subDto.setPid(vo.getId());
            subDto.setCreater(authContext.getUserName());
            subDto.setIfDel(ConstInterface.IF_DEL.NO);
            List<PostmanConfigPO> subList = postmanConfigPOMapper.selectByParams(subDto);
            Map<Long, List<PostmanConfigPO>> subMap = subList.stream().filter(temp -> null != temp.getPid()).collect(Collectors.groupingBy(PostmanConfigPO::getPid));

            this.saveShareConfig(vo, parentList, subMap);
        } else if (ConstInterface.CATEGORY.INTERFACE_TYPE.equals(vo.getCategory())) {
            PostmanConfigDTO dto = new PostmanConfigDTO();
            dto.setId(vo.getPid());
            dto.setCreater(authContext.getUserName());
            dto.setIfDel(ConstInterface.IF_DEL.NO);
            List<PostmanConfigPO> parentList = postmanConfigPOMapper.selectByParams(dto);

            PostmanConfigDTO subDto = new PostmanConfigDTO();
            subDto.setId(vo.getId());
            subDto.setCreater(authContext.getUserName());
            subDto.setIfDel(ConstInterface.IF_DEL.NO);
            List<PostmanConfigPO> subList = postmanConfigPOMapper.selectByParams(subDto);
            Map<Long, List<PostmanConfigPO>> subMap = subList.stream().filter(temp -> null != temp.getPid()).collect(Collectors.groupingBy(PostmanConfigPO::getPid));

            this.saveShareConfig(vo, parentList, subMap);
        }
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<List<PostmanConfigResp>> queryList(PostmanConfigVO vo, AuthContext authContext) {
        List<PostmanConfigResp> respList = new ArrayList<>();
        List<PostmanConfigPO> list = postmanConfigPOMapper.selectByParams(this.buildQueryDTO(vo, authContext));
        Map<Long, List<PostmanConfigResp>> subListMap = list.stream().
            filter(temp -> ConstInterface.CATEGORY.INTERFACE_TYPE.equals(temp.getCategory()) && null != temp.getPid()).
            map(temp -> {
                PostmanConfigResp sub = new PostmanConfigResp();
                BeanUtils.copyProperties(temp, sub);
                return sub;
            }).collect(Collectors.groupingBy(PostmanConfigResp::getPid));

        for (PostmanConfigPO temp : list) {
            if (ConstInterface.CATEGORY.INTERFACE_CATEGORY.equals(temp.getCategory())) {
                PostmanConfigResp resp = new PostmanConfigResp();
                BeanUtils.copyProperties(temp, resp);
                resp.setSubList(Optional.ofNullable(subListMap.get(resp.getId())).orElse(new ArrayList<>()).stream().sorted(Comparator.comparing(PostmanConfigResp::getSort)).collect(Collectors.toList()));
                respList.add(resp);
            }
        }
        return new ApiResult<>(ResultCode.SUCCESS, respList.stream().sorted(Comparator.comparing(PostmanConfigResp::getSort)).collect(Collectors.toList()));
    }

    @Override
    public Object request(RequestConfigVO vo, AuthContext authContext) {
        Map<String, String> headersMap = JsonUtils.parseObject(vo.getHeaders(), new TypeReference<Map<String, Object>>() {
        }.getType());
        HttpHeaders headers = new HttpHeaders();
        if (MapUtils.isNotEmpty(headersMap)) {
            headers.setAll(headersMap);
        }
        Map<String, String> cookiesMap = JsonUtils.parseObject(vo.getCookies(), new TypeReference<Map<String, Object>>() {
        }.getType());
        if (MapUtils.isNotEmpty(cookiesMap)) {
            List<String> cookieList = new ArrayList<>();
            cookiesMap.forEach((key, value) -> cookieList.add(StringUtils.join(key, ConstInterface.Symbol.EQUAL, value)));
            headers.put(HttpHeaders.COOKIE, cookieList);
        }
        Map<String, Object> body = JsonUtils.parseObject(vo.getBody(), new TypeReference<Map<String, Object>>() {
        }.getType());
        Map<String, String> paramsMap = JsonUtils.parseObject(vo.getParams(), new TypeReference<Map<String, Object>>() {
        }.getType());
        ResponseEntity<Object> responseEntity = restTemplate.exchange(vo.getRequestUrl(), HttpMethod.resolve(StringUtils.upperCase(vo.getRequestType())), new HttpEntity<>(body, headers), Object.class, paramsMap);
        return responseEntity;
    }

    private void saveShareConfig(PostmanConfigVO vo, List<PostmanConfigPO> parentList, Map<Long, List<PostmanConfigPO>> subMap) {
        transactionTemplate.execute((transactionStatus) -> {
            for (PostmanConfigPO parentAddPO : parentList) {
                Long oldId = parentAddPO.getId();
                String configName = parentAddPO.getConfigName() + "[share]";
                for (String userName : vo.getShareUserNameList()) {
                    parentAddPO.setConfigName(configName);
                    parentAddPO.setCreater(userName);
                    parentAddPO.setCreateTime(new Date());
                    parentAddPO.setUpdater(userName);
                    parentAddPO.setUpdateTime(new Date());
                    postmanConfigPOMapper.insertSelective(parentAddPO);
                    List<PostmanConfigPO> subListTemp = Optional.ofNullable(subMap.get(oldId)).orElse(new ArrayList<>());
                    for (PostmanConfigPO subAddPO : subListTemp) {
                        subAddPO.setPid(parentAddPO.getId());
                        subAddPO.setCreater(userName);
                        subAddPO.setCreateTime(new Date());
                        subAddPO.setUpdater(userName);
                        subAddPO.setUpdateTime(new Date());
                        postmanConfigPOMapper.insertSelective(subAddPO);
                    }
                }
            }
            return null;
        });
    }

    private PostmanConfigPO buildAddPO(PostmanConfigVO vo, AuthContext authContext) {
        PostmanConfigPO po = new PostmanConfigPO();
        BeanUtils.copyProperties(vo, po);
        po.setCreater(authContext.getUserName());
        po.setUpdater(authContext.getUserName());
        po.setCreateTime(new Date());
        po.setUpdateTime(new Date());
        return po;
    }

    private PostmanConfigPO buildUpdatePO(PostmanConfigVO vo, AuthContext authContext) {
        PostmanConfigPO po = new PostmanConfigPO();
        BeanUtils.copyProperties(vo, po);
        po.setUpdater(authContext.getUserName());
        po.setUpdateTime(new Date());
        return po;
    }

    private PostmanConfigDTO buildQueryDTO(PostmanConfigVO vo, AuthContext authContext) {
        PostmanConfigDTO dto = new PostmanConfigDTO();
        dto.setCategory(vo.getCategory());
        dto.setPid(vo.getPid());
        dto.setIfDel(vo.getIfDel());
        dto.setRequestUrl(vo.getRequestUrl());
        dto.setConfigName(vo.getConfigName());
        dto.setShareFlag(vo.getShareFlag());
        dto.setCreater(authContext.getUserName());
        dto.setIfDel(ConstInterface.IF_DEL.NO);
        return dto;
    }

    private PostmanConfigDTO buildQueryCategoryDTO(PostmanConfigVO vo, AuthContext authContext) {
        PostmanConfigDTO dto = new PostmanConfigDTO();
        dto.setConfigName(vo.getCategoryName());
        dto.setCategory(ConstInterface.CATEGORY.INTERFACE_CATEGORY);
        dto.setIfDel(ConstInterface.IF_DEL.NO);
        dto.setCreater(authContext.getUserName());
        return dto;
    }
}
