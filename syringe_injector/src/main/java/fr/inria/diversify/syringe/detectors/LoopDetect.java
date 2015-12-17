package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.events.BlockEvent;
import fr.inria.diversify.syringe.events.DetectionEvent;
import fr.inria.diversify.syringe.events.StatementDetectionEvent;
import org.apache.log4j.Logger;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtLoop;
import spoon.reflect.code.CtReturn;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourceCodeFragment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Detect the begin of a method
 * <p/>
 * Created by marodrig on 08/12/2014.
 */
public class LoopDetect extends AbstractDetector<CtLoop> {

    final static Logger logger = Logger.getLogger(LoopDetect.class);

    //Injector to listen just before the first statement of the loop
    public static String LOOP_BLOCK = "@Loop.BLOCK@";

    //Injectos to listen just before the loop
    public static String LOOP_DETECTED = "@Loop.DETECTED@";

    @Override
    public void process(CtLoop element) {
        try {
            int statementListeners = notifyStatementDetection(element, LOOP_DETECTED);
            if (listenerCount(LOOP_BLOCK) > 0) {
                BlockEvent event = new BlockEvent();
                if (element.getBody() instanceof CtBlock) {
                    event.setFirstStatement(((CtBlock) element.getBody()).getStatement(0));
                    event.setLastStatement(((CtBlock) element.getBody()).getLastStatement());
                    event.setBlock((CtBlock) element.getBody());
                } else {
                    event.setFirstStatement(element.getBody());
                    event.setLastStatement(element.getBody());
                }
                event.setReturnStatements(element.getElements(new TypeFilter<CtReturn>(CtReturn.class)));
                event.setDetected(element);
                putSignatureIntoEvent(event, element);
                notify(LOOP_BLOCK, event);
                if ( statementListeners == 0) elementsDetected++;
            }
        } catch (Exception e) {
            logger.warn(e);
            throw e;
        }
    }


    @Override
    public Collection<String> eventsSupported() {
        return Arrays.asList(LOOP_BLOCK, LOOP_DETECTED);
    }
}
