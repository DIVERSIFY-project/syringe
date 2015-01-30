package fr.inria.diversify.lightinstru.detectors;

import fr.inria.diversify.lightinstru.IdMap;
import fr.inria.diversify.lightinstru.injectors.Injector;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Detects an occurrence of a given element and calls the fr.inria.diversify.lightinstru.injectors registered to the detected element to
 * inject custom code
 *
 * Created by marodrig on 08/12/2014.
 */
public abstract class Detector<E extends CtElement> extends AbstractProcessor<E> {

    private Collection<Injector> injectors;

    private IdMap idMap;

    public void setInjectors(Collection<Injector> injectors) {
        this.injectors = injectors;
    }

    public Collection<Injector> getInjectors() {
        return injectors;
    }

    public abstract void collectInjectors(AbstractMap<String, Collection<Injector>> injectors);

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
     * Returns the signature from element e
     * @param e
     * @return
     */
    protected String getSignatureFromElement(CtElement e) {
        SourcePosition sp = e.getPosition();
        CompilationUnit cu = sp.getCompilationUnit();
        return "<"+e.getClass().getSimpleName()+">" + cu.getMainType().getQualifiedName() + ":" + sp.getLine();
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
    public int getElementsDetected() {
        return elementsDetected;
    }
}
