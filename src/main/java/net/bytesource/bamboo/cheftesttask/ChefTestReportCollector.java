package net.bytesource.bamboo.cheftesttask;

import com.atlassian.bamboo.build.test.TestCollectionResult;
import com.atlassian.bamboo.build.test.TestCollectionResultBuilder;
import com.atlassian.bamboo.build.test.TestReportCollector;
import com.atlassian.bamboo.results.tests.TestResults;
import com.atlassian.bamboo.resultsummary.tests.TestState;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Set;


public class ChefTestReportCollector implements TestReportCollector {
    public static final int MILLISECONDS_IN_SECOND = 1000;

    @NotNull
    @Override
    public TestCollectionResult collect(File file) throws Exception {
        TestCollectionResultBuilder builder = new TestCollectionResultBuilder();

        Collection<TestResults> successfulTestResults = Lists.newArrayList();
        Collection<TestResults> failingTestResults = Lists.newArrayList();
        String returnCode = Files.readFirstLine(file, Charset.forName("UTF-8"));
        String filename = file.getName();
        String[] strings = returnCode.split(";");
        long duration = Long.parseLong(strings[1]);
        long newDuration = duration / MILLISECONDS_IN_SECOND;
        TestResults testResults = new TestResults(filename, filename, String.valueOf(newDuration));
        if ("0".equals(strings[0])) {
            testResults.setState(TestState.SUCCESS);
            successfulTestResults.add(testResults);
        } else {
            testResults.setState(TestState.FAILED);
            failingTestResults.add(testResults);
        }

        return builder
                .addSuccessfulTestResults(successfulTestResults)
                .addFailedTestResults(failingTestResults)
                .build();
    }

    @NotNull
    @Override
    public Set<String> getSupportedFileExtensions() {
        // this will collect all *.chefresult files
        return Sets.newHashSet("chefresult");
    }
}
