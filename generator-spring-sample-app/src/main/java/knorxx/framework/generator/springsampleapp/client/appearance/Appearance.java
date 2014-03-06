package knorxx.framework.generator.springsampleapp.client.appearance;

import com.projetloki.genesis.Properties;
import java.awt.Color;
import knorxx.framework.generator.web.client.CssDefinition;

/**
 *
 * @author sj
 */
public class Appearance extends CssDefinition {
    
    private static final Color BASE_COLOR = Color.GREEN;
    
    public static final Color DEFAULT_COLOR = BASE_COLOR.darker().darker();
    public static final Color HIGHLIGHT_COLOR = BASE_COLOR;

    public static final String HEADING_STYLE = build(Properties.builder()
            .setBackground(converColor(DEFAULT_COLOR))
            .setColor(converColor(Color.WHITE)));
    
    public static final String HIGHLIGHT_STYLE = build(Properties.builder()
            .setBackground(converColor(HIGHLIGHT_COLOR)));
}
