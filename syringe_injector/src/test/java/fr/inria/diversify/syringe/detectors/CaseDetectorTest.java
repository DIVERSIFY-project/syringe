package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.events.DetectionEvent;
import fr.inria.diversify.syringe.events.DetectionListener;
import org.junit.Before;
import org.junit.Test;
import spoon.reflect.code.CtCase;

import static org.junit.Assert.*;

/**
 * Created by marodrig on 15/12/2015.
 */
public class CaseDetectorTest extends DetectorTestCase implements DetectionListener {

    int eventLaunched = 0;

    @Before
    public void setup() {
        eventLaunched = 0;
    }

    @Test
    public void testProcess() throws Exception {
        CaseDetect d = new CaseDetect();
        d.addListener(CaseDetect.CASE_DETECTED, this);
        String folder = this.getClass().getResource("/control").toURI().getPath();
        process(d, folder);
        assertEquals(eventLaunched, d.getElementsDetectedCount());
        assertEquals(5, eventLaunched);
    }

    @Override
    public void listen(DetectionEvent data) {
        defaultBlockAssertions(data, CtCase.class);
        eventLaunched++;
    }
}