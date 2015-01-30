package fr.inria.diversify.lightinstru.detectors;

import fr.inria.diversify.lightinstru.injectors.Injector;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourceCodeFragment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.reference.CtExecutableReference;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A detector to detect JUnit assert methods and Java built-in assertions
 *
 * Created by marodrig on 22/12/2014.
 */
public class AssertDetector extends Detector<CtInvocation<?>> {

    public static String END_KEY = "@Assert.End@";

    private Collection<Injector> endInjectors;

    public AssertDetector() {
        super();
        data = new DetectionData();
    }

    @Override
    public void collectInjectors(AbstractMap<String, Collection<Injector>> injectors) {
        endInjectors = injectors.containsKey(END_KEY) ? injectors.get(END_KEY) : new ArrayList<>();
    }

    @Override
    public boolean isToBeProcessed(CtInvocation<?> candidate) {
        try {
            return candidate.getExecutable().getSimpleName().startsWith("assert");
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void process(CtInvocation<?> invocation) {
        SourcePosition sp = invocation.getPosition();
        CompilationUnit compileUnit = sp.getCompilationUnit();
        String snippet = "";

        //Set id of the element to the id map
        elementsDetected++;
        putSignatureIntoData(getSignatureFromElement(invocation));
        snippet += getSnippet(endInjectors, invocation, data)+";";

        compileUnit.addSourceCodeFragment(new SourceCodeFragment(sp.getSourceEnd() + 2,snippet, 0));
    }
}
