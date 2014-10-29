package knorxx.framework.generator.web.generator.stjs;

import org.stjs.generator.check.CheckVisitor;
import org.stjs.generator.plugin.STJSGenerationPlugin;
import org.stjs.generator.visitor.DiscriminatorKey;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

/**
 *
 * @author sj
 */
public class KnorxxPlugin<JS> implements STJSGenerationPlugin<JS> {

    @Override
    public boolean loadByDefault() {
        return true;
    }

    @Override
    public void contributeCheckVisitor(CheckVisitor visitor) {        
    }

    @Override
    public void contributeWriteVisitor(WriterVisitor<JS> visitor) {
		visitor.contribute(new TypedNewClassWriter<JS>());
		
        visitor.contribute(template("propertyAccess"), new PropertyAccessTemplate());		
    }
    
    private DiscriminatorKey template(String name) {
		return DiscriminatorKey.of(MethodInvocationWriter.class.getSimpleName(), name);
	}
}
