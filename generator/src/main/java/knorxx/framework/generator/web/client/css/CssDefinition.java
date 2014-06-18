package knorxx.framework.generator.web.client.css;

import com.projetloki.genesis.Properties.Builder;
import com.projetloki.genesis.image.Color;

/**
 *
 * @author sj
 */
public abstract class CssDefinition {
    
    public static Color converColor(java.awt.Color c) {
        return Color.forJavaColor(c);
    }
    
    public static String build(Builder builder) {
        return builder.build().toString();
    }
}
