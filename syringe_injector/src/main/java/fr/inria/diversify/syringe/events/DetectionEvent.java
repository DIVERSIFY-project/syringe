package fr.inria.diversify.syringe.events;

import spoon.reflect.declaration.CtElement;

/**
 * Created by marodrig on 14/12/2015.
 */
public interface DetectionEvent {

    /**
     * Signature of the detected element.
     *
     * @return
     */
    String getSignature();

    void setSignature(String sig);

    CtElement getDetected();





}
