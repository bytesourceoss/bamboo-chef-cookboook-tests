package net.bytesource.bamboo.cheftesttask;

import com.atlassian.bamboo.build.test.TestCollationService;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.util.BambooFileUtils;
import org.jetbrains.annotations.NotNull;


public class ChefTestParserTask implements TaskType {

    private final TestCollationService testCollationService;

    public ChefTestParserTask(TestCollationService testCollationService) {
        this.testCollationService = testCollationService;
    }


    @NotNull
    @Override
    public TaskResult execute(TaskContext taskContext) throws TaskException {
        TaskResultBuilder taskResultBuilder = TaskResultBuilder.create(taskContext);
        final String testFilePath = calculateEffectiveFilePattern(taskContext, "*.chefresult");
        testCollationService.collateTestResults(taskContext, testFilePath, new ChefTestReportCollector());

        return taskResultBuilder.checkTestFailures().build();
    }

    public static String calculateEffectiveFilePattern(@NotNull TaskContext taskContext, @NotNull String filePattern) {
        final String workingDirectoryPath = BambooFileUtils.calculateRelativePath(
                taskContext.getRootDirectory(), taskContext.getWorkingDirectory());

        return workingDirectoryPath + filePattern;
    }

}
