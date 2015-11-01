package fr.inria.diversify.syringe.injectors;

import spoon.reflect.declaration.CtElement;
import fr.inria.diversify.syringe.detectors.DetectionData;

/**
 * Created by marodrig on 08/12/2014.
 */
public abstract class BaseInjector implements Injector {

    //A generic injection string
    private String injectionString;

    @Override
    public String getInjectionString() {
        return injectionString;
    }

    @Override
    public void setInjectionString(String s) {
        injectionString = s;
    }

    @Deprecated
    public abstract String injection(CtElement element, DetectionData data);


}
