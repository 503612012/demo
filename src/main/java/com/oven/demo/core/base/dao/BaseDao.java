package com.oven.demo.core.base.dao;

import com.oven.demo.common.util.VoPropertyRowMapper;
import com.oven.demo.core.base.entity.ConditionAndParams;
import com.oven.demo.core.base.utils.AutoGeneratePreparedStatementCreator;
import com.oven.demo.core.base.utils.CamelNameUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通过DAO
 *
 * @author Oven
 */
@Repository
public class BaseDao<T> {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 通用分页查询
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     */
    public List<T> getByPage(Integer pageNum, Integer pageSize) {
        return getByPage(getBaseSql(), new Object[]{}, pageNum, pageSize);
    }

    /**
     * 通用分页查询
     *
     * @param sql      查询sql语句
     * @param pageNum  页码
     * @param pageSize 每页数量
     */
    public List<T> getByPage(StringBuilder sql, Integer pageNum, Integer pageSize) {
        return getByPage(sql, new Object[]{}, pageNum, pageSize);
    }

    /**
     * 通用分页查询（带条件、不带排序）
     *
     * @param pageNum            页码
     * @param pageSize           每页数量
     * @param conditionAndParams 过滤条件和值
     */
    public List<T> getByPage(Integer pageNum, Integer pageSize, ConditionAndParams conditionAndParams) {
        StringBuilder sql = getBaseSql().append(conditionAndParams.getCondition());
        return getByPage(sql, conditionAndParams.getParams(), pageNum, pageSize);
    }

    /**
     * 通用分页查询（带条件、不带排序）
     *
     * @param sql                查询sql语句
     * @param pageNum            页码
     * @param pageSize           每页数量
     * @param conditionAndParams 过滤条件和值
     */
    public List<T> getByPage(StringBuilder sql, Integer pageNum, Integer pageSize, ConditionAndParams conditionAndParams) {
        sql.append(conditionAndParams.getCondition());
        return getByPage(sql, conditionAndParams.getParams(), pageNum, pageSize);
    }

    /**
     * 通用分页查询（不带条件、带排序）
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @param orderby  排序字段（实例：create_time desc）
     */
    public List<T> getByPage(Integer pageNum, Integer pageSize, String orderby) {
        StringBuilder sql = getBaseSql().append(" order by ").append(orderby);
        return getByPage(sql, new Object[]{}, pageNum, pageSize);
    }

    /**
     * 通用分页查询（不带条件、带排序）
     *
     * @param sql      查询sql语句
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @param orderby  排序字段（实例：create_time desc）
     */
    public List<T> getByPage(StringBuilder sql, Integer pageNum, Integer pageSize, String orderby) {
        sql.append(" order by ").append(orderby);
        return getByPage(sql, new Object[]{}, pageNum, pageSize);
    }

    /**
     * 通用分页查询（带条件、带排序）
     *
     * @param pageNum            页码
     * @param pageSize           每页数量
     * @param conditionAndParams 过滤条件和值
     * @param orderby            排序字段（实例：create_time desc）
     */
    public List<T> getByPage(Integer pageNum, Integer pageSize, ConditionAndParams conditionAndParams, String orderby) {
        StringBuilder sql = getBaseSql().append(conditionAndParams.getCondition()).append(" order by ").append(orderby);
        return getByPage(sql, conditionAndParams.getParams(), pageNum, pageSize);
    }

    /**
     * 通用分页查询（带条件、带排序）
     *
     * @param sql                查询sql语句
     * @param pageNum            页码
     * @param pageSize           每页数量
     * @param conditionAndParams 过滤条件和值
     * @param orderby            排序字段（实例：create_time desc）
     */
    public List<T> getByPage(StringBuilder sql, Integer pageNum, Integer pageSize, ConditionAndParams conditionAndParams, String orderby) {
        sql.append(conditionAndParams.getCondition()).append(" order by ").append(orderby);
        return getByPage(sql, conditionAndParams.getParams(), pageNum, pageSize);
    }

    /**
     * 分页查询
     *
     * @param sb       查询sql语句
     * @param params   过滤参数值
     * @param pageNum  页码
     * @param pageSize 每页数量
     */
    private List<T> getByPage(StringBuilder sb, Object[] params, Integer pageNum, Integer pageSize) {
        sb.append(" limit ?,?");
        String sql = sb.toString().replaceFirst("and", "where");
        List<Object> args = new ArrayList<>(Arrays.asList(params));
        args.add((pageNum - 1) * pageSize);
        args.add(pageSize);
        return jdbcTemplate.query(sql, args.toArray(), new VoPropertyRowMapper<>(getGenericClass()));
    }

    /**
     * 查询总数量
     */
    public Integer getTotalNum() {
        return getTotalNum(getBaseCountSql(), new Object[]{});
    }

    /**
     * 查询总数量
     *
     * @param sql 查询sql语句
     */
    public Integer getTotalNum(StringBuilder sql) {
        return getTotalNum(sql, new Object[]{});
    }

    /**
     * 查询总数量（带条件）
     *
     * @param conditionAndParams 过滤条件和值
     */
    public Integer getTotalNum(ConditionAndParams conditionAndParams) {
        StringBuilder sql = getBaseCountSql().append(conditionAndParams.getCondition());
        return getTotalNum(sql, conditionAndParams.getParams());
    }

    /**
     * 查询总数量（带条件）
     *
     * @param sql                查询sql语句
     * @param conditionAndParams 过滤条件和值
     */
    public Integer getTotalNum(StringBuilder sql, ConditionAndParams conditionAndParams) {
        sql.append(conditionAndParams.getCondition());
        return getTotalNum(sql, conditionAndParams.getParams());
    }

    /**
     * 查询总数量
     */
    public Integer getTotalNum(StringBuilder sb, Object[] params) {
        String sql = sb.toString().replaceFirst("and", "where");
        return jdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    /**
     * 添加
     */
    public int save(T obj) throws Exception {
        ConditionAndParams conditionAndParams = getInsertSql(obj);
        AutoGeneratePreparedStatementCreator creator = getAutoGeneratePreparedStatementCreator(conditionAndParams.getCondition(), conditionAndParams.getParams(), obj.getClass());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(creator, keyHolder);
        Number key = keyHolder.getKey();
        return key == null ? -1 : key.intValue();
    }

    /**
     * 修改
     */
    public int update(T obj) throws Exception {
        ConditionAndParams conditionAndParams = getUpdateSql(obj);
        return jdbcTemplate.update(conditionAndParams.getCondition(), conditionAndParams.getParams());
    }

    /**
     * 删除
     */
    public int delete(Object id) throws Exception {
        String sql = "delete from " + getTableName(getGenericClass()) + " where " + getAutoGeneratedColumn(getGenericClass()) + " = ?";
        return jdbcTemplate.update(sql, id);
    }

    /**
     * 根据主键获取
     */
    public T getById(Object id) {
        StringBuilder sql = getBaseSql().append(" where ").append(getAutoGeneratedColumn(getGenericClass())).append(" = ?");
        List<T> list = this.jdbcTemplate.query(sql.toString(), new VoPropertyRowMapper<>(getGenericClass()), id);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 查询一个
     */
    public T getOne(ConditionAndParams conditionAndParams) {
        String sql = getBaseSql().append(conditionAndParams.getCondition()).toString().replaceFirst("and", "where");
        List<T> list = this.jdbcTemplate.query(sql, conditionAndParams.getParams(), new VoPropertyRowMapper<>(getGenericClass()));
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 获取全部
     */
    public List<T> getAll() {
        return jdbcTemplate.query(getBaseSql().toString(), new VoPropertyRowMapper<>(getGenericClass()));
    }

    /**
     * 获取全部（带条件）
     *
     * @param conditionAndParams 过滤条件和值
     */
    public List<T> getAll(ConditionAndParams conditionAndParams) {
        String sql = getBaseSql().append(conditionAndParams.getCondition()).toString().replaceFirst("and", "where");
        return jdbcTemplate.query(sql, conditionAndParams.getParams(), new VoPropertyRowMapper<>(getGenericClass()));
    }

    /**
     * 获取全部（带排序）
     *
     * @param orderby 排序字段（实例：create_time desc）
     */
    public List<T> getAll(String orderby) {
        StringBuilder sql = getBaseSql().append(" order by ").append(orderby);
        return jdbcTemplate.query(sql.toString(), new VoPropertyRowMapper<>(getGenericClass()));
    }

    /**
     * 获取全部（带条件、带排序）
     *
     * @param conditionAndParams 过滤条件和值
     * @param orderby            排序字段（实例：create_time desc）
     */
    public List<T> getAll(ConditionAndParams conditionAndParams, String orderby) {
        String sql = getBaseSql().append(conditionAndParams.getCondition()).append(" order by ").append(orderby).toString().replaceFirst("and", "where");
        return jdbcTemplate.query(sql, conditionAndParams.getParams(), new VoPropertyRowMapper<>(getGenericClass()));
    }

    /**
     * 获取基础查询语句
     */
    private StringBuilder getBaseSql() {
        return new StringBuilder("select * from " + getTableName(getGenericClass()) + " ");
    }

    /**
     * 获取基础统计数量语句
     */
    private StringBuilder getBaseCountSql() {
        return new StringBuilder("select count(*) from " + getTableName(getGenericClass()) + " ");
    }

    /**
     * 获取泛型类
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Class<T> getGenericClass() {
        Type type = getClass().getGenericSuperclass();
        Type actualType = ((ParameterizedType) type).getActualTypeArguments()[0];
        return (Class) actualType;
    }

    /**
     * 获取插入语句
     */
    public ConditionAndParams getInsertSql(T obj) throws Exception {
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
            boolean insertable = columnAnnotation.insertable();
            if (!insertable) {
                continue;
            }
            // 获取具体参数值
            Method getter = getGetter(obj.getClass(), field);

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

        return ConditionAndParams.build(insertSql.toString(), params.toArray());
    }

    /**
     * 获取更新语句
     */
    public ConditionAndParams getUpdateSql(T obj) throws Exception {
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

        return ConditionAndParams.build(updateSql.toString(), params.toArray());
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
     * 获取自增主键数据库属性名
     */
    public static <T> String getAutoGeneratedColumn(Class<T> clazz) {
        String autoGeneratedColumn = "";
        // 根据注解获取自增主键字段名
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            Column columnAnno = field.getAnnotation(Column.class);
            if (columnAnno == null) {
                continue;
            }
            Id idAnno = field.getAnnotation(Id.class);
            if (idAnno == null) {
                continue;
            }
            autoGeneratedColumn = columnAnno.name();
            break;
        }
        return autoGeneratedColumn;
    }

    /**
     * 获取自增主键字段名
     */
    public static <T> String getAutoGeneratedId(Class<T> clazz) {
        String autoGeneratedId = "";
        // 根据注解获取自增主键字段名
        Field[] fields = clazz.getDeclaredFields();
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
    private static <T> Method getGetter(Class<T> clazz, Field field) throws NoSuchMethodException {
        String getterName = "get" + CamelNameUtils.capitalize(field.getName());
        return clazz.getMethod(getterName);
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
    public static <T> AutoGeneratePreparedStatementCreator getAutoGeneratePreparedStatementCreator(String sql, Object[] params, Class<T> clazz) {
        String autoGeneratedId = getAutoGeneratedId(clazz);
        return new AutoGeneratePreparedStatementCreator(sql, params, autoGeneratedId);
    }

}
