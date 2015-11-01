package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.IdMap;
import fr.inria.diversify.syringe.injectors.BaseInjector;
import fr.inria.diversify.syringe.injectors.Injector;
import fr.inria.diversify.syringe.signature.SignatureGenerator;
import spoon.processing.Processor;
import spoon.reflect.declaration.CtElement;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;

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
     * Add an injector to inject code in a given event
     * @param eventName
     * @param injector
     */
    public void addInjector(String eventName, Injector injector);

    /**
     * Removes an injector
     * @param injector
     */
    public void removeInjector(Injector injector);

    /**
     * Notify the injectors listening to a message of a detection
     * @param eventName Message being listening
     * @param detection Element detected
     * @param data MetaData of the detection
     */
    public void notify(String eventName, CtElement detection, DetectionData data);

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
}
