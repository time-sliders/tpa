package common;

import java.io.Serializable;

/**
 * 查询条件<br/>
 * 作为Facade层暴露给外部的通用Query对象父类
 * 定义一些子类共用属性
 *
 * @author yongjian
 */
public class Query implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 默认的查询条数
     */
    public static final int DEFAULT_LIMIT = 20;

    /**
     * 默认的偏移量
     */
    public static final int DEFAULT_OFFSET = 0;

    /**
     * 查询条数
     */
    private int limit = DEFAULT_LIMIT;

    /**
     * 偏移量
     */
    private int offset = DEFAULT_OFFSET;

    /**
     * 排序字段
     */
    private OrderBy orderBy;

    /**
     * 强制使用索引名称
     */
    private String forceIndexName;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public String getForceIndexName() {
        return forceIndexName;
    }

    public void setForceIndexName(String forceIndexName) {
        this.forceIndexName = forceIndexName;
    }

    public void decorate() {
        offset = Math.max(offset, 0);
        limit = limit <= 0 ? DEFAULT_LIMIT : limit;
    }

}
