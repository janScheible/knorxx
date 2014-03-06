package knorxx.framework.generator.javaeesampleapp.client;

import static knorxx.framework.generator.javaeesampleapp.client.appearance.Appearance.HEADING;
import static knorxx.framework.generator.javaeesampleapp.client.appearance.Appearance.HIGHLIGHT;
import knorxx.framework.generator.javaeesampleapp.client.util.AbstractWebPage;
import knorxx.framework.generator.javaeesampleapp.server.model.TestEntity;
import knorxx.framework.generator.javaeesampleapp.server.service.StorageService;
import org.stjs.javascript.functions.Callback1;
import static org.stjs.javascript.jquery.GlobalJQuery.$;

/**
 * @author sj
 */
public class ServiceWebPage extends AbstractWebPage {
    
    private static StorageService storageService = new StorageService();
    
    @Override
    public void onLoad() {
        $(TITLE_ID).text("Service WebPage").addClass(HEADING);

        storageService.getById(null, 0, new Callback1<TestEntity>() {
            @Override
            public void $invoke(TestEntity testEntity) {
                $(CONTENT_ID).append($("<div></div>").text(testEntity.getName()).addClass(HIGHLIGHT));
            }
        }, this);
    }
}
