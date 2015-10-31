package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.IdMap;
import fr.inria.diversify.syringe.injectors.Injector;
import fr.inria.diversify.syringe.signature.DefaultSignature;
import fr.inria.diversify.syringe.signature.SignatureGenerator;
import spoon.processing.AbstractProcessor;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;

import java.util.AbstractMap;
import java.util.Collection;

/**
 * Detects an occurrence of a given element and calls the fr.inria.diversify.syringe.syringe.injectors registered to the detected element to
 * inject custom code
 *
 * Created by marodrig on 08/12/2014.
 */
public abstract class BaseDetector<E extends CtElement> extends AbstractProcessor<E> implements Detector<E> {

    private Collection<Injector> injectors;

    private IdMap idMap;
    private SignatureGenerator signature;

    @Override
    public void setInjectors(Collection<Injector> injectors) {
        this.injectors = injectors;
    }

    @Override
    public Collection<Injector> getInjectors() {
        return injectors;
    }

    //Number of elements detected
    protected int elementsDetected = 0;

    protected DetectionData data;

    /**
     * Gets a injection string out of a list of injectors
     * @param injectors Injectors composing the string
     * @return A string composed of all injections
     */
    protected String getSnippet(Collection<Injector> injectors, CtElement e, DetectionData data) {
        StringBuilder sb = new StringBuilder();
        for ( Injector i : injectors ) {
            sb.append(i.injection(e, data));
        }
        return sb.toString();
    }

    /**
     * In order to save space in the log files is often useful to store an id instead of the full signature
     * @param idMap
     */
    @Override
    public void setIdMap(IdMap idMap) {
        this.idMap = idMap;
    }

    /**
     * In order to save space in the log files is often useful to store an id instead of the full signature
     */
    @Override
    public IdMap getIdMap() {
        return idMap;
    }


    /**
     * Returns the signature from element e
     * @param e
     * @return
     */
    protected String getSignatureFromElement(CtElement e) {
        return getSignature().getSignature(e);
    }

    protected void putSignatureIntoData(String sig) {
        //Set id of the element to the id map
        getIdMap().addToMap(sig);
        data.setElementId(getIdMap().get(sig));
        data.setIdMap(getIdMap());
    }

    /**
     * Number of elements detected
     * @return int: The number of elements detected by this detector
     */
    @Override
    public int getElementsDetected() {
        return elementsDetected;
    }

    @Override
    public void setSignature(SignatureGenerator signature) {
        this.signature = signature;
    }

    @Override
    public SignatureGenerator getSignature() {
        if ( signature == null ) signature = new DefaultSignature();
        return signature;
    }
}