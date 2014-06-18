package knorxx.framework.generator.springsampleapp.client.appearance;

import com.projetloki.genesis.Properties;
import java.awt.Color;
import knorxx.framework.generator.web.client.css.CssDefinition;
import knorxx.framework.generator.web.client.css.annotation.NoRule;
import knorxx.framework.generator.web.client.css.annotation.StyleScopeClass;

/**
 *
 * @author sj
 */
@StyleScopeClass(Appearance.STYLE_SCOPE_CLASS)
public class Appearance extends CssDefinition {
	
	@NoRule
	public static final String STYLE_SCOPE_CLASS = "springSampleApplication";
    
    private static final Color BASE_COLOR = Color.GREEN;
    
    public static final Color DEFAULT_COLOR = BASE_COLOR.darker().darker();
    public static final Color HIGHLIGHT_COLOR = BASE_COLOR;

    public static final String HEADING_STYLE = build(Properties.builder()
            .setBackground(converColor(DEFAULT_COLOR))
            .setColor(converColor(Color.WHITE)));
    
    public static final String HIGHLIGHT_STYLE = build(Properties.builder()
            .setBackground(converColor(HIGHLIGHT_COLOR)));
}
