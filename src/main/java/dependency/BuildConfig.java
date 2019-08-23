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
     * 是否需要 DTO 数据传输层模型
     * 如果为 true 会自动生成 TableDTO TableDTOConverter 以及在 Facade 层做 Model 到 DTO 的转换代码
     * 如果为 false 则直接将 Model 作为 DTO 使用，代码会更轻便
     */
    private boolean needDTO = false;

    /**
     * 是否需要 Facade 层代码
     * 如果为 false 则不会生成 TableFacade TableFacadeImpl
     */
    private boolean needFacade = false;

    public BuildConfig needDTO(boolean needDTO) {
        this.needDTO = needDTO;
        return this;
    }

    public BuildConfig needDO(boolean needDO) {
        this.needDO = needDO;
        return this;
    }

    public BuildConfig needFacade(boolean needFacade) {
        this.needFacade = needFacade;
        return this;
    }

    public boolean isNeedDO() {
        return needDO;
    }

    public void setNeedDO(boolean needDO) {
        this.needDO = needDO;
    }

    public boolean isNeedDTO() {
        return needDTO;
    }

    public void setNeedDTO(boolean needDTO) {
        this.needDTO = needDTO;
    }

    public boolean isNeedFacade() {
        return needFacade;
    }

    public void setNeedFacade(boolean needFacade) {
        this.needFacade = needFacade;
    }
}
