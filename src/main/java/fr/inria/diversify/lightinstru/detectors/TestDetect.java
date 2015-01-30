package fr.inria.diversify.lightinstru.detectors;

import fr.inria.diversify.lightinstru.injectors.Injector;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtStatement;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourceCodeFragment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtTypeReference;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Detect the begin of a method
 *
 * Created by marodrig on 08/12/2014.
 */
public class TestDetect extends MethodDetect {

    public static String BEGIN_KEY= "@Test.Begin@";
    public static String END_KEY = "@Test.End@";

    @Override
    public boolean isToBeProcessed(CtExecutable candidate) {
        /*
        if(!(isTestClass(candidate.getDeclaringType().getReference())))
            return false;*/

        if(candidate.isImplicit()
                || candidate.getBody() == null
                || candidate.getBody().getStatements().size() == 0)
            return false;

        for(CtAnnotation<?> annotation: candidate.getAnnotations())
            if(annotation.toString().startsWith("@org.junit.Test") ||
                    annotation.toString().startsWith("@org.junit.Before") ||
                    annotation.toString().startsWith("@org.junit.After"))
                return true;

        if(candidate.getSimpleName().contains("test"))
            return true;

        return false;
    }

    protected boolean isTestClass(CtTypeReference<?> type) {
        if(type.getSimpleName().endsWith("Test") || type.getSimpleName().endsWith("Behaviour")) // Behaviour for jbehave
            return true;
        else {
            try {
                if (type.getSuperclass() != null) return isTestClass(type.getSuperclass());
            } catch (Exception e) {}
        }
        return false;
    }

    @Override
    public void collectInjectors(AbstractMap<String, Collection<Injector>> injectors) {
        beginInjectors = injectors.containsKey(BEGIN_KEY) ? injectors.get(BEGIN_KEY) : new ArrayList<>();
        endInjectors = injectors.containsKey(END_KEY) ? injectors.get(END_KEY) : new ArrayList<>();
    }

}
