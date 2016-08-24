package fr.inria.diversify.syringe.detectors;

import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.reference.CtTypeReference;

/**
 * Detect the begin of a method
 *
 * Created by marodrig on 08/12/2014.
 */
public class TestJunit4Detect extends MethodDetect {

    @Override
    public boolean isToBeProcessed(CtExecutable candidate) {
        /*
        if(!(isTestClass(candidate.getDeclaringType().getReference())))
            return false;*/

        if (candidate.isImplicit()
                || candidate.getBody() == null
                || candidate.getBody().getStatements().size() == 0)
            return false;

        for (CtAnnotation<?> annotation : candidate.getAnnotations())
            if (annotation.toString().startsWith("@org.junit.Test") ||
                    annotation.toString().startsWith("@org.junit.Before") ||
                    annotation.toString().startsWith("@org.junit.After"))
                return true;

        return false;
    }
    /*
    protected boolean isTestClass(CtTypeReference<?> type) {
        if(type.getSimpleName().endsWith("Test") || type.getSimpleName().endsWith("Behaviour")) // Behaviour for jbehave
            return true;
        else {
            try {
                if (type.getSuperclass() != null) return isTestClass(type.getSuperclass());
            } catch (Exception e) {}
        }
        return false;
    }*/
}
