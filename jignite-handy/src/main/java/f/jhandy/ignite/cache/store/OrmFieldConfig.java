package f.jhandy.ignite.cache.store;

import org.apache.ignite.cache.store.jdbc.JdbcTypeField;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;

/**
 * @author sunmoonone
 * @version 2018/12/26
 */
public class OrmFieldConfig {

    private boolean pk;
    private String column;
    private String sqlType;

    public boolean isPk() {
        return pk;
    }

    public void setPk(boolean pk) {
        this.pk = pk;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public JdbcTypeField buildJdbcTypeField(String name) {

        JdbcTypeField field = new JdbcTypeField();
        field.setJavaFieldName(name);
        field.setDatabaseFieldName(column);

        field.setDatabaseFieldType(mapSqlType(sqlType));
        field.setJavaFieldType(mapJavaType(sqlType));

        return field;
    }

    private Class<?> mapJavaType(String sqlType) {
        switch (sqlType.toUpperCase()) {
            case "TINYTEXT":
            case "MEDIUMTEXT":
            case "LONGTEXT":
            case "TEXT":
            case "NCHAR":
            case "NVARCHAR":
            case "LONGNVARCHAR":
            case "CHAR":
            case "VARCHAR":
            case "LONGVARCHAR":
                return String.class;
            case "INT":
            case "TINYINT":
            case "SMALLINT":
            case "INTEGER":
                return Integer.class;
            case "BIGINT":
                return Long.class;
            case "FLOAT":
                return Float.class;
            case "DOUBLE":
                return Double.class;
            case "NUMERIC":
                return Double.class;
            case "DECIMAL":
                return BigDecimal.class;
            case "BINARY":
            case "VARBINARY":
                return byte[].class;
            case "DATE":
                return Date.class;
            case "TIME":
                return Date.class;
            case "TIMESTAMP":
                return Date.class;
            case "BOOLEAN":
                return Boolean.class;
            case "TIME_WITH_TIMEZONE":
            case "TIMESTAMP_WITH_TIMEZONE":
                return Date.class;
        }
        throw new IllegalArgumentException("not supported sqlType:" + sqlType);

    }

    private int mapSqlType(String sqlType) {

        switch (sqlType.toUpperCase()) {
            case "TINYTEXT":
                return Types.VARCHAR;
            case "MEDIUMTEXT":
                return Types.VARCHAR;
            case "LONGTEXT":
                return Types.VARCHAR;
            case "TEXT":
                return Types.VARCHAR;
            case "TINYINT":
                return Types.TINYINT;
            case "SMALLINT":
                return Types.SMALLINT;
            case "INT":
            case "INTEGER":
                return Types.INTEGER;
            case "BIGINT":
                return Types.BIGINT;
            case "FLOAT":
                return Types.FLOAT;
            case "DOUBLE":
                return Types.DOUBLE;
            case "NUMERIC":
                return Types.NUMERIC;
            case "DECIMAL":
                return Types.DECIMAL;
            case "CHAR":
                return Types.CHAR;
            case "VARCHAR":
                return Types.VARCHAR;
            case "LONGVARCHAR":
                return Types.LONGVARCHAR;
            case "BINARY":
                return Types.BINARY;
            case "VARBINARY":
                return Types.VARBINARY;
            case "DATE":
                return Types.DATE;
            case "TIME":
                return Types.TIME;
            case "TIMESTAMP":
                return Types.TIMESTAMP;
            case "BOOLEAN":
                return Types.BOOLEAN;
            case "NCHAR":
                return Types.NCHAR;
            case "NVARCHAR":
                return Types.NVARCHAR;
            case "LONGNVARCHAR":
                return Types.LONGNVARCHAR;
            case "TIME_WITH_TIMEZONE":
                return Types.TIME_WITH_TIMEZONE;
            case "TIMESTAMP_WITH_TIMEZONE":
                return Types.TIMESTAMP_WITH_TIMEZONE;
        }
        throw new IllegalArgumentException("not supported sqlType:" + sqlType);
    }

}
