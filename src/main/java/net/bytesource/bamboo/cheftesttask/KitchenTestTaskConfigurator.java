package net.bytesource.bamboo.cheftesttask;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class KitchenTestTaskConfigurator extends AbstractTaskConfigurator {
    public static final String TEXTFIELD_ENVIRONMENT_VARIABLES = "textfieldEnvironmentVariables";
    public static final String CONCURRENCY_LIST = "ConcurrencyList";
    public static final String DESTROY_LIST = "DestroyList";
    public static final String LOG_LEVEL_LIST = "LogLevelList";
    public static final String SELECT_CONCURRENCY = "selectConcurrency";
    public static final String SELECT_DESTROY = "selectDestroy";
    public static final String SELECT_LOG_LEVEL = "selectLogLevel";

    public static final String DEFAULT_CONCURRENCY = "6";
    public static final String DEFAULT_DESTROY = "Always";
    public static final String DEFAULT_LOG_LEVEL = "Info";

    private static final List<String> ConcurrencyList = Arrays.asList("1", "2", "3", "4", "5", "6");
    private static final List<String> DestroyList = Arrays.asList("Passing", "Always", "Never");
    private static final List<String> LogLevelList = Arrays.asList("Debug", "Info", "Warn", "Error", "Fatal");

    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params, @Nullable final TaskDefinition previousTaskDefinition) {
        final Map<String, String> config = super.generateTaskConfigMap(params, previousTaskDefinition);
        config.put(TEXTFIELD_ENVIRONMENT_VARIABLES, params.getString(TEXTFIELD_ENVIRONMENT_VARIABLES));
        config.put(SELECT_CONCURRENCY, arrayToString(params.getStringArray(SELECT_CONCURRENCY)));
        config.put(SELECT_DESTROY, arrayToString(params.getStringArray(SELECT_DESTROY)));
        config.put(SELECT_LOG_LEVEL, arrayToString(params.getStringArray(SELECT_LOG_LEVEL)));

        return config;
    }

    @Override
    public void populateContextForCreate(@NotNull final Map<String, Object> context) {
        super.populateContextForCreate(context);

        context.put(TEXTFIELD_ENVIRONMENT_VARIABLES, "");
        context.put(CONCURRENCY_LIST, ConcurrencyList);
        context.put(DESTROY_LIST, DestroyList);
        context.put(LOG_LEVEL_LIST, LogLevelList);

        context.put(SELECT_CONCURRENCY, DEFAULT_CONCURRENCY);
        context.put(SELECT_DESTROY, DEFAULT_DESTROY);
        context.put(SELECT_LOG_LEVEL, DEFAULT_LOG_LEVEL);
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition) {
        super.populateContextForEdit(context, taskDefinition);

        context.put(TEXTFIELD_ENVIRONMENT_VARIABLES, taskDefinition.getConfiguration().get(TEXTFIELD_ENVIRONMENT_VARIABLES));
        context.put(CONCURRENCY_LIST, ConcurrencyList);
        context.put(DESTROY_LIST, DestroyList);
        context.put(LOG_LEVEL_LIST, LogLevelList);

        context.put(SELECT_CONCURRENCY, taskDefinition.getConfiguration().get(SELECT_CONCURRENCY));
        context.put(SELECT_DESTROY, taskDefinition.getConfiguration().get(SELECT_DESTROY));
        context.put(SELECT_LOG_LEVEL, taskDefinition.getConfiguration().get(SELECT_LOG_LEVEL));

    }

    @Override
    public void populateContextForView(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition) {
        super.populateContextForView(context, taskDefinition);

        context.put(TEXTFIELD_ENVIRONMENT_VARIABLES, taskDefinition.getConfiguration().get(TEXTFIELD_ENVIRONMENT_VARIABLES));
        context.put(SELECT_CONCURRENCY, taskDefinition.getConfiguration().get(SELECT_CONCURRENCY));
        context.put(SELECT_DESTROY, taskDefinition.getConfiguration().get(SELECT_DESTROY));
        context.put(SELECT_LOG_LEVEL, taskDefinition.getConfiguration().get(SELECT_LOG_LEVEL));

    }

    @NotNull
    private String arrayToString(@Nullable String[] input) {
        return Joiner.on(",").join(Objects.firstNonNull(input, new String[]{}));
    }
}
