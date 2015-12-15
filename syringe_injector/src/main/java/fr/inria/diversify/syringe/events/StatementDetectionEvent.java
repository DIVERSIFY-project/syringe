package fr.inria.diversify.syringe.events;

import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;

/**
 *
 * Class to notify that a particular statement is detected
 *
 * Created by marodrig on 15/12/2015.
 */
public class StatementDetectionEvent extends AbstractEvent{

    public CtStatement getStatement() {
        return (CtStatement)getDetected();
    }

    public void setStatement(CtStatement statement) {
        setDetected(statement);
    }

    public StatementDetectionEvent(CtStatement st) {
        setDetected(st);
    }
}
