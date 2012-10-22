package org.whitesource.bamboo.agent;

import static org.mockito.Mockito.mock;

import java.io.File;
import java.util.Collection;

import junit.framework.TestCase;

import org.junit.Test;
import org.whitesource.agent.api.model.AgentProjectInfo;

import com.atlassian.bamboo.build.logger.BuildLogger;

public class GenericOssInfoExtractorTest extends TestCase
{
    private static BuildLogger buildLogger;
    private static File testDirectory;
    protected static final String PROJECT_NAME = "TestPojectName";
    protected static final String PROJECT_TOKEN = "TestProjectToken";
    protected static final String PATTERN_NONE = "";
    protected static final String PATTERN_ALL = "lib/*.jar";
    protected static final int NUM_ALL = 10; // REVIEW: maybe derive this number dynamically in setUp(), or maybe not?
    protected static final String PATTERN_WSS = "lib/wss*.jar";
    protected static final int NUM_WSS = 2;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        buildLogger = mock(BuildLogger.class);
        testDirectory = new File("target/classes/META-INF");
    }

    @Test
    public void testExtractOssInfoIsDirectory()
    {
        assertTrue(testDirectory.isDirectory());
    }

    @Test
    public void testExtractOssInfoDefault()
    {
        BaseOssInfoExtractor extractor = new GenericOssInfoExtractor(PROJECT_NAME, PROJECT_TOKEN, PATTERN_ALL,
                PATTERN_NONE, testDirectory, buildLogger);
        Collection<AgentProjectInfo> projectInfos = extractor.extract();
        assertEquals(1, projectInfos.size());
        assertEquals(NUM_ALL, projectInfos.iterator().next().getDependencies().size());
    }

    @Test
    public void testExtractOssInfoDefaultWithIncludes()
    {
        BaseOssInfoExtractor extractor = new GenericOssInfoExtractor(PROJECT_NAME, PROJECT_TOKEN, PATTERN_WSS,
                PATTERN_NONE, testDirectory, buildLogger);
        Collection<AgentProjectInfo> projectInfos = extractor.extract();
        assertEquals(1, projectInfos.size());
        assertEquals(NUM_WSS, projectInfos.iterator().next().getDependencies().size());
    }

    @Test
    public void testExtractOssInfoDefaultWithExcludes()
    {
        BaseOssInfoExtractor extractor = new GenericOssInfoExtractor(PROJECT_NAME, PROJECT_TOKEN, PATTERN_ALL,
                PATTERN_WSS, testDirectory, buildLogger);
        Collection<AgentProjectInfo> projectInfos = extractor.extract();
        assertEquals(1, projectInfos.size());
        assertEquals(NUM_ALL - NUM_WSS, projectInfos.iterator().next().getDependencies().size());
    }
}
