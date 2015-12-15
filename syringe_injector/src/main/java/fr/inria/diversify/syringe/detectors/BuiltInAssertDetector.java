package fr.inria.diversify.syringe.detectors;

/**
 * A detector to detect JUnit assert methods and Java built-in assertions
 *
 * Created by marodrig on 22/12/2014.
 */

import fr.inria.diversify.syringe.events.DetectionEvent;
import fr.inria.diversify.syringe.events.StatementDetectionEvent;
import spoon.reflect.code.CtAssert;
import java.util.Arrays;
import java.util.Collection;

/**
 * A detector for built asserts
 */
public class BuiltInAssertDetector extends AbstractDetector<CtAssert<?>> {

    public static String ASSERT_DETECTED = "@Built.In.Assert.End@";

    @Override
    public boolean isToBeProcessed(CtAssert<?> candidate) {
        return true;
    }

    @Override
    public void process(CtAssert ctAssert) {
        DetectionEvent event = putSignatureIntoEvent(new StatementDetectionEvent(ctAssert), ctAssert);
        notify(ASSERT_DETECTED, event);
        elementsDetected++;
    }

    @Override
    public Collection<String> eventsSupported() {
        return Arrays.asList(ASSERT_DETECTED);
    }


}
