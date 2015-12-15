package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.events.DetectionEvent;
import fr.inria.diversify.syringe.events.StatementDetectionEvent;
import spoon.reflect.code.CtInvocation;

import java.util.Arrays;
import java.util.Collection;

/**
 * A detector to detect JUnit assert methods and Java built-in assertions
 *
 * Created by marodrig on 22/12/2014.
 */
public class AssertDetector extends AbstractDetector<CtInvocation<?>> {

    public static String ASSERT_DETECTED = "@ASSERT_DETECTED@";

    @Override
    public boolean isToBeProcessed(CtInvocation<?> candidate) {
        try {
            return candidate.getExecutable().getSimpleName().startsWith("assert");
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void process(CtInvocation<?> invocation) {
        DetectionEvent event = putSignatureIntoEvent(new StatementDetectionEvent(invocation), invocation);
        notify(ASSERT_DETECTED, event);
        elementsDetected++;
    }

    @Override
    public Collection<String> eventsSupported() {
        return Arrays.asList(ASSERT_DETECTED);
    }

    @Override
    public int getElementsDetectedCount() {
        return elementsDetected;
    }


}
