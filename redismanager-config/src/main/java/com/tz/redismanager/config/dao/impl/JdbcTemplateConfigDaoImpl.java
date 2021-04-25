package com.tz.redismanager.config.dao.impl;

import com.tz.redismanager.config.config.ConfigProperties;
import com.tz.redismanager.config.constant.ConstInterface;
import com.tz.redismanager.config.dao.IConfigDao;
import com.tz.redismanager.config.domain.dto.ConfigDTO;
import com.tz.redismanager.config.domain.param.ConfigPageParam;
import com.tz.redismanager.config.domain.param.ConfigQueryParam;
import com.tz.redismanager.config.domain.po.ConfigPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-09 23:17
 **/
public class JdbcTemplateConfigDaoImpl implements IConfigDao {

    private static final String TABLE_NAME = "${tableName}";

    private String deleteByPrimaryKey_sql =
            "delete from ${tableName} where id = :id";
    private String deleteLogicByPrimaryKey_sql =
            "update ${tableName} set updater = :updater,update_time = :updateTime,if_del = :ifDel where id = :id";
    private String deleteLogicByIds_sql =
            "update ${tableName} set updater = :updater,update_time = :updateTime,if_del = :ifDel where id in (:ids)";
    private String insert_sql =
            "insert into ${tableName} (service_name, config_type, config_key, key_name, content, version, note, creater, create_time, updater, update_time, if_del) " +
            "values (:serviceName, :configType, :configKey, :keyName, :content, :version, :note, :creater, :createTime, :updater, :updateTime, :ifDel)";
    private String selectByParam_pre_sql =
            "select id, service_name, config_type, config_key, key_name, content, version, note, creater, create_time, updater, update_time, if_del from ${tableName} ";
    private String selectByPrimaryKey_sql = selectByParam_pre_sql + " where id = :id";
    private String count_sql =
            "select count(id) from ${tableName} ";
    private String updateByPrimaryKey_sql =
            "update ${tableName} " +
            "set service_name = :serviceName, " +
            "config_type = :configType, " +
            "config_key = :configKey, " +
            "key_name = :keyName, " +
            "content = :content, " +
            "version = version + 1, " +
            "note = :note, " +
            "updater = :updater, " +
            "update_time = :updateTime, " +
            "if_del = :ifDel " +
            "where id = :id";

    /**
     * {@link org.springframework.boot.autoconfigure.jdbc.NamedParameterJdbcTemplateConfiguration#namedParameterJdbcTemplate}
     */
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcTemplateConfigDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, ConfigProperties configProperties) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        deleteByPrimaryKey_sql = deleteByPrimaryKey_sql.replace(TABLE_NAME, configProperties.getConfigTableName());
        deleteLogicByPrimaryKey_sql = deleteLogicByPrimaryKey_sql.replace(TABLE_NAME, configProperties.getConfigTableName());
        deleteLogicByIds_sql = deleteLogicByIds_sql.replace(TABLE_NAME, configProperties.getConfigTableName());
        insert_sql = insert_sql.replace(TABLE_NAME, configProperties.getConfigTableName());
        selectByParam_pre_sql = selectByParam_pre_sql.replace(TABLE_NAME, configProperties.getConfigTableName());
        count_sql = count_sql.replace(TABLE_NAME, configProperties.getConfigTableName());
        updateByPrimaryKey_sql = updateByPrimaryKey_sql.replace(TABLE_NAME, configProperties.getConfigTableName());
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        ConfigPO po = new ConfigPO();
        po.setId(id);
        return namedParameterJdbcTemplate.update(deleteByPrimaryKey_sql, new BeanPropertySqlParameterSource(po));
    }

    @Override
    public int deleteLogicByPrimaryKey(ConfigDTO dto) {
        return namedParameterJdbcTemplate.update(deleteLogicByPrimaryKey_sql, new BeanPropertySqlParameterSource(dto));
    }

    @Override
    public int deleteLogicByIds(ConfigDTO dto) {
        return namedParameterJdbcTemplate.update(deleteLogicByIds_sql, new BeanPropertySqlParameterSource(dto));
    }

    @Override
    public int insert(ConfigPO record) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int retVal = namedParameterJdbcTemplate.update(insert_sql, new BeanPropertySqlParameterSource(record), keyHolder);
        record.setId(keyHolder.getKey().intValue());
        return retVal;
    }

    @Override
    public ConfigPO selectByPrimaryKey(Integer id) {
        ConfigPO po = new ConfigPO();
        po.setId(id);
        return namedParameterJdbcTemplate.queryForObject(selectByPrimaryKey_sql, new BeanPropertySqlParameterSource(po), new ConfigPORowMapper());
    }

    @Override
    public List<ConfigPO> selectListByParam(ConfigQueryParam param) {
        StringBuilder suf_sql = this.getQuerySql(param);
        return namedParameterJdbcTemplate.query(selectByParam_pre_sql + suf_sql.toString(), new BeanPropertySqlParameterSource(param), new ConfigPORowMapper());
    }

    @Override
    public List<ConfigPO> selectPageByParam(ConfigPageParam param) {
        if (param.getCurrentPage() < 1) {
            param.setCurrentPage(1);
        }
        if (param.getPageSize() < 0) {
            param.setPageSize(ConfigPageParam.DEFAULT_PAGE_SIZE);
        }
        param.setOffset((param.getCurrentPage() - 1) * param.getPageSize());
        param.setRows(param.getPageSize());
        StringBuilder suf_sql = this.getLikeSql(param);
        suf_sql.append("ORDER BY create_time DESC ");
        suf_sql.append("LIMIT :offset,:rows ");
        return namedParameterJdbcTemplate.query(selectByParam_pre_sql + suf_sql.toString(), new BeanPropertySqlParameterSource(param), new ConfigPORowMapper());
    }

    @Override
    public int count(ConfigQueryParam param) {
        StringBuilder suf_sql = this.getQuerySql(param);
        return namedParameterJdbcTemplate.queryForObject(count_sql + suf_sql.toString(), new BeanPropertySqlParameterSource(param), Integer.class);
    }

    @Override
    public int countPage(ConfigPageParam param) {
        StringBuilder suf_sql = this.getLikeSql(param);
        return namedParameterJdbcTemplate.queryForObject(count_sql + suf_sql.toString(), new BeanPropertySqlParameterSource(param), Integer.class);
    }

    @Override
    public int updateByPrimaryKey(ConfigPO record) {
        return namedParameterJdbcTemplate.update(updateByPrimaryKey_sql, new BeanPropertySqlParameterSource(record));
    }

    private StringBuilder getBaseWhereSql(ConfigQueryParam param) {
        if (null == param.getIfDel()) {
            param.setIfDel(ConstInterface.IF_DEL.NO);
        }
        StringBuilder suf_sql = new StringBuilder("where 1=1 ");
        if (null != param.getConfigType()) {
            suf_sql.append(" and config_type = :configType ");
        }
        suf_sql.append(" and if_del = :ifDel ");
        return suf_sql;
    }

    private StringBuilder getLikeSql(ConfigPageParam param) {
        StringBuilder suf_sql = this.getBaseWhereSql(param);
        if (StringUtils.isNotBlank(param.getServiceName())) {
            suf_sql.append(" and LOCATE(:serviceName,service_name) > 0 ");
        }
        if (StringUtils.isNotBlank(param.getConfigKey())) {
            suf_sql.append(" and LOCATE(:configKey,config_key) > 0 ");
        }
        if (StringUtils.isNotBlank(param.getKeyName())) {
            suf_sql.append(" and LOCATE(:keyName,key_name) > 0 ");
        }
        return suf_sql;
    }

    private StringBuilder getQuerySql(ConfigQueryParam param) {
        StringBuilder suf_sql = this.getBaseWhereSql(param);
        if (null != param.getId()) {
            suf_sql.append(" and id = :id ");
        }
        if (StringUtils.isNotBlank(param.getServiceName())) {
            suf_sql.append(" and service_name = :serviceName ");
        }
        if (StringUtils.isNotBlank(param.getConfigKey())) {
            suf_sql.append(" and config_key = :configKey ");
        }
        if (StringUtils.isNotBlank(param.getKeyName())) {
            suf_sql.append(" and key_name = :keyName ");
        }
        return suf_sql;
    }

    public static class ConfigPORowMapper implements RowMapper<ConfigPO> {

        @Override
        public ConfigPO mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConfigPO po = new ConfigPO();
            po.setId(rs.getInt("id"));
            po.setServiceName(rs.getString("service_name"));
            po.setConfigType(rs.getInt("config_type"));
            po.setConfigKey(rs.getString("config_key"));
            po.setKeyName(rs.getString("key_name"));
            po.setContent(rs.getString("content"));
            po.setVersion(rs.getInt("version"));
            po.setNote(rs.getString("note"));
            po.setCreater(rs.getString("creater"));
            po.setCreateTime(rs.getDate("create_time"));
            po.setUpdater(rs.getString("updater"));
            po.setUpdateTime(rs.getDate("update_time"));
            po.setIfDel(rs.getInt("if_del"));
            return po;
        }
    }
}
