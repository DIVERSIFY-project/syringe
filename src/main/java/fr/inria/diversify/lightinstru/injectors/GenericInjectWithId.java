package fr.inria.diversify.lightinstru.injectors;

import fr.inria.diversify.lightinstru.detectors.DetectionData;
import spoon.reflect.declaration.CtElement;

/**
 * Created by marodrig on 12/12/2014.
 */
public class GenericInjectWithId extends Injector {

    private String methodCall;

    /**
     * Constructor inserting the method call string
     * @param methodCall
     */
    public GenericInjectWithId(String methodCall) {
        this.methodCall = methodCall;
    }

    @Override
    public String injection(CtElement element, DetectionData data) {
        String sm = data.isEndWithSemiColon() ? ";" : "";
        return "\t" + getMethodCall() + "("+data.getElementId()+")" + sm ;
    }

    public String getMethodCall() {
        return methodCall;
    }

    /**
     * Canonical signature of the method call (Including Class name) without "()"
     * @param methodCall
     */
    public void setMethodCall(String methodCall) {
        this.methodCall = methodCall;
    }
}
