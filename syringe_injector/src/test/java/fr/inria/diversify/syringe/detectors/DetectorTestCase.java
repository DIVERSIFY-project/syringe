package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.SpoonMetaFactory;
import fr.inria.diversify.syringe.events.BlockEvent;
import fr.inria.diversify.syringe.events.DetectionEvent;
import spoon.processing.AbstractProcessor;
import spoon.processing.ProcessingManager;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

/**
 * Created by marodrig on 15/12/2015.
 */
public class DetectorTestCase {
    public void process(Detector e, String folder) throws Exception {
        Factory factory = new SpoonMetaFactory().buildNewFactory(folder, 5);
        ProcessingManager pm = new QueueProcessingManager(factory);
        pm.addProcessor(e);
        pm.process();
    }

    public void defaultBlockAssertions(DetectionEvent data, Class klass) {
        assertTrue(data instanceof BlockEvent);
        BlockEvent blockEvent = (BlockEvent) data;
        assertNull(blockEvent.getBlock());
        if (blockEvent.getFirstStatement() == null) assertNull(blockEvent.getLastStatement());
        else assertNotNull(blockEvent.getLastStatement());
        assertNotEquals(null, data.getDetected());
        assertTrue(klass.isAssignableFrom(data.getDetected().getClass()));
        assertTrue(data.getSignature().contains("ControlFlowArithmetic"));
    }

}
