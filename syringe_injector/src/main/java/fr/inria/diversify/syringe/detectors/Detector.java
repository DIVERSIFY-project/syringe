package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.IdMap;
import fr.inria.diversify.syringe.injectors.Injector;
import fr.inria.diversify.syringe.signature.SignatureGenerator;
import spoon.processing.Processor;
import spoon.reflect.declaration.CtElement;

import java.util.AbstractMap;
import java.util.Collection;

/**
 * Created by marodrig on 31/10/2015.
 */
public interface Detector<E extends CtElement> extends Processor<E> {
    void setInjectors(Collection<Injector> injectors);

    Collection<Injector> getInjectors();

    void collectInjectors(AbstractMap<String, Collection<Injector>> injectors);

    void setIdMap(IdMap idMap);

    IdMap getIdMap();

    int getElementsDetected();

    void setSignature(SignatureGenerator signature);

    SignatureGenerator getSignature();
}
