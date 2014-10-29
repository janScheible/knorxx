package knorxx.framework.generator.web.generator.stjs;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.NewClassTree;
import javax.lang.model.element.PackageElement;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.ObjectLiteral;
import org.mozilla.javascript.ast.ObjectProperty;
import org.mozilla.javascript.ast.StringLiteral;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.javac.ElementUtils;
import org.stjs.generator.javac.TreeWrapper;
import org.stjs.generator.writer.WriterContributor;
import org.stjs.generator.writer.WriterVisitor;
import org.stjs.generator.writer.expression.NewClassWriter;

/**
 *
 * @author sj
 */
public class  TypedNewClassWriter<JS> implements WriterContributor<NewClassTree, JS> {
	
	private final NewClassWriter<JS> newClassWriter = new NewClassWriter<>();

	@Override
	public JS visit(WriterVisitor<JS> visitor, NewClassTree tree, GenerationContext<JS> context) {
		JS result = newClassWriter.visit(visitor, tree, context);
		TreeWrapper<NewClassTree, JS> tw = context.getCurrentWrapper();
		TreeWrapper<ExpressionTree, JS> classTreeWrapper = tw.child(tree.getIdentifier());
		
		if(result instanceof ObjectLiteral) {			
			((ObjectLiteral) result).addElement(property(string("@type"), string(getName(classTreeWrapper))));
		}
		
		return result;
	}
	
	private ObjectProperty property(AstNode key, AstNode value) {
		ObjectProperty property = new ObjectProperty();
		property.setLeft(key);
		property.setRight(value);
		return property;
	}

	private StringLiteral string(String value) {
		StringLiteral stringLiteral = new StringLiteral();
		stringLiteral.setQuoteCharacter('"');
		stringLiteral.setValue(value);
		return stringLiteral;
	}
	
	private String getName(TreeWrapper<ExpressionTree, JS> treeWrapper) {
		PackageElement pack = ElementUtils.enclosingPackage(treeWrapper.getElement());
		return pack.getQualifiedName() + "." + treeWrapper.getTypeName();
	}
}
