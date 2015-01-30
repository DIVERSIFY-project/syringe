package fr.inria.diversify.lightinstru.injectors;

import fr.inria.diversify.lightinstru.detectors.DetectionData;
import spoon.reflect.declaration.CtElement;

/**
 * Created by marodrig on 10/12/2014.
 */
public class GenericInjector extends Injector {

    /**
     * Creates a generic injector
     * @param s String to inject
     */
    public GenericInjector(String s) {
        setInjectionString(s);
    }

    @Override
    public String injection(CtElement element, DetectionData data) {
        return getInjectionString();
    }
}
