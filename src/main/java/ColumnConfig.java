public class ColumnConfig {

    private boolean isPrimaryKey;

    private String columnName;

    private String columnType;

    private String fieldName;

    private String javaType;

    private String SimpleJavaType;

    private String comment;

    public ColumnConfig(boolean isPrimaryKey, String columnName, String fieldName) {
        this.isPrimaryKey = isPrimaryKey;
        this.columnName = columnName;
        this.fieldName = fieldName;
    }

    public ColumnConfig(String columnName, String fieldName) {
        this.columnName = columnName;
        this.fieldName = fieldName;
    }

    public ColumnConfig() {

    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getSimpleJavaType() {
        return SimpleJavaType;
    }

    public void setSimpleJavaType(String simpleJavaType) {
        SimpleJavaType = simpleJavaType;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
