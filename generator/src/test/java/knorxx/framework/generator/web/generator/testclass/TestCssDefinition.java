package knorxx.framework.generator.web.generator.testclass;

import com.projetloki.genesis.Properties;
import java.awt.Color;
import static knorxx.framework.generator.web.client.css.CssDefinition.build;
import static knorxx.framework.generator.web.client.css.CssDefinition.converColor;

/**
 *
 * @author sj
 */
public class TestCssDefinition {
    
    private static final Color BASE_COLOR = Color.GREEN;
    
    public static final Color DEFAULT_COLOR = BASE_COLOR.darker().darker();
    public static final Color HIGHLIGHT_COLOR = BASE_COLOR;

    public static final String HEADING = build(Properties.builder()
            .setBackground(converColor(DEFAULT_COLOR))
            .setColor(converColor(Color.WHITE)));
    
    public static final String COLOR_HIGHLIGHT = build(Properties.builder()
            .setBackground(converColor(HIGHLIGHT_COLOR)));
}
