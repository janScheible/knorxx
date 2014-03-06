package knorxx.framework.generator;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author sj
 */
public abstract class GenerationRoots {

    public abstract String getSourceRoot();

    public abstract String getOutputRoot();

    public static class Simple extends GenerationRoots {

        private final String sourceRoot;
        private final String outputRoot;

        public Simple(String sourceRoot, String outputRoot) {
            this.sourceRoot = sourceRoot;
            this.outputRoot = outputRoot;
        }
        
        public Simple(List<String> sourceParts, List<String> outputParts) {
            this.sourceRoot = Joiner.on(File.separator).join(sourceParts);
            this.outputRoot = Joiner.on(File.separator).join(outputParts);
        }
        
        public static GenerationRoots createMainJava() {
            return new Simple(Lists.newArrayList(getCurrentDirectory(), "src", "main", "java"),
                    Lists.newArrayList(getCurrentDirectory(), "target", "classes"));
        }
        
        private static String getCurrentDirectory() {
            try {
                String rootFolder = new File(".").getCanonicalPath();
                return rootFolder;
            } catch (IOException ex) {
                throw new IllegalStateException("Unable to get the current directory!", ex);
            }
        }
        
        public static GenerationRoots createTestJava() {
            return new Simple(Lists.newArrayList(getCurrentDirectory(), "src", "test", "java"),
                    Lists.newArrayList(getCurrentDirectory(), "target", "test-classes"));
        }

        @Override
        public String getSourceRoot() {
            return sourceRoot;
        }

        @Override
        public String getOutputRoot() {
            return outputRoot;
        }
    }

    public static class PropertyFile extends GenerationRoots {

        private final String sourceRoot;
        private final String outputRoot;

        public PropertyFile(InputStream inputStream) throws IOException {
            Properties properties = new Properties();

            try (InputStream input = inputStream) {
                properties.load(new StringReader(CharStreams.toString(new InputStreamReader(input, Charsets.UTF_8)).replace("\\", "\\\\")));
            }

            this.sourceRoot = properties.getProperty("sourceRoot");
            this.outputRoot = properties.getProperty("outputRoot");
        }

        @Override
        public String getSourceRoot() {
            return sourceRoot;
        }

        @Override
        public String getOutputRoot() {
            return outputRoot;
        }
    }
}
