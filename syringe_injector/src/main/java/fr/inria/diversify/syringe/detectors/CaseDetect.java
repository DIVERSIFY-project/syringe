package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.events.BlockEvent;
import spoon.reflect.code.CtCase;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.*;

/**
 * Detect the begin of a method
 * <p>
 * Created by marodrig on 08/12/2014.
 */
public class CaseDetect extends AbstractDetector<CtCase> {

    public static String CASE_DETECTED = "@Case.Detected@";

    @Override
    public void process(CtCase element) {
        BlockEvent event = new BlockEvent();
        if ( element.getStatements().size() > 0 ) {
            event.setFirstStatement(element.getStatements().get(0));
            event.setLastStatement(element.getStatements().get(element.getStatements().size() - 1));
        }
        event.setReturnStatements(element.getElements(new TypeFilter<>(CtReturn.class)));
        event.setDetected(element);
        putSignatureIntoEvent(event, element);
        elementsDetected++;
        notify(CASE_DETECTED, event);
    }

    @Override
    public Collection<String> eventsSupported() {
        return Arrays.asList(CASE_DETECTED);
    }


}
