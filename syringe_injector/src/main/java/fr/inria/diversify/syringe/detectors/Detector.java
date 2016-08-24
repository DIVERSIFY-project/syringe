package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.IdMap;
import fr.inria.diversify.syringe.events.DetectionEvent;
import fr.inria.diversify.syringe.events.DetectionListener;
import fr.inria.diversify.syringe.signature.SignatureGenerator;
import spoon.processing.Processor;
import spoon.reflect.declaration.CtElement;

/**
 * Created by marodrig on 31/10/2015.
 */
public interface Detector<E extends CtElement> extends Processor<E> {

    /**
     * Number of elements detected so far
     * @return The number of elements detected
     */
    int getElementsDetectedCount();

    /**
     * Elements detected so far
     * @return A list with the elements detected
     */
    //List<CtElement> getElementsDetected();

    /**
     * Add an injector to listen code in a given event
     * @param eventName
     * @param eventListener
     */
    void addListener(String eventName, DetectionListener eventListener);

    /**
     * Removes an injector
     * @param eventListener
     */
    void removeListener(DetectionListener eventListener);

    /**
     * Notify the injectors listening to a message of a detection
     * @param eventName Message being listening
     * @param data MetaData of the detection
     */
    void notify(String eventName, DetectionEvent data);

    /**
     * Number of listener listening to a particular event.
     * @param eventName
     * @return
     */
    int listenerCount(String eventName);

    /**
     * The Id map allows the detectors to provide detected elements with an ID
     * so they can uniquely identify them
     *
     * @param idMap
     */
    void setIdMap(IdMap idMap);

    /**
     * Sets the signature generator for this detector
     * @param signature
     */
    void setSignature(SignatureGenerator signature);

    /**
     * Gets the signature generator from this detector
     * @return
     */
    SignatureGenerator getSignature();

    /**
     * Resets the detector to its original state
     */
    void reset();

        /*
    @Deprecated
    void setInjectors(Collection<BaseInjector> injectors);

    @Deprecated
    Collection<BaseInjector> getInjectors();

    @Deprecated
    void collectInjectors(AbstractMap<String, Collection<BaseInjector>> injectors);

    @Deprecated
    void setIdMap(IdMap idMap);

    @Deprecated
    IdMap getIdMap();

    @Deprecated
    void setSignature(SignatureGenerator signature);

    @Deprecated
    SignatureGenerator getSignature();
    */
}
