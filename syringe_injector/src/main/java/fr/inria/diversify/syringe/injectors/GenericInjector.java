package fr.inria.diversify.syringe.injectors;

import fr.inria.diversify.syringe.detectors.DetectionData;
import spoon.reflect.declaration.CtElement;

import java.util.Map;

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
        //Replace all datas in the string
        String injection = getInjectionString();
        for (Map.Entry<String, String> e : data.getParams().entrySet() ) {
            injection = injection.replaceAll("%" + e.getKey()+"%", e.getValue());
        }
        return injection;
    }
}
