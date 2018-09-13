package dependency;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JdbcTypeService {

    private static JdbcTypeService instances = new JdbcTypeService();

    protected Map<Integer, JdbcType> typeMap;

    private JdbcTypeService() {
        typeMap = new HashMap<Integer, JdbcType>();

        typeMap.put(Types.ARRAY, new JdbcType("ARRAY", //$NON-NLS-1$
                Object.class.getName()));
        typeMap.put(Types.BIGINT, new JdbcType("BIGINT", //$NON-NLS-1$
                Long.class.getName()));
        typeMap.put(Types.BINARY, new JdbcType("BINARY", //$NON-NLS-1$
                "byte[]")); //$NON-NLS-1$
        typeMap.put(Types.BIT, new JdbcType("BIT", //$NON-NLS-1$
                Boolean.class.getName()));
        typeMap.put(Types.BLOB, new JdbcType("BLOB", //$NON-NLS-1$
                "byte[]")); //$NON-NLS-1$
        typeMap.put(Types.BOOLEAN, new JdbcType("BOOLEAN", //$NON-NLS-1$
                Boolean.class.getName()));
        typeMap.put(Types.CHAR, new JdbcType("CHAR", //$NON-NLS-1$
                String.class.getName()));
        typeMap.put(Types.CLOB, new JdbcType("CLOB", //$NON-NLS-1$
                String.class.getName()));
        typeMap.put(Types.DATALINK, new JdbcType("DATALINK", //$NON-NLS-1$
                Object.class.getName()));
        typeMap.put(Types.DATE, new JdbcType("DATE", //$NON-NLS-1$
                Date.class.getName()));
        typeMap.put(Types.DISTINCT, new JdbcType("DISTINCT", //$NON-NLS-1$
                Object.class.getName()));
        typeMap.put(Types.DOUBLE, new JdbcType("DOUBLE", //$NON-NLS-1$
                Double.class.getName()));
        typeMap.put(Types.FLOAT, new JdbcType("FLOAT", //$NON-NLS-1$
                Double.class.getName()));
        typeMap.put(Types.INTEGER, new JdbcType("INTEGER", //$NON-NLS-1$
                Integer.class.getName()));
        typeMap.put(Types.JAVA_OBJECT, new JdbcType("JAVA_OBJECT", //$NON-NLS-1$
                Object.class.getName()));
        typeMap.put(Types.LONGNVARCHAR, new JdbcType("LONGNVARCHAR", //$NON-NLS-1$
                String.class.getName()));
        typeMap.put(Types.LONGVARBINARY, new JdbcType(
                "LONGVARBINARY", //$NON-NLS-1$
                "byte[]")); //$NON-NLS-1$
        typeMap.put(Types.LONGVARCHAR, new JdbcType("LONGVARCHAR", //$NON-NLS-1$
                String.class.getName()));
        typeMap.put(Types.NCHAR, new JdbcType("NCHAR", //$NON-NLS-1$
                String.class.getName()));
        typeMap.put(Types.NCLOB, new JdbcType("NCLOB", //$NON-NLS-1$
                String.class.getName()));
        typeMap.put(Types.NVARCHAR, new JdbcType("NVARCHAR", //$NON-NLS-1$
                String.class.getName()));
        typeMap.put(Types.NULL, new JdbcType("NULL", //$NON-NLS-1$
                Object.class.getName()));
        typeMap.put(Types.OTHER, new JdbcType("OTHER", //$NON-NLS-1$
                Object.class.getName()));
        typeMap.put(Types.REAL, new JdbcType("REAL", //$NON-NLS-1$
                Float.class.getName()));
        typeMap.put(Types.REF, new JdbcType("REF", //$NON-NLS-1$
                Object.class.getName()));
        typeMap.put(Types.SMALLINT, new JdbcType("SMALLINT", //$NON-NLS-1$
                Integer.class.getName()));
        typeMap.put(Types.STRUCT, new JdbcType("STRUCT", //$NON-NLS-1$
                Object.class.getName()));
        typeMap.put(Types.TIME, new JdbcType("TIME", //$NON-NLS-1$
                Date.class.getName()));
        typeMap.put(Types.TIMESTAMP, new JdbcType("TIMESTAMP", //$NON-NLS-1$
                Date.class.getName()));
        typeMap.put(Types.TINYINT, new JdbcType("TINYINT", //$NON-NLS-1$
                Integer.class.getName()));
        typeMap.put(Types.VARBINARY, new JdbcType("VARBINARY", //$NON-NLS-1$
                "byte[]")); //$NON-NLS-1$
        typeMap.put(Types.VARCHAR, new JdbcType("VARCHAR", //$NON-NLS-1$
                String.class.getName()));

        typeMap.put(Types.DECIMAL, new JdbcType("DECIMAL", BigDecimal.class.getName()));

        //##############################################################################
        typeMap.put(ITypes.INT, new JdbcType(ITypes.INT_STR, //$NON-NLS-1$
                Integer.class.getName()));
        //##############################################################################

    }

    public JdbcType getJavaType(Integer jdbcType) {
        JdbcType type = typeMap.get(jdbcType);
        if (type == null || type.getJavaType() == null) {
            throw new RuntimeException("JDBC类型转换错误:" + jdbcType);
        }
        return type;
    }

    public static JdbcTypeService getInstances() {
        return instances;
    }

    public static class JdbcType {

        // VARCHAR
        private String jdbcType;

        // java.lang.String
        private String javaType;

        public JdbcType(String jdbcType, String javaType) {
            this.jdbcType = jdbcType;
            this.javaType = javaType;
        }

        public String getJdbcType() {
            return jdbcType;
        }

        public String getJavaType() {
            return javaType;
        }
    }

    public class ITypes{
        public static final int INT = 1234001;
        public static final String INT_STR = "INT";

    }
}