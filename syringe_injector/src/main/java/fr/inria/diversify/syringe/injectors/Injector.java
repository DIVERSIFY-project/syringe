package fr.inria.diversify.syringe.injectors;

import fr.inria.diversify.syringe.detectors.DetectionData;
import spoon.reflect.declaration.CtElement;

/**
 * Interface to all injectors. An injector is a class that injects code at specific points
 *
 * Created by marodrig on 31/10/2015.
 */
public interface Injector {

    String getInjectionString();

    void setInjectionString(String s);

    void inject(CtElement element, DetectionData data);
}
