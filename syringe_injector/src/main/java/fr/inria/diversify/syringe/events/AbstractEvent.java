package fr.inria.diversify.syringe.events;

import spoon.reflect.code.CtCase;
import spoon.reflect.declaration.CtElement;

/**
 * Created by marodrig on 14/12/2015.
 */
public abstract class AbstractEvent implements DetectionEvent {

    protected String signature;

    private CtElement element;

    @Override
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setDetected(CtElement element) {
        this.element = element;
    }

    @Override
    public CtElement getDetected() {
        return element;
    }
}
