package fr.inria.diversify.syringe.injectors;

import fr.inria.diversify.syringe.IdMap;
import fr.inria.diversify.syringe.events.DetectionEvent;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;

import java.util.HashMap;

/**
 * Created by marodrig on 15/12/2015.
 */
public class GenericInjector implements Injector {

    /**
     * The shared among all injectors ID map to uniquely identify all detected elements
     */
    IdMap idMap = null;

    /**
     * The string that is going to be injected;
     */
    private String injection;

    /**
     * Tells the GenericInjector where to inject
     */
    public InjectionPosition injectAt;

    /**
     * This class is used to collect parameters from the detected element.
     */
    private ParameterCollector parameterCollector;

    public enum InjectionPosition {
        BEFORE, FIRST, LAST, AFTER
    }

    public InjectionPosition getInjectAt() {
        return injectAt;
    }

    public void setInjectAt(InjectionPosition injectAt) {
        this.injectAt = injectAt;
    }

    public ParameterCollector getParameterCollector() {
        return parameterCollector;
    }

    public void setParameterCollector(ParameterCollector parameterCollector) {
        this.parameterCollector = parameterCollector;
    }

    public IdMap getIdMap() {
        return idMap;
    }

    public void setIdMap(IdMap idMap) {
        this.idMap = idMap;
    }

    @Override
    public String getInjectionTemplate() {
        return injection;
    }

    @Override
    public void setInjectionTemplate(String s) {
        injection = s;
    }

    @Override
    public void listen(DetectionEvent data) {
        CtElement element = data.getDetected();
        if ( element instanceof CtStatement) {
            CtStatement st = (CtStatement)element;
            HashMap<String, Object> params;
            if (parameterCollector != null)
                params = parameterCollector.collectParameters(element);
            else params = new HashMap<>();

            CtParametrizedSnippetStatement pst = new CtParametrizedSnippetStatement();
            pst.setValue(injection);
            pst.setParameters(params);

            if ( injectAt == InjectionPosition.BEFORE ) {
                st.insertBefore(pst);
            } else if ( injectAt == InjectionPosition.AFTER ) {
                st.insertAfter(st);
            }
        } else throw new RuntimeException("Only statements allowed in injection");
    }
}
