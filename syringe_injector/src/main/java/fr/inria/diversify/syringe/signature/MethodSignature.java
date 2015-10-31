package fr.inria.diversify.syringe.signature;

import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;

/**
 * A signature returning only the class of the element
 *
 * Created by marodrig on 20/02/2015.
 */
public class MethodSignature implements SignatureGenerator {

    @Override
    public String getSignature(CtElement e) {
        if ( e instanceof CtExecutable ) return methodSignature((CtExecutable)e);
        e = e.getParent(CtMethod.class);
        return e == null ? new DefaultSignature().getSignature(e) : methodSignature((CtExecutable)e);
    }

    private String methodSignature(CtExecutable e) {
        return new ClassSignature().getSignature(e) + "." + e.getSimpleName() + ":" + e.getPosition().getLine();
    }
}
