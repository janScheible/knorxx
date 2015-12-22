package knorxx.framework.generator.export;

import knorxx.framework.generator.GenerationUnit;
import knorxx.framework.generator.application.ApplicationResult;
import knorxx.framework.generator.web.server.rtti.RttiGenerationResult;
import knorxx.framework.generator.web.server.rtti.UrlResolver;

/**
 *
 * @author sj
 */
public class ExportResult {
	
	private final ApplicationResult applicationResult;
	private final GenerationUnit generationUnit;
	private final RttiGenerationResult rttiGenerationResult;
	private final UrlResolver urlResolver;

	public ExportResult(ApplicationResult applicationResult, GenerationUnit generationUnit, 
			RttiGenerationResult rttiGenerationResult, UrlResolver urlResolver) {
		this.applicationResult = applicationResult;
		this.generationUnit = generationUnit;
		this.rttiGenerationResult = rttiGenerationResult;
		this.urlResolver = urlResolver;
	}

	public ApplicationResult getApplicationResult() {
		return applicationResult;
	}

	public GenerationUnit getGenerationUnit() {
		return generationUnit;
	}

	public RttiGenerationResult getRttiGenerationResult() {
		return rttiGenerationResult;
	}

	public UrlResolver getUrlResolver() {
		return urlResolver;
	}
}
