package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.events.DetectionEvent;
import fr.inria.diversify.syringe.events.DetectionListener;
import org.junit.Before;
import org.junit.Test;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtStatement;

import static org.junit.Assert.*;

/**
 * Created by marodrig on 15/12/2015.
 */
public class IfDetectorTest extends DetectorTestCase implements DetectionListener {

    int eventLaunched = 0;

    @Before
    public void setup() {
        eventLaunched = 0;
    }

    public class IfBodyListener implements DetectionListener {

        @Override
        public void listen(DetectionEvent data) {
            eventLaunched++;
            defaultBlockAssertions(data, CtStatement.class);
        }
    }

    @Test
    public void testProcessIf() throws Exception {
        IfDetector d = new IfDetector();
        d.addListener(IfDetector.IF_DETECTED, this);
        String folder = this.getClass().getResource("/control").toURI().getPath();
        process(d, folder);
        assertEquals(eventLaunched, d.getElementsDetectedCount());
        assertEquals(23, eventLaunched);
    }

    @Test
    public void testProcessIfBody() throws Exception {
        IfDetector d = new IfDetector();
        d.addListener(IfDetector.IF_BODY, new IfBodyListener());
        String folder = this.getClass().getResource("/control").toURI().getPath();
        process(d, folder);
        assertEquals(eventLaunched, d.getElementsDetectedCount());
        assertEquals(23, eventLaunched);
    }

    @Override
    public void listen(DetectionEvent data) {
        eventLaunched++;
        assertNotEquals(null, data.getDetected());
        assertTrue(data.getDetected() instanceof CtIf);
        assertTrue(data.getSignature().contains("ControlFlowArithmetic"));
    }
}