package com.oven.demo.core.base.dao;

import com.oven.demo.common.util.VoPropertyRowMapper;
import com.oven.demo.core.base.entity.SqlAndParams;
import com.oven.demo.core.base.utils.AutoGeneratePreparedStatementCreator;
import com.oven.demo.core.base.utils.CamelNameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BaseDao<T> {

    public List<T> getByPage(StringBuilder sb, List<Object> params, Class<T> clazz, Integer pageNum, Integer pageSize, JdbcTemplate jdbcTemplate) {
        String sql = sb.append(" limit ?,?").toString().replaceFirst("and", "where");
        params.add((pageNum - 1) * pageSize);
        params.add(pageSize);
        return jdbcTemplate.query(sql, params.toArray(), new VoPropertyRowMapper<>(clazz));
    }

    public List<T> getByPage(StringBuilder sb, List<Object> params, Class<T> clazz, Integer pageNum, Integer pageSize, JdbcTemplate jdbcTemplate, String orderby) {
        sb.append(" order by ").append(orderby);
        String sql = sb.append(" limit ?,?").toString().replaceFirst("and", "where");
        params.add((pageNum - 1) * pageSize);
        params.add(pageSize);
        return jdbcTemplate.query(sql, params.toArray(), new VoPropertyRowMapper<>(clazz));
    }

    public Integer getTotalNum(StringBuilder sb, List<Object> params, JdbcTemplate jdbcTemplate) {
        String sql = sb.toString().replaceFirst("and", "where");
        return jdbcTemplate.queryForObject(sql, params.toArray(), Integer.class);
    }

    public int add(JdbcTemplate jdbcTemplate, T obj) throws Exception {
        SqlAndParams sqlAndParams = getInsertFromObject(obj);
        AutoGeneratePreparedStatementCreator creator = getAutoGeneratePreparedStatementCreator(sqlAndParams.getSql(), sqlAndParams.getParams(), obj);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(creator, keyHolder);
        Number key = keyHolder.getKey();
        return key == null ? -1 : key.intValue();
    }

    public int update(JdbcTemplate jdbcTemplate, T obj) throws Exception {
        SqlAndParams sqlAndParams = getUpdateSql(obj);
        return jdbcTemplate.update(sqlAndParams.getSql(), sqlAndParams.getParams());
    }

    /**
     * 获取插入语句
     */
    public SqlAndParams getInsertFromObject(T obj) throws Exception {
        // 用来存放insert语句
        StringBuilder insertSql = new StringBuilder();
        // 用来存放?号的语句
        StringBuilder paramsSql = new StringBuilder();
        // 用来存放参数值
        List<Object> params = new ArrayList<>();
        // 分析表名
        String tableName = getTableName(obj.getClass());

        insertSql.append("insert into ").append(tableName).append(" (");

        // 计数器
        int count = 0;

        // 分析列
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation == null) { // 没有column注解的字段跳过
                continue;
            }
            // 获取具体参数值
            Method getter = getGetter(obj.getClass(), field);

            if (getter == null) {
                continue;
            }

            Object value = getter.invoke(obj);
            if (value == null) {
                continue;
            }

            Transient tranAnno = field.getAnnotation(Transient.class);
            if (tranAnno != null) {
                // 如果有 Transient 标记直接跳过
                continue;
            }

            // 获取字段名
            String columnName = getColumnName(field);

            if (count != 0) {
                insertSql.append(",");
            }
            insertSql.append(columnName);

            if (count != 0) {
                paramsSql.append(",");
            }
            paramsSql.append("?");

            params.add(value);
            count++;
        }

        insertSql.append(") values (");
        insertSql.append(paramsSql).append(")");
        return new SqlAndParams(insertSql.toString(), params.toArray());
    }

    /**
     * 获取更新语句
     */
    public SqlAndParams getUpdateSql(T obj) throws Exception {
        // 用来存放update语句
        StringBuilder updateSql = new StringBuilder();
        // 用来存放where语句
        StringBuilder whereSql = new StringBuilder();
        // 用来存放参数值
        List<Object> params = new ArrayList<>();
        // 用来存储id
        Object idValue = null;
        // 分析表名
        String tableName = getTableName(obj.getClass());

        updateSql.append("update ").append(tableName).append(" set");

        // 分析列
        Field[] fields = obj.getClass().getDeclaredFields();

        // 用于计数
        int count = 0;
        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation == null) { // 没有column注解的字段跳过
                continue;
            }
            boolean updatable = columnAnnotation.updatable();
            if (!updatable) {
                continue;
            }
            // 获取具体参数值
            Method getter = getGetter(obj.getClass(), field);

            if (getter == null) {
                continue;
            }

            Object value = getter.invoke(obj);
            if (value == null) {
                continue;
            }

            Transient tranAnno = field.getAnnotation(Transient.class);
            if (tranAnno != null) {
                // 如果有 Transient 标记直接跳过
                continue;
            }

            // 获取字段名
            String columnName = getColumnName(field);

            // 看看是不是主键
            Id idAnno = field.getAnnotation(Id.class);
            if (idAnno != null) {
                // 如果是主键
                whereSql.append(columnName).append(" = ?");
                idValue = value;
                continue;
            }

            // 如果是普通列
            params.add(value);

            if (count != 0) {
                updateSql.append(",");
            }
            updateSql.append(" ").append(columnName).append(" = ?");
            count++;
        }

        updateSql.append(" where ");
        updateSql.append(whereSql);
        params.add(idValue);

        return new SqlAndParams(updateSql.toString(), params.toArray());
    }

    /**
     * 从实体类类获取表名
     */
    private static <T> String getTableName(Class<T> clazz) {
        Table tableAnno = clazz.getAnnotation(Table.class);
        if (tableAnno != null) {
            if (tableAnno.catalog() != null && !"".equals(tableAnno.catalog())) {
                return tableAnno.catalog() + "." + tableAnno.name();
            }
            return tableAnno.name();
        }
        String className = clazz.getName();
        return CamelNameUtils.camel2underscore(className.substring(className.lastIndexOf(".") + 1));
    }

    /**
     * 获取自增主键字段名
     */
    public String getAutoGeneratedId(T obj) {
        String autoGeneratedId = "";
        // 根据注解获取自增主键字段名
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            Id idAnno = field.getAnnotation(Id.class);
            if (idAnno == null) {
                continue;
            }
            autoGeneratedId = field.getName();
            break;
        }
        return autoGeneratedId;
    }

    /**
     * 获取属性的getter方法
     */
    private static <T> Method getGetter(Class<T> clazz, Field field) {
        String getterName = "get" + CamelNameUtils.capitalize(field.getName());
        Method getter = null;
        try {
            getter = clazz.getMethod(getterName);
        } catch (Exception e) {
            log.error(getterName + " doesn't exist!", e);
        }
        return getter;
    }

    /**
     * 获取字段名
     */
    private static String getColumnName(Field field) {
        String columnName = "";
        Column columnAnno = field.getAnnotation(Column.class);
        if (columnAnno != null) {
            // 如果是列注解就读取name属性
            columnName = columnAnno.name();
        }
        if (columnName == null || "".equals(columnName)) {
            // 如果没有列注解就用属性名
            columnName = CamelNameUtils.camel2underscore(field.getName());
        }
        return columnName;
    }

    /**
     * 获取自增主键PreparedStatementCreator
     */
    public AutoGeneratePreparedStatementCreator getAutoGeneratePreparedStatementCreator(String sql, Object[] params, T obj) {
        String autoGeneratedId = getAutoGeneratedId(obj);
        return new AutoGeneratePreparedStatementCreator(sql, params, autoGeneratedId);
    }

}
