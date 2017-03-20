package net.bytesource.bamboo.cheftesttask;

import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;


public class RubocopTestTask implements TaskType {
    private final ProcessService processService;

    public RubocopTestTask(final ProcessService processService) {
        this.processService = processService;
    }

    // Bamboo seems to cutoff the miliseconds when reading the report
    // So if a test is faster than 1 second, Bamboo ignores it
    @Override
    public TaskResult execute(final TaskContext taskContext) throws TaskException {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException localInterruptedException) {}
        return ChefTaskBuilderHelper.execute(processService,taskContext,"Rubocop","chef exec rubocop","/rubocop.chefresult");
    }
}

