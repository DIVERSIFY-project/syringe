package fr.inria.diversify.syringe.injectors;

import fr.inria.diversify.syringe.detectors.DetectionData;
import spoon.reflect.declaration.CtElement;

import java.util.Map;

/**
 * Created by marodrig on 12/12/2014.
 */
public class GenericInjectWithId extends Injector {

    private String methodParams;

    private String methodCall;

    /**
     * Constructor inserting the method call string
     * @param methodCall
     */
    public GenericInjectWithId(String methodCall) {
        this(methodCall, "");
    }

    /**
     * Constructor inserting the method call string
     * @param methodCall
     */
    public GenericInjectWithId(String methodCall, String methodParams) {
        this.methodCall = methodCall;
        this.methodParams = methodParams;
    }

    @Override
    public String injection(CtElement element, DetectionData data) {
        //Replace all datas in the string
        String injection = methodParams;
        for (Map.Entry<String, String> e : data.getParams().entrySet() ) {
            injection = injection.replaceAll("%" + e.getKey()+"%", e.getValue());
        }
        String sm = data.isEndWithSemiColon() ? ";" : "";
        if ( !injection.isEmpty() )
            return "\t" + getMethodCall() + "("+data.getElementId()+","+injection+")" + sm ;
        else return "\t" + getMethodCall() + "("+data.getElementId()+")" + sm ;
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
