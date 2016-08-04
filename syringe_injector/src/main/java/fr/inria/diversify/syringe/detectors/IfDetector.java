package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.events.BlockEvent;
import fr.inria.diversify.syringe.events.DetectionEvent;
import fr.inria.diversify.syringe.events.StatementDetectionEvent;
import spoon.reflect.code.*;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.Arrays;
import java.util.Collection;

/**
 * Detect the begin of a method
 * <p/>
 * Created by marodrig on 08/12/2014.
 */
public class IfDetector extends AbstractDetector<CtIf> {

    public static String IF_DETECTED = "@If.Detected@";
    public static String IF_BODY = "@If.Body.Detected@";
    public static String ELSE_BODY = "@Else.Detected@";

    private void notifyBlock(CtStatement st, String eventName) {

        if (listenerCount(eventName) <= 0 || st == null) return;

        BlockEvent event = new BlockEvent();
        if (st != null) {
            if (st instanceof CtBlock) {
                CtBlock block = (CtBlock) st;
                event.setFirstStatement(block.getStatement(0));
                event.setLastStatement(block.getLastStatement());
            } else {
                event.setFirstStatement(st);
                event.setLastStatement(st);
            }
        }
        try {
            event.setReturnStatements(st.getElements(new TypeFilter<CtReturn>(CtReturn.class)));
        } catch (NullPointerException ex) {
            event.setReturnStatements(null);
        }
        event.setDetected(st);
        putSignatureIntoEvent(event, st);
        elementsDetected++;
        notify(eventName, event);
    }

    @Override
    public void process(CtIf element) {
        notifyStatementDetection(element, IF_DETECTED);
        notifyBlock(element.getThenStatement(), IF_BODY);
        notifyBlock(element.getElseStatement(), ELSE_BODY);
    }

    @Override
    public Collection<String> eventsSupported() {
        return Arrays.asList(IF_DETECTED, IF_BODY, ELSE_BODY);
    }
}
