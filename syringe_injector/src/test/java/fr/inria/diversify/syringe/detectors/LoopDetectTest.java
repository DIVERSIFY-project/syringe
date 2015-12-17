package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.events.BlockEvent;
import fr.inria.diversify.syringe.events.DetectionEvent;
import fr.inria.diversify.syringe.events.DetectionListener;
import org.junit.Before;
import org.junit.Test;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLoop;
import spoon.reflect.code.CtStatement;

import static org.junit.Assert.*;

/**
 * Created by marodrig on 15/12/2015.
 */
public class LoopDetectTest extends DetectorTestCase {

    int eventLaunched = 0;

    @Before
    public void setup() {
        eventLaunched = 0;
    }

    @Test
    public void testDetectStatement() throws Exception {
        LoopDetect d = new LoopDetect();
        d.addListener(LoopDetect.LOOP_DETECTED, new StatementListener());
        String folder = this.getClass().getResource("/control").toURI().getPath();
        process(d, folder);
        assertEquals(eventLaunched, d.getElementsDetectedCount());
        assertEquals(19, eventLaunched);
    }

    @Test
    public void testDetectBlock() throws Exception {
        LoopDetect d = new LoopDetect();
        d.addListener(LoopDetect.LOOP_BLOCK, new BlockListener());
        String folder = this.getClass().getResource("/control").toURI().getPath();
        process(d, folder);
        assertEquals(eventLaunched, d.getElementsDetectedCount());
        assertEquals(19, eventLaunched);
    }

    class StatementListener implements DetectionListener {
        @Override
        public void listen(DetectionEvent data) {
            eventLaunched++;
            assertNotEquals(null, data.getDetected());
            assertTrue(data.getDetected() instanceof CtLoop);
            assertTrue(data.getSignature().contains("ControlFlowArithmetic"));
        }
    }

    class BlockListener implements DetectionListener {
        @Override
        public void listen(DetectionEvent data) {
            eventLaunched++;
            assertNotEquals(null, data.getDetected());
            assertTrue(data instanceof BlockEvent);
            BlockEvent e = (BlockEvent) data;
            if (e.getBlock() == null)
                assertEquals(e.getFirstStatement(), e.getLastStatement());
            else {
                assertNotNull(e.getFirstStatement());
                if (e.getBlock().getStatements().size() == 1)
                    assertEquals(e.getFirstStatement(), e.getLastStatement());
                else assertNotEquals(e.getFirstStatement(), e.getLastStatement());
            }
            assertTrue(data.getDetected() instanceof CtLoop);
            assertTrue(data.getSignature().contains("ControlFlowArithmetic"));
        }
    }
}