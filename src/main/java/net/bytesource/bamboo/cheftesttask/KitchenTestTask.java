package net.bytesource.bamboo.cheftesttask;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;
import com.atlassian.utils.process.ExternalProcess;

import java.util.Map;

public class KitchenTestTask implements TaskType {
    private final ProcessService processService;
    private final EnvironmentVariableAccessor environmentVariableAccessor;

    public KitchenTestTask(final ProcessService processService, EnvironmentVariableAccessor environmentVariableAccessor) {
        this.processService = processService;
        this.environmentVariableAccessor = environmentVariableAccessor;
    }

    // Bamboo seems to cutoff the miliseconds when reading the report
    // So if a test is faster than 1 second, Bamboo ignores it
    @Override
    public TaskResult execute(final TaskContext taskContext) throws TaskException {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException localInterruptedException) {}
        final BuildLogger buildLogger = taskContext.getBuildLogger();
        final String environmentVariables = taskContext.getConfigurationMap().get("textfieldEnvironmentVariables");
        Map<String, String> environmentVariablesMap = environmentVariableAccessor.splitEnvironmentAssignments(environmentVariables);

        buildLogger.addBuildLogEntry("Start Kitchen Task!");

        final TaskResultBuilder builder = TaskResultBuilder.create(taskContext);

        String kitchenCmd = "chef exec kitchen test " +
                "--log-level=" + taskContext.getConfigurationMap().get("selectLogLevel").toLowerCase() + " " +
                "--concurrency=" + taskContext.getConfigurationMap().get("selectConcurrency").toLowerCase() + " " +
                "--destroy=" + taskContext.getConfigurationMap().get("selectDestroy").toLowerCase();

        ExternalProcessBuilder processBuilder = new ExternalProcessBuilder()
                .commandFromString(kitchenCmd)
                .env(environmentVariablesMap)
                .workingDirectory(taskContext.getWorkingDirectory());

        ExternalProcess process = processService.createExternalProcess(taskContext, processBuilder);
        String filename = processBuilder.getWorkingDirectory().getAbsolutePath() + "/kitchen.chefresult";
        ChefTestResultWriter.writeProcessResults(process, filename);
        return builder.checkReturnCode(process, 0).build();
    }
}
