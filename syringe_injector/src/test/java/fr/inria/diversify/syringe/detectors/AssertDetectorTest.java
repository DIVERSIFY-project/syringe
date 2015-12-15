package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.events.DetectionEvent;
import fr.inria.diversify.syringe.events.DetectionListener;
import org.junit.Before;
import org.junit.Test;
import spoon.reflect.code.CtInvocation;

import static org.junit.Assert.*;

/**
 * Created by marodrig on 15/12/2015.
 */
public class AssertDetectorTest extends DetectorTestCase implements DetectionListener {

    int eventLaunched = 0;

    @Before
    public void setup() {
        eventLaunched = 0;
    }

    @Test
    public void testProcess() throws Exception {
        AssertDetector d = new AssertDetector();
        d.addListener(AssertDetector.ASSERT_DETECTED, this);

        String folder = this.getClass().getResource("/control").toURI().getPath();
        process(d, folder);
        assertEquals(eventLaunched, d.getElementsDetectedCount());
        assertEquals(2, eventLaunched);

    }

    @Override
    public void listen(DetectionEvent data) {
        eventLaunched++;
        assertNotEquals(null, data.getDetected());
        assertTrue(data.getDetected() instanceof  CtInvocation);
        assertTrue(data.getSignature().contains("ControlFlowArithmetic"));
    }
}