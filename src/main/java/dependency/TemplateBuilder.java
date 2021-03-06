package dependency;

import org.apache.velocity.VelocityContext;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;


public class TemplateBuilder {

    public final static String MAPPER = "template.vm";
    public final static String model = "model.vm";
    public final static String DAO = "DAO.vm";
    public final static String DAOImpl = "DAOImpl.vm";
    public final static String Service = "Service.vm";
    public final static String ServiceImpl = "ServiceImpl.vm";
    public final static String DOConverter = "Converter.vm";

    public final static String Query = "Query.vm";
    public final static String DO = "DO.vm";
    public final static String dtoModel = "modelDTO.vm";
    public final static String DTOConverter = "DTOConverter.vm";
    public final static String ManageFacade = "ManageFacade.vm";
    public final static String ManageFacadeImpl = "ManageFacadeImpl.vm";
    public final static String QueryFacade = "QueryFacade.vm";
    public final static String QueryFacadeImpl = "QueryFacadeImpl.vm";

    public static String build(TableConfig table, BuildConfig config, String templateName) {
        VelocityContext velocityContext = new VelocityContext();
        ByteArrayOutputStream temp = new ByteArrayOutputStream();
        OutputStreamWriter output = new OutputStreamWriter(temp);
        Template template = VelocityFactory.getInstances().getTemplate(templateName, output, velocityContext);
        velocityContext.put("table", table);
        velocityContext.put("config", config);
        try {
            template.outPut();
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] source;
        source = temp.toByteArray();
        return new String(source);
    }
}
