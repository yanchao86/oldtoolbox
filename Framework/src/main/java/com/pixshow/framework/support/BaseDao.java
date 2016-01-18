package com.pixshow.framework.support;

import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.pixshow.framework.support.mapper.CustomTypeBeanPropertyRowMapper;
import com.pixshow.framework.support.mapper.CustomTypeMapper;
import com.pixshow.framework.support.mapper.CustomTypeProcesor;
import com.pixshow.framework.utils.BeanUtility;
import com.pixshow.framework.utils.ClassUtility;

public abstract class BaseDao extends DaoSupport {

    protected Log        logger = LogFactory.getLog(getClass());

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Autowired(required = false)
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected void checkDaoConfig() {
    }

    protected String getSqlStatement(String name) {
        return ClassUtility.getClassName(getClass()) + "." + name;
    }

    private Object[] customType(Object[] args) {
        List<CustomTypeProcesor> procesors = CustomTypeMapper.getProcesors();
        Object[] newArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            Object value = args[i];
            if (value != null) {
                for (CustomTypeProcesor procesor : procesors) {
                    if (procesor.isMatch(value.getClass())) {
                        value = procesor.getDatabaseValue(value);
                        break;
                    }
                }
            }
            newArgs[i] = value;
        }
        return newArgs;
    }

    protected Number update(final String sql, final Object... args) {
        Object[] newArgs = customType(args);
        return getJdbcTemplate().update(sql, newArgs);
    }

    protected <T> List<T> queryForList(String sql, Class<T> mappedClass, final Object... args) {
        Object[] newArgs = customType(args);
        List<T> list = getJdbcTemplate().query(sql, new CustomTypeBeanPropertyRowMapper<T>(mappedClass), newArgs);
        return list;
    }

    protected Number insert(final String sql, final Object... args) {
        final Object[] newArgs = customType(args);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                for (int i = 0; i < newArgs.length; i++) {
                    ps.setObject(i + 1, newArgs[i]);
                }
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey();
    }

    protected <T> T queryForBean(String sql, Class<T> mappedClass, Object... args) {
        List<T> list = queryForList(sql, mappedClass, args);
        return (list == null || list.size() == 0) ? null : list.get(0);
    }

    protected <T> T queryForBean(String tableName, Class<T> mappedClass, Map<String, Object> paramMap, String spec) {
        String[] sign = { "<", ">", "!=", ">=", "<=" };
        StringBuilder nameSql = new StringBuilder();
        Object[] args = new Object[paramMap.size()];
        Iterator<String> it = paramMap.keySet().iterator();
        for (int i = 0; it.hasNext(); i++) {
            String name = it.next();
            Object value = paramMap.get(name);
            if (nameSql.length() > 0) {
                nameSql.append(" and ");
            }
            if (value == null) {
                nameSql.append(name);
            } else {
                boolean s = false;
                for (String str : sign) {
                    if (name.endsWith(str)) {
                        s = true;
                        break;
                    }
                }
                if (s) {
                    nameSql.append(name + "?");
                } else {
                    nameSql.append(name + "=?");
                }
                args[i] = value;
            }
        }
        spec = spec == null ? "" : spec;
        List<T> list = queryForList("select * from " + tableName + " where " + nameSql.toString() + " " + spec, mappedClass, args);
        return (list == null || list.size() == 0) ? null : list.get(0);
    }

    protected <T> List<T> queryForList(String tableName, Class<T> mappedClass, Map<String, Object> paramMap, String spec) {
        String[] sign = { "<", ">", "!=", ">=", "<=" };

        StringBuilder nameSql = new StringBuilder();
        List<Object> args = new ArrayList<Object>();
        Iterator<String> it = paramMap.keySet().iterator();
        for (int i = 0; it.hasNext(); i++) {
            String name = it.next();
            Object value = paramMap.get(name);
            if (nameSql.length() > 0) {
                nameSql.append(" and ");
            }

            StringBuilder inSql = new StringBuilder();
            if (value instanceof List) {
                List<Object> valueList = (List<Object>) value;
                nameSql.append(name + " in ");
                inSql.append("(");
                for (int j = 0; j < valueList.size(); j++) {
                    if (inSql.length() > 1) {
                        inSql.append(",");
                    }
                    inSql.append("?");
                    args.add(valueList.get(j));
                }
                inSql.append(")");
            } else if (value == null) {
                nameSql.append(name);
            } else {
                boolean s = false;
                for (String str : sign) {
                    if (name.endsWith(str)) {
                        s = true;
                        break;
                    }
                }
                if (s) {
                    nameSql.append(name + "?");
                } else {
                    nameSql.append(name + "=?");
                }
                args.add(value);
            }
            nameSql.append(inSql.toString());
        }
        spec = spec == null ? "" : spec;
        return queryForList("select * from " + tableName + " where " + nameSql.toString() + " " + spec, mappedClass, args.toArray());
    }

    protected Number insertBean(final String tableName, final Object bean, final String... excludeNames) {
        StringBuilder nameSql = new StringBuilder();
        StringBuilder valueSql = new StringBuilder();

        List<PropertyDescriptor> propertys = BeanUtility.getPropertyDescriptors(bean);
        List<Object> args = new ArrayList<Object>();
        for (int i = 0; i < propertys.size(); i++) {
            PropertyDescriptor property = propertys.get(i);
            String name = property.getName();
            if ("class".equals(name) || ArrayUtils.contains(excludeNames, name)) {
                continue;
            }
            args.add(BeanUtility.getSimpleProperty(bean, name));
            if (valueSql.length() > 0) {
                valueSql.append(",");
            }
            valueSql.append("?");
            if (nameSql.length() > 0) {
                nameSql.append(",");
            }
            nameSql.append(name);
        }
        return insert("insert into " + tableName + "(" + nameSql.toString() + ") values (" + valueSql.toString() + ")", args.toArray());
    }

    protected Number replaceBean(final String tableName, final Object bean, final String... excludeNames) {
        StringBuilder nameSql = new StringBuilder();
        StringBuilder valueSql = new StringBuilder();

        List<PropertyDescriptor> propertys = BeanUtility.getPropertyDescriptors(bean);
        List<Object> args = new ArrayList<Object>();
        for (int i = 0; i < propertys.size(); i++) {
            PropertyDescriptor property = propertys.get(i);
            String name = property.getName();
            if ("class".equals(name) || ArrayUtils.contains(excludeNames, name)) {
                continue;
            }
            args.add(BeanUtility.getSimpleProperty(bean, name));
            if (valueSql.length() > 0) {
                valueSql.append(",");
            }
            valueSql.append("?");
            if (nameSql.length() > 0) {
                nameSql.append(",");
            }
            nameSql.append(name);
        }
        return insert("replace into " + tableName + "(" + nameSql.toString() + ") values (" + valueSql.toString() + ")", args.toArray());
    }

    protected <T> Number update(String tableName, Map<String, Object> paramMap) {
        String[] sign = { "<", ">", "!=", ">=", "<=" };

        StringBuilder nameSql = new StringBuilder();
        List<Object> args = new ArrayList<Object>();
        Iterator<String> it = paramMap.keySet().iterator();
        for (int i = 0; it.hasNext(); i++) {
            String name = it.next();
            Object value = paramMap.get(name);
            if (nameSql.length() > 0) {
                nameSql.append(" and ");
            }

            StringBuilder inSql = new StringBuilder();
            if (value instanceof List) {
                List<Object> valueList = (List<Object>) value;
                nameSql.append(name + " in ");
                inSql.append("(");
                for (int j = 0; j < valueList.size(); j++) {
                    if (inSql.length() > 1) {
                        inSql.append(",");
                    }
                    inSql.append("?");
                    args.add(valueList.get(j));
                }
                inSql.append(")");
            } else {
                boolean s = false;
                for (String str : sign) {
                    if (name.endsWith(str)) {
                        s = true;
                        break;
                    }
                }
                if (s) {
                    nameSql.append(name + "?");
                } else {
                    nameSql.append(name + "=?");
                }
                args.add(value);
            }
            nameSql.append(inSql.toString());
        }
        return update("update " + tableName + " where " + nameSql, args);
    }

    protected Number updateBean(final String tableName, final Object bean, final String where, final String... excludeNames) {
        StringBuilder sql = new StringBuilder();

        List<PropertyDescriptor> propertys = BeanUtility.getPropertyDescriptors(bean);
        List<Object> args = new ArrayList<Object>();
        for (int i = 0; i < propertys.size(); i++) {
            PropertyDescriptor property = propertys.get(i);
            String name = property.getName();
            if ("class".equals(name) || ArrayUtils.contains(excludeNames, name)) {
                continue;
            }
            args.add(BeanUtility.getSimpleProperty(bean, name));
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(name).append("=?");
        }
        return update("update " + tableName + " set " + sql.toString() + " where " + where, args.toArray());
    }

}
