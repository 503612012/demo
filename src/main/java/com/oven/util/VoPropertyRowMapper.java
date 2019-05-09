package com.oven.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * JDBC关系映射工具类
 *
 * @author Oven
 */
public class VoPropertyRowMapper<T> implements RowMapper<T> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Class<T> mappedClass;
    private boolean checkFullyPopulated = false;
    private boolean primitivesDefaultedForNullValue = false;
    private Map<String, PropertyDescriptor> mappedFields;
    private Set<String> mappedProperties;

    public VoPropertyRowMapper() {
    }

    public VoPropertyRowMapper(Class<T> mappedClass) {
        this.initialize(mappedClass);
    }

    public VoPropertyRowMapper(Class<T> mappedClass, boolean checkFullyPopulated) {
        this.initialize(mappedClass);
        this.checkFullyPopulated = checkFullyPopulated;
    }

    public void setMappedClass(Class<T> mappedClass) {
        if (this.mappedClass == null) {
            this.initialize(mappedClass);
        } else if (!this.mappedClass.equals(mappedClass)) {
            throw new InvalidDataAccessApiUsageException("The mapped class can not be reassigned to map to " + mappedClass + " since it is already providing mapping for " + this.mappedClass);
        }

    }

    protected void initialize(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
        this.mappedFields = new HashMap();
        this.mappedProperties = new HashSet();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
        PropertyDescriptor[] arr$ = pds;
        int len$ = pds.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            PropertyDescriptor pd = arr$[i$];
            String propertyName = pd.getName();
            Method method = pd.getWriteMethod();
            if (method != null) {
                Column column = this.getClassFieldColumnInfo(mappedClass, propertyName);
                String underscoredName;
                if (column != null) {
                    underscoredName = column.name();
                    this.mappedFields.put(underscoredName.toLowerCase(), pd);
                } else {
                    this.mappedFields.put(pd.getName().toLowerCase(), pd);
                    underscoredName = this.underscoreName(pd.getName());
                    if (!pd.getName().toLowerCase().equals(underscoredName)) {
                        this.mappedFields.put(underscoredName, pd);
                    }
                }

                this.mappedProperties.add(pd.getName());
            }
        }

    }

    private Column getClassFieldColumnInfo(Class<T> mappedClass, String propertyName) {
        Column column = null;
        Field[] fields = mappedClass.getDeclaredFields();

        for (int i = 0; i < fields.length; ++i) {
            Field f = fields[i];
            if (f.getName().equals(propertyName)) {
                column = (Column) f.getAnnotation(Column.class);
                break;
            }
        }

        return column;
    }

    private String underscoreName(String name) {
        if (!StringUtils.hasLength(name)) {
            return "";
        } else {
            StringBuilder result = new StringBuilder();
            result.append(name.substring(0, 1).toLowerCase());

            for (int i = 1; i < name.length(); ++i) {
                String s = name.substring(i, i + 1);
                String slc = s.toLowerCase();
                if (!s.equals(slc)) {
                    result.append("_").append(slc);
                } else {
                    result.append(s);
                }
            }

            return result.toString();
        }
    }

    public final Class<T> getMappedClass() {
        return this.mappedClass;
    }

    public void setCheckFullyPopulated(boolean checkFullyPopulated) {
        this.checkFullyPopulated = checkFullyPopulated;
    }

    public boolean isCheckFullyPopulated() {
        return this.checkFullyPopulated;
    }

    public void setPrimitivesDefaultedForNullValue(boolean primitivesDefaultedForNullValue) {
        this.primitivesDefaultedForNullValue = primitivesDefaultedForNullValue;
    }

    public boolean isPrimitivesDefaultedForNullValue() {
        return this.primitivesDefaultedForNullValue;
    }

    public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Assert.state(this.mappedClass != null, "Mapped class was not specified");
        T mappedObject = BeanUtils.instantiate(this.mappedClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
        this.initBeanWrapper(bw);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Set<String> populatedProperties = this.isCheckFullyPopulated() ? new HashSet() : null;

        for (int index = 1; index <= columnCount; ++index) {
            String column = JdbcUtils.lookupColumnName(rsmd, index);
            PropertyDescriptor pd = (PropertyDescriptor) this.mappedFields.get(column.replaceAll(" ", "").toLowerCase());
            if (pd != null) {
                try {
                    Object value = this.getColumnValue(rs, index, pd);
                    if (this.logger.isDebugEnabled() && rowNumber == 0) {
                        this.logger.debug("Mapping column '" + column + "' to property '" + pd.getName() + "' of type " + pd.getPropertyType());
                    }

                    try {
                        bw.setPropertyValue(pd.getName(), value);
                    } catch (TypeMismatchException var13) {
                        if (value != null || !this.primitivesDefaultedForNullValue) {
                            throw var13;
                        }

                        this.logger.debug("Intercepted TypeMismatchException for row " + rowNumber + " and column '" + column + "' with value " + value + " when setting property '" + pd.getName() + "' of type " + pd.getPropertyType() + " on object: " + mappedObject);
                    }

                    if (populatedProperties != null) {
                        populatedProperties.add(pd.getName());
                    }
                } catch (NotWritablePropertyException var14) {
                    throw new DataRetrievalFailureException("Unable to map column " + column + " to property " + pd.getName(), var14);
                }
            }
        }

        if (populatedProperties != null && !populatedProperties.equals(this.mappedProperties)) {
            throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields necessary to populate object of class [" + this.mappedClass + "]: " + this.mappedProperties);
        } else {
            return mappedObject;
        }
    }

    protected void initBeanWrapper(BeanWrapper bw) {
    }

    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
    }

    public static <T> BeanPropertyRowMapper<T> newInstance(Class<T> mappedClass) {
        BeanPropertyRowMapper<T> newInstance = new BeanPropertyRowMapper();
        newInstance.setMappedClass(mappedClass);
        return newInstance;
    }
}