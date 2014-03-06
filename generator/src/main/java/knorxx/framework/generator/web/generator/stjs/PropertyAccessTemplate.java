package knorxx.framework.generator.web.generator.stjs;

import japa.parser.ast.expr.MethodCallExpr;
import java.lang.reflect.Modifier;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.ast.ASTNodeData;
import org.stjs.generator.type.MethodWrapper;
import org.stjs.generator.writer.JavascriptWriterVisitor;
import org.stjs.generator.writer.template.MethodCallTemplate;
import org.stjs.generator.writer.template.TemplateUtils;

/**
 *
 * @author sj
 */
public class PropertyAccessTemplate implements MethodCallTemplate {

    @Override
    public boolean write(JavascriptWriterVisitor currentHandler, MethodCallExpr n, GenerationContext context) {
        int arg = 0;
		MethodWrapper method = ASTNodeData.resolvedMethod(n);
        
		if (Modifier.isStatic(method.getModifiers())) {
			currentHandler.getPrinter().print("(");
			n.getArgs().get(arg++).accept(currentHandler, context);
			currentHandler.getPrinter().print(").");
		} else {
			TemplateUtils.printScope(currentHandler, n, context, true);
		}
        
		int start = 0;
        boolean firstToLower = false;
        
        if(n.getName().startsWith("$")) {
            start = 1;
        } else if (n.getName().startsWith("get") || n.getName().startsWith("set")) {
            start = 3;
            firstToLower = true;
        }
        
		if ((n.getArgs() == null) || (n.getArgs().size() == arg)) {
			currentHandler.getPrinter().print(formatName(n, start, firstToLower));
		} else {
			currentHandler.getPrinter().print(formatName(n, start, firstToLower));
			currentHandler.getPrinter().print(" = ");
			n.getArgs().get(arg).accept(currentHandler, context);
		}
        
		return true;
    }
    
    private String formatName(MethodCallExpr expr, int start, boolean firstToLower) {
        String name = expr.getName();
        name = name.substring(start);
        return firstToLower ? Character.toString(name.charAt(0)).toLowerCase() + name.substring(1) : name;
    }
}
