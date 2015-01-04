package knorxx.framework.generator.web.generator.stjs;

import com.sun.source.tree.MethodInvocationTree;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.TreeUtils;
import org.stjs.generator.javascript.AssignOperator;
import org.stjs.generator.utils.JavaNodes;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.MethodInvocationWriter;

/**
 *
 * @author sj
 */
public class PropertyAccessTemplate<JS> implements WriterContributor<MethodInvocationTree, JS> { // implements MethodCallTemplate {

    @Override
    public JS visit(WriterVisitor<JS> visitor, MethodInvocationTree tree, GenerationContext<JS> context) {
		int argCount = tree.getArguments().size();
		if (argCount > 1) {
			throw context.addError(tree, "A 'PropertyAccess' template can only be applied for methods with 0 or 1 parameters");
		} else if(JavaNodes.isStatic(TreeUtils.elementFromUse(tree))) {
            throw context.addError(tree, "A 'PropertyAccess' template can only be applied for non static methods");
            
        }
		
		// NAME
		String name = MethodInvocationWriter.buildMethodName(tree);
        int start = 0;
        boolean firstToLower = false;
        
        if(name.startsWith("$")) {
            start = 1;
        } else if (name.startsWith("get") || name.startsWith("set")) {
            start = 3;
            firstToLower = true;
        } else if (name.startsWith("is")) {
			start = 2;
			firstToLower = true;
		}
  
        JS target = MethodInvocationWriter.buildTarget(visitor, context.<MethodInvocationTree>getCurrentWrapper());
		JS property = context.js().property(target, formatName(name, start, firstToLower));

		// VALUE
		if (argCount == 0) {
			// getMethod() or $method()
			return property;
		} else {
            // setMethod(x) or $method(x)
            return context.js().assignment(AssignOperator.ASSIGN, property, visitor.scan(tree.getArguments().get(0), context));
        }
    }
    
    private String formatName(String name, int start, boolean firstToLower) {
        name = name.substring(start);
        return firstToLower ? Character.toString(name.charAt(0)).toLowerCase() + name.substring(1) : name;
    }
}
