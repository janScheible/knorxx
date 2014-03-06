package knorxx.framework.generator.springsampleapp.client.page;

import static knorxx.framework.generator.springsampleapp.client.appearance.Appearance.HEADING_STYLE;
import static knorxx.framework.generator.springsampleapp.client.appearance.Appearance.HIGHLIGHT_STYLE;
import knorxx.framework.generator.springsampleapp.client.util.AbstractWebPage;
import knorxx.framework.generator.springsampleapp.server.model.TestEntity;
import knorxx.framework.generator.springsampleapp.server.service.StorageService;
import org.springframework.stereotype.Component;
import org.stjs.javascript.functions.Callback1;
import static org.stjs.javascript.jquery.GlobalJQuery.$;

/**
 * @author sj
 */
@Component
public class ServiceWebPage extends AbstractWebPage {
    
    private static StorageService storageService = new StorageService();
    
    @Override
    public void onLoad() {
        $(TITLE_ID).text("Service WebPage").addClass(HEADING_STYLE);

        storageService.getById(null, 0, new Callback1<TestEntity>() {
            @Override
            public void $invoke(TestEntity testEntity) {
                $(CONTENT_ID).append($("<div></div>").text(testEntity.getName()).addClass(HIGHLIGHT_STYLE));
            }
        }, this);
    }
}
