package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.injectors.GenericInjector;
import org.junit.Test;

import static fr.inria.diversify.syringe.detectors.IfDetector.ELSE_BODY;
import static fr.inria.diversify.syringe.detectors.IfDetector.IF_BODY;
import static fr.inria.diversify.syringe.detectors.IfDetector.IF_DETECTED;
import static fr.inria.diversify.syringe.detectors.LoopDetect.LOOP_BLOCK;
import static fr.inria.diversify.syringe.detectors.LoopDetect.LOOP_DETECTED;
import static fr.inria.diversify.syringe.detectors.MethodDetect.METHOD_BEGIN;
import static fr.inria.diversify.syringe.detectors.MethodDetect.METHOD_END;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by elmarce on 13/08/16.
 */
public class ListenerSupportTest {

    private void testListeners(Detector d, String... listeners) {
        for (String s : listeners)
            d.addListener(s, new GenericInjector());
        for (String s : listeners)
            assertEquals(1, d.listenerCount(s));

    }

    @Test
    public void loopDetect_ListenersSupport() {
        testListeners(new LoopDetect(), LOOP_DETECTED, LOOP_BLOCK);
    }

    @Test
    public void testDetect_ListenersSupport() {
        testListeners(new TestJunit4Detect(), METHOD_BEGIN, METHOD_END);
    }

    @Test
    public void methodDetect_ListenersSupport() {
        testListeners(new MethodDetect(), METHOD_BEGIN, METHOD_END);
    }

    @Test
    public void ifDetect_ListenersSupport() {
        testListeners(new IfDetector(), IF_BODY, ELSE_BODY, IF_DETECTED);
    }
}
