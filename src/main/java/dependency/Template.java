package dependency;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

public class Template {
    private String templateName;
	private VelocityContext velocityContext;
	private Writer output;
	private VelocityEngine velocityEngine;
	private ByteArrayOutputStream outTmp = new ByteArrayOutputStream();
	
	public Template(String templateName, VelocityContext velocityContext, Writer output, VelocityEngine velocityEngine){
		this.templateName = templateName;
		this.velocityContext = velocityContext;
		this.output = output;
		this.velocityEngine = velocityEngine;
	}
	public void outPut() throws IOException {
		org.apache.velocity.Template t = velocityEngine.getTemplate(templateName);

		t.merge(velocityContext, output);
        output.flush();

	}

}