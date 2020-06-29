package dependency;

/**
 * @author 卢云(luyun)
 * @since 2019.08.23
 */
public class BuildConfig {

    /**
     * 是否需要 DO 数据访问层模型
     * 如果为 true 会自动生成 TableDO TableDOConverter 以及在 Service 层做 Model 到 DO 的转换代码
     * 如果为 false 则直接将 Model 作为 DO 使用，代码会更轻便
     */
    private boolean needDO = false;

    /**
     * 是否需要 Facade 层代码
     * 如果为 false 则不会生成 TableFacade TableFacadeImpl
     */
    private boolean needFacade = false;

    /**
     * 文件头
     */
    private String fileHeader;

    public void needDO(boolean needDO) {
        this.needDO = needDO;
    }

    public void needFacade(boolean needFacade) {
        this.needFacade = needFacade;
    }

    public boolean isNeedDO() {
        return needDO;
    }

    public void setNeedDO(boolean needDO) {
        this.needDO = needDO;
    }

    public boolean isNeedFacade() {
        return needFacade;
    }

    public void setNeedFacade(boolean needFacade) {
        this.needFacade = needFacade;
    }

    public String getFileHeader() {
        return fileHeader;
    }

    public void setFileHeader(String fileHeader) {
        this.fileHeader = fileHeader;
    }
}
