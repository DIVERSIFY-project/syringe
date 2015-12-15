package fr.inria.diversify.syringe.detectors;

import com.sun.javaws.exceptions.InvalidArgumentException;
import fr.inria.diversify.syringe.IdMap;
import fr.inria.diversify.syringe.events.DetectionEvent;
import fr.inria.diversify.syringe.events.DetectionListener;
import fr.inria.diversify.syringe.signature.DefaultSignature;
import fr.inria.diversify.syringe.signature.SignatureGenerator;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by marodrig on 01/11/2015.
 */
public abstract class AbstractDetector<E extends CtElement> extends AbstractProcessor<E>
        implements Detector<E> {

    /**
     * Number of elements detected
     */
    protected int elementsDetected = 0;

    /**
     * Injectors listening to an event. Ordered by event.
     */
    private HashMap<String, Collection<DetectionListener>> injectors;

    /**
     * In order to uniquely identify an element is necessary to provide a signature. Default spoon signatures
     * are not enough in this case because two semantically equal elements in different positions
     * would have the same signature. Our signature must also include the position of the element.
     */
    private IdMap idMap;

    /**
     * In order to save space in the log files is often useful to store an id instead of the full signature.
     *
     * This is a map to recover the full signature given the id
     *
     * @param idMap
     */
    public void setIdMap(IdMap idMap) {
        this.idMap = idMap;
    }

    /**
     * In order to save space in the log files is often useful to store an id instead of the full signature
     */
    public IdMap getIdMap() {
        return idMap;
    }

    /**
     * Indicates which events are supported by this detector
     * @return A collection of events supported by this detector
     */
    public abstract Collection<String> eventsSupported();

    /**
     * Signature generator for the elements detected using this detector
     */
    SignatureGenerator signature = new DefaultSignature();

    /**
     * Takes an event an puts the element's signature on ot
     * @param event Event to put the element's signature on
     * @param element Element to put the signature on.
     * @return The same detection event passed as parameter
     */
    protected DetectionEvent putSignatureIntoEvent(DetectionEvent event, CtElement element) {
        String sig = getSignature().getSignature(element);
        event.setSignature(sig);
        return event;
    }


    /**
     * Add an injector to listen code in a given event
     *
     * @param eventName
     * @param eventListener
     * @throws InvalidArgumentException when the eventName is not supported by the detector
     */
    public void addListener(String eventName, DetectionListener eventListener) throws InvalidArgumentException {
        if ( eventsSupported().contains(eventName) ) {
            if (injectors == null) injectors = new HashMap<>();
            if (!injectors.containsKey(eventName)) injectors.put(eventName, new ArrayList<DetectionListener>());
            injectors.get(eventName).add(eventListener);
        } else {
            throw new InvalidArgumentException(new String[] {"Event not supported"});
        }
    }

    /**
     * Removes an injector
     *
     * @param eventListener
     */
    public void removeListener(DetectionListener eventListener) {
        if (injectors == null) return;
        for (Collection<DetectionListener> c : injectors.values()) {
            if (c.contains(eventListener)) c.remove(eventListener);
        }
    }

    @Override
    public int listenerCount(String eventName) {
        return injectors.containsKey(eventName) ? injectors.get(eventName).size() : 0;
    }

    @Override
    public void notify(String eventName, DetectionEvent event) {
        if (injectors == null) return;
        for (DetectionListener eventListener : injectors.get(eventName)) {
            eventListener.listen(event);
        }
    }



    public int getElementsDetected() {
        return elementsDetected;
    }

    /**
     * Sets the signature generator for this detector
     * @param signature
     */
    @Override
    public void setSignature(SignatureGenerator signature) {
        this.signature = signature;
    }

    /**
     * Gets the signature generator from this detector
     * @return
     */
    @Override
    public SignatureGenerator getSignature() {
        return signature;
    }

    /**
     * Number of elements detected
     * @return
     */
    @Override
    public int getElementsDetectedCount() {
        return elementsDetected;
    }
}
