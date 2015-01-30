package fr.inria.diversify.lightinstru.detectors;

/**
 * A detector to detect JUnit assert methods and Java built-in assertions
 *
 * Created by marodrig on 22/12/2014.
 */

import fr.inria.diversify.lightinstru.injectors.Injector;
import spoon.reflect.code.CtAssert;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourceCodeFragment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.reference.CtExecutableReference;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A detector for built asserts
 */
public class BuiltInAssertDetector extends Detector<CtAssert<?>> {

    public static String END_KEY = "@Built.In.Assert.End@";

    private Collection<Injector> endInjectors;

    public BuiltInAssertDetector() {
        super();
        data = new DetectionData();
    }

    @Override
    public boolean isToBeProcessed(CtAssert<?> candidate) {
        return true;
    }

    @Override
    public void process(CtAssert ctAssert) {
        SourcePosition sp = ctAssert.getPosition();
        CompilationUnit compileUnit = sp.getCompilationUnit();
        String snippet = "";

        //Set id of the element to the id map
        elementsDetected++;
        putSignatureIntoData(getSignatureFromElement(ctAssert));
        snippet += getSnippet(endInjectors, ctAssert, data)+";";

        compileUnit.addSourceCodeFragment(new SourceCodeFragment(sp.getSourceEnd() + 2,snippet, 0));
    }

    @Override
    public void collectInjectors(AbstractMap<String, Collection<Injector>> injectors) {
        endInjectors = injectors.containsKey(END_KEY) ? injectors.get(END_KEY) : new ArrayList<>();
    }
}
