package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.IdMap;
import fr.inria.diversify.syringe.injectors.BaseInjector;
import fr.inria.diversify.syringe.injectors.Injector;
import fr.inria.diversify.syringe.signature.DefaultSignature;
import fr.inria.diversify.syringe.signature.SignatureGenerator;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtElement;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Detects an occurrence of a given element and calls the fr.inria.diversify.syringe.syringe.deprecatedInjectors registered to the detected element to
 * inject custom code
 * <p/>
 * Created by marodrig on 08/12/2014.
 */
@Deprecated
public abstract class BaseDetector<E extends CtElement> extends AbstractDetector<E> implements Detector<E> {

    @Deprecated
    private Collection<BaseInjector> deprecatedInjectors;




    private SignatureGenerator signature;

    @Deprecated
    public void setInjectors(Collection<BaseInjector> injectors) {
        this.deprecatedInjectors = injectors;
    }

    @Deprecated
    public Collection<BaseInjector> getInjectors() {
        return deprecatedInjectors;
    }

    //Number of elements detected
    protected int elementsDetected = 0;

    protected DetectionData data;


    /**
     * Gets a injection string out of a list of deprecatedInjectors
     *
     * @param injectors Injectors composing the string
     * @return A string composed of all injections
     */
    protected String getSnippet(Collection<BaseInjector> injectors, CtElement e, DetectionData data) {
        StringBuilder sb = new StringBuilder();
        for (BaseInjector i : injectors) {
            sb.append(i.injection(e, data));
        }
        return sb.toString();
    }



    /**
     * Returns the signature from element e
     *
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
     *
     * @return int: The number of elements detected by this detector
     */
    @Override
    public int getElementsDetectedCount() {
        return elementsDetected;
    }

    @Deprecated
    public void setSignature(SignatureGenerator signature) {
        this.signature = signature;
    }

    @Deprecated
    public SignatureGenerator getSignature() {
        if (signature == null) signature = new DefaultSignature();
        return signature;
    }

    @Deprecated
    public abstract void collectInjectors(AbstractMap<String, Collection<BaseInjector>> injectors);

}
