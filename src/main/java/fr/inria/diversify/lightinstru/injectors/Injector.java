package fr.inria.diversify.lightinstru.injectors;

import spoon.reflect.declaration.CtElement;
import fr.inria.diversify.lightinstru.detectors.DetectionData;

/**
 * Created by marodrig on 08/12/2014.
 */
public abstract class Injector {


    //A generic injection string
    private String injectionString;

    public String getInjectionString() {
        return injectionString;
    }

    public void setInjectionString(String s) {
        injectionString = s;
    }

    public abstract String injection(CtElement element, DetectionData data);

}
