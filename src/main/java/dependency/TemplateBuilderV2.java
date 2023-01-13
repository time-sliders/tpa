package dependency;

import org.apache.velocity.VelocityContext;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;


public class TemplateBuilderV2 {

    public final static String DOConverter = "domain/Converter.vm";
    public final static String DAO = "domain/DAO.vm";
    public final static String DAOImpl = "domain/DAOImpl.vm";
    public final static String DO = "domain/DO.vm";
    public final static String model = "domain/entity.vm";
    public final static String Query = "domain/Query.vm";
    public final static String MAPPER = "domain/template.vm";
    public final static String Repository = "domain/Repository.vm";
    public final static String RepositoryImpl = "domain/RepositoryImpl.vm";

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
