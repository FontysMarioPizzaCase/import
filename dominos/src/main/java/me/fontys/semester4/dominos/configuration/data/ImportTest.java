package me.fontys.semester4.dominos.configuration.data;

import me.fontys.semester4.dominos.configuration.data.order.test.OrderImportRecordCountTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class ImportTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportTest.class);

    public static void test(String name, Class<?> ...classes) {

        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        LauncherDiscoveryRequestBuilder requestBuilder = LauncherDiscoveryRequestBuilder.request();
        for (Class<?> clazz : classes) {
            requestBuilder.selectors(selectClass(clazz));
        }
        LauncherDiscoveryRequest request = requestBuilder.build();

        Launcher launcher = LauncherFactory.create();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();

        if (!summary.getFailures().isEmpty()) {
            LOGGER.warn("There were {} import test failures!", name);

            Map<String, List<TestExecutionSummary.Failure>> errors = new HashMap<>();

            for (TestExecutionSummary.Failure failure : summary.getFailures()) {
                try {
                    String parentId = failure.getTestIdentifier().getParentId().get();
                    // This is really dirty
                    String parentClass = parentId.split("class:")[1].replace("]", "");
                    Class<?> parent = Class.forName(parentClass);
                    String parentName = getTestContainerNameFromClass(parent);
                    if (!errors.containsKey(parentName)) {
                        List<TestExecutionSummary.Failure> newClassErrors = new ArrayList<>();
                        newClassErrors.add(failure);
                        errors.put(parentName, newClassErrors);
                    } else {
                        errors.get(parentName).add(failure);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            for (Map.Entry<String, List<TestExecutionSummary.Failure>> reportedErrors : errors.entrySet()) {
                LOGGER.error("");
                LOGGER.error("  " + reportedErrors.getKey() + ":");
                for (TestExecutionSummary.Failure failure : reportedErrors.getValue()) {
                    LOGGER.error("");
                    LOGGER.error("    " + failure.getTestIdentifier().getDisplayName());
                    LOGGER.error("      " + failure.getException().getMessage());
                }
            }
            LOGGER.error("");
        }
    }

    private static String getTestContainerNameFromClass(Class<?> clazz) {
        DisplayName displayName = clazz.getDeclaredAnnotation(DisplayName.class);
        if (displayName == null) {
            return clazz.getSimpleName();
        }

        return displayName.value();
    }
}
