package dependency;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.util.ExceptionUtils;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.InputStream;

public class URIResourceLoader extends ClasspathResourceLoader {

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    /**
     * This is abstract in the base class, so we need it
     *
     * @param configuration
     */
    @Override
    public void init(ExtendedProperties configuration) {
        if (log.isTraceEnabled()) {
            log.trace("ClasspathResourceLoader : initialization complete.");
        }
    }

    /**
     * Get an InputStream so that the Runtime can build a
     * template with it.
     *
     * @param name name of template to get
     * @return InputStream containing the template
     * @throws ResourceNotFoundException if template not found
     *                                   in  classpath.
     */
    @Override
    public InputStream getResourceStream(String name) throws ResourceNotFoundException {
        InputStream result = null;

        if (StringUtils.isEmpty(name)) {
            throw new ResourceNotFoundException("No template name provided");
        }

        /*
         * look for resource in thread classloader first (e.g. WEB-INF\lib in
         * a servlet container) then fall back to the system classloader.
         */

        try {
            org.springframework.core.io.Resource[] res = resourcePatternResolver.getResources("classpath:" + name);
            if (res != null && res.length == 1){
                result = new VTLIndentationGlobber(res[0].getInputStream());
            }
        } catch (Exception fnfe) {
            throw (ResourceNotFoundException) ExceptionUtils.createWithCause(ResourceNotFoundException.class, "problem with template: " + name, fnfe);
        }

        if (result == null) {
            String msg = "ClasspathResourceLoader Error: cannot find resource " +
                    name;

            throw new ResourceNotFoundException(msg);
        }

        return result;
    }

    /**
     * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#isSourceModified(Resource)
     */
    @Override
    public boolean isSourceModified(Resource resource) {
        return false;
    }

    /**
     * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#getLastModified(Resource)
     */
    @Override
    public long getLastModified(Resource resource) {
        return 0;
    }
}
