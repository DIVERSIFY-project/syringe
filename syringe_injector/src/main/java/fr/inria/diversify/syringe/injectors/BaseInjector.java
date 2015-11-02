package fr.inria.diversify.syringe.injectors;

import spoon.reflect.declaration.CtElement;
import fr.inria.diversify.syringe.detectors.DetectionData;

/**
 * Created by marodrig on 08/12/2014.
 */
public abstract class BaseInjector extends AbstractInjector {

    @Deprecated
    public abstract String injection(CtElement element, DetectionData data);

}
