package knorxx.framework.generator.springsampleapp.client.page;

import knorxx.framework.generator.extjsbridge.Ext;
import knorxx.framework.generator.extjsbridge.ext.button.Button;
import knorxx.framework.generator.extjsbridge.ext.button.ButtonConfig;
import knorxx.framework.generator.extjsbridge.ext.window.Window;
import knorxx.framework.generator.extjsbridge.ext.window.WindowConfig;
import static knorxx.framework.generator.springsampleapp.client.appearance.Appearance.HEADING_STYLE;
import knorxx.framework.generator.springsampleapp.client.util.AbstractWebPage;
import org.springframework.stereotype.Component;
import static org.stjs.javascript.jquery.GlobalJQueryUI.$;

/**
 *
 * @author sj
 */
@Component
public class ExtJsWebPage extends AbstractWebPage {
    
    @Override
    public void onLoad() {
        $(TITLE_ID).text("ExtJs WebPage").addClass(HEADING_STYLE);
        
        WindowConfig windowConfig = new WindowConfig();
        windowConfig.setXclass("Ext.window.Window");
        windowConfig.setTitle("ExtJs Window");
        windowConfig.setLayout("fit");
        windowConfig.setWidth(640);
        windowConfig.setHeight(480);
                
        Window window = Ext.create(windowConfig);
        
        ButtonConfig buttonConfig = new ButtonConfig();
        buttonConfig.setText("hey ho!");
                
        window.add(new Button(buttonConfig));
        window.show();
    }    
}
