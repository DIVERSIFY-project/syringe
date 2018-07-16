package fr.inria.diversify.syringe.detectors;

import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.reference.CtTypeReference;

/**
 * Detect the begin of a method
 * <p>
 * Created by marodrig on 08/12/2014.
 */
public class TestJunit4Detect extends MethodDetect {

    @Override
    public boolean isToBeProcessed(CtExecutable candidate) {
        /*
        if(!(isTestClass(candidate.getDeclaringType().getReference())))
            return false;*/
        try {

            if (candidate.isImplicit()
                    || candidate.getBody() == null
                    || candidate.getBody().getStatements().size() == 0)
                return false;

            for (CtAnnotation<?> annotation : candidate.getAnnotations()) {
                String a = annotation.toString();
                if (a.startsWith("@Test") ||
                        a.startsWith("@Before") ||
                        a.startsWith("@After") ||
                        a.startsWith("@org.junit.Test") ||
                        a.startsWith("@org.junit.Before") ||
                        a.startsWith("@org.junit.After"))
                    return true;
            }
            return false;
        } catch ( Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
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
