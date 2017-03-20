package net.bytesource.bamboo.cheftesttask;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.utils.process.ExternalProcess;

public class ChefTaskBuilderHelper {
    private ChefTaskBuilderHelper(){

    }
    public static TaskResult execute(ProcessService processService, TaskContext taskContext, String task, String command, String file) {
        final BuildLogger buildLogger = taskContext.getBuildLogger();
        buildLogger.addBuildLogEntry("Start " + task + " Task!");

        final TaskResultBuilder builder = TaskResultBuilder.create(taskContext);

        ExternalProcessBuilder processBuilder = new ExternalProcessBuilder()
                .commandFromString(command)
                .workingDirectory(taskContext.getWorkingDirectory());

        ExternalProcess process = processService.createExternalProcess(taskContext, processBuilder);
        String filename = processBuilder.getWorkingDirectory().getAbsolutePath() + file;
        ChefTestResultWriter.writeProcessResults(process, filename);
        return builder.checkReturnCode(process, 0).build();
    }
}
