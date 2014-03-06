package knorxx.framework.generator.web.generator;

import com.google.common.base.Joiner;
import java.lang.reflect.Method;

/**
 *
 * @author sj
 */
public abstract class AbstractJavaScriptBuilder<T extends AbstractJavaScriptBuilder<T>> {

    protected final StringBuilder source = new StringBuilder();
    private int indentation = 0;
    private boolean noIndent = false;
    
    protected abstract T self();

    public T noIndent() {
        noIndent = true;
        return self();
    }

    public T _noIndent() {
        noIndent = false;
        return self();
    }

    public T newLine() {
        source.append("\n");
        return self();
    }
    
    public T namespace(Class javaClass) {
        indent();
        source.append("stjs.ns('");
        source.append(javaClass.getPackage().getName());
        source.append("');\n\n");
        return self();
    }    

    public T jsonStringifyObjectLiteral() {
        indent();
        source.append("JSON.stringify({");
        indentation++;
        return self();
    }

    public T _jsonStringifyObjectLiteral() {
        indentation--;
        indent();
        source.append("})");
        return self();
    }

    public T constructor(Class javaClass) {
        indent();
        source.append(javaClass.getName());
        source.append(" = function() {\n");
        indentation++;
        return self();
    }

    public T _constructor() {
        indentation--;
        indent();
        source.append("}\n\n");
        return self();
    }

    public T if$(String expression) {
        indent();
        source.append("if(");
        source.append(expression);
        source.append(") {\n");
        indentation++;
        return self();
    }
    
    public T else$() {
        indentation--;
        indent();
        source.append("} else {\n");
        indentation++;
        return self();
    }

    public T _if() {
        indentation--;
        indent();
        source.append("}\n");
        return self();
    }

    public T function(Class javaClass, Method method, String... argumentNames) {
        return function(javaClass, method.getName(), argumentNames);
    }

    public T function(Class javaClass, String methodName, String... argumentNames) {
        indent();
        source.append(javaClass.getName());
        source.append(".prototype.");
        source.append(methodName);
        source.append(" = function(");
        source.append(Joiner.on(", ").join(argumentNames));
        source.append(") {\n");
        indentation++;
        return self();
    }

    public T staticFunction(Class javaClass, String methodName, String... argumentNames) {
        indent();
        source.append(javaClass.getName());
        source.append(".");
        source.append(methodName);
        source.append(" = function(");
        source.append(Joiner.on(", ").join(argumentNames));
        source.append(") {\n");
        indentation++;
        return self();
    }

    public T anonymousFunction(String... argumentNames) {
        source.append("function(");
        source.append(Joiner.on(", ").join(argumentNames));
        source.append(") {\n");
        indentation++;
        return self();
    }

    public T _function() {
        indentation--;
        indent();
        source.append("}\n\n");
        return self();
    }

    public T code(String code, Object... args) {
        indent();
        source.append(String.format(code, args));
        return self();
    }

    public T literal(boolean value) {
        source.append(Boolean.toString(value));
        return self();
    }

    public T literal(String value) {
        source.append("'");
        source.append(value);
        source.append("'");
        return self();
    }

    public T comma() {
        source.append(", ");
        return self();
    }

    public T semicolon() {
        source.append(";");
        return self();
    }

    public T jQueryPost() {
        indent();
        source.append("$.ajax({ type: 'POST',\n");
        indentation++;
        return self();
    }

    public T _jQueryPost() {
        indentation--;
        indent();
        source.append("});\n");
        return self();
    }

    public T property(String propertyName) {
        indent();
        source.append(propertyName);
        source.append(" : ");
        return self();
    }

    public String create() {
        return source.toString();
    }

    protected void indent() {
        if (!noIndent) {
            for (int i = 0; i < indentation * 4; i++) {
                source.append(" ");
            }
        }
    }    
}
