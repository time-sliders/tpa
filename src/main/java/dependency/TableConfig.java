package dependency;

import java.util.List;


public class TableConfig {

    private String packageName;

    private String beanName;

    private String injectName;

    private String tableName;

    private String namespace;

    private String typeAlisName;

    private ColumnConfig primaryKey;

    private List<ColumnConfig> columns;

    private String comment;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public List<ColumnConfig> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnConfig> columns) {
        this.columns = columns;
    }

    public String getTypeAlisName() {
        return typeAlisName;
    }

    public void setTypeAlisName(String typeAlisName) {
        this.typeAlisName = typeAlisName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public ColumnConfig getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(ColumnConfig primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getInjectName() {
        return injectName;
    }

    public void setInjectName(String injectName) {
        this.injectName = injectName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
