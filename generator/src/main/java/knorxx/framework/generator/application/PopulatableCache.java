package knorxx.framework.generator.application;

import knorxx.framework.generator.GenerationUnit;
import knorxx.framework.generator.library.LibraryUrls;
import knorxx.framework.generator.web.server.rtti.RttiGenerationResult;
import knorxx.framework.generator.web.server.rtti.UrlResolver;

/**
 *
 * @author sj
 */
public interface PopulatableCache {
	
	LibraryUrls populate(GenerationUnit unit, RttiGenerationResult rttiGenerationResult, UrlResolver urlResolver);
}
