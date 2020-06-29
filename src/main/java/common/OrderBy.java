package common;

import java.io.Serializable;

/**
 * @author yongjian 
 */
public class OrderBy implements Serializable{

    private static final long serialVersionUID = 1L;

    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    private String columnName;

    private String type;

    public OrderBy(String columnName, String type) {
        this.columnName = columnName;
        this.type = type;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
