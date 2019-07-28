package com.tz.redismanager.controller;

import com.tz.redismanager.bean.vo.RedisKeyDelVo;
import com.tz.redismanager.bean.vo.RedisKeyUpdateVo;
import com.tz.redismanager.bean.vo.RedisValueQueryVo;
import com.tz.redismanager.service.IRedisAdminService;
import com.tz.redismanager.service.IRedisContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/redis/admin")
public class RedisAdminController {

    @Autowired
    private IRedisAdminService redisAdminService;
    @Autowired
    private IRedisContextService redisContextService;

    @RequestMapping("context/init/{id}")
    public void initContext(@PathVariable("id") String id) {
        redisContextService.initContext(id);
    }

    @RequestMapping("context/cache/clear/{id}")
    public void clearCacheRedisTemplate(@PathVariable("id") String id) {
        redisContextService.removeRedisTemplate(id);
        redisContextService.getRedisConfigCache().invalidate(id);
    }

    @RequestMapping("key/list")
    public Object keyList(String id, String searchKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyList", redisAdminService.searchKey(id, searchKey));
        return map;
    }

    @RequestMapping("key/value")
    public Object keyValue(@RequestBody RedisValueQueryVo vo) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyValue", redisAdminService.searchKeyValue(vo));
        return map;
    }

    @RequestMapping("key/del")
    public void delKeys(@RequestBody RedisKeyDelVo vo) {
        redisAdminService.delKeys(vo);
    }

    @RequestMapping("key/rename")
    public void renameKey(@RequestBody RedisKeyUpdateVo vo) {
        redisAdminService.renameKey(vo);
    }

    @RequestMapping("key/setTtl")
    public void setTtl(@RequestBody RedisKeyUpdateVo vo) {
        redisAdminService.setTtl(vo);
    }

    @RequestMapping("key/updateValue")
    public void updateValue(@RequestBody RedisKeyUpdateVo vo) {
        redisAdminService.updateValue(vo);
    }

}
