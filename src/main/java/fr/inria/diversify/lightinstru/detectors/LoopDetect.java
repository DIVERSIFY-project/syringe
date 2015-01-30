package fr.inria.diversify.lightinstru.detectors;

import fr.inria.diversify.lightinstru.injectors.Injector;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtLoop;
import spoon.reflect.code.CtStatement;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourceCodeFragment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Detect the begin of a method
 * <p>
 * Created by marodrig on 08/12/2014.
 */
public class LoopDetect extends Detector<CtLoop> {

    public static String BEGIN_KEY = "@Loop.Begin@";

    public static String END_KEY = "@Loop.End@";

    /**
     * Injectors to inject in the begining of the method
     */
    Collection<Injector> beginInjectors;

    /**
     * Injectors to inject at the end of the method
     */
    Collection<Injector> endInjectors;

    /**
     * Method detector constructor
     */
    public LoopDetect() {
        data = new DetectionData();
    }

    @Override
    public void collectInjectors(AbstractMap<String, Collection<Injector>> injectors) {
        beginInjectors = injectors.containsKey(BEGIN_KEY) ? injectors.get(BEGIN_KEY) : new ArrayList<>();
        endInjectors = injectors.containsKey(END_KEY) ? injectors.get(END_KEY) : new ArrayList<>();
    }

    private void processStatement(CtElement e, Collection<Injector> begin, Collection<Injector> end) {
        SourcePosition sp = e.getPosition();
        CompilationUnit cu = sp.getCompilationUnit();
        int index = sp.getSourceStart() + 1;

        //Set id of the element to the id map
        elementsDetected++;
        putSignatureIntoData(getSignatureFromElement(e));

        //Begin snippet
        String snippet = "\ttry{\n\t" + getSnippet(begin, e, data);
        cu.addSourceCodeFragment(new SourceCodeFragment(index, snippet, 0));
        snippet = "\n\t} finally {" + getSnippet(end, e, data) + " }";
        cu.addSourceCodeFragment(new SourceCodeFragment(sp.getSourceEnd(), snippet, 0));

    }

    @Override
    public void process(CtLoop element) {
        if ( element.getBody().getElements(new TypeFilter<>(CtElement.class)).size() > 0 ) {
            //Don't process empty loop bodies
            processStatement(element.getBody(), beginInjectors, endInjectors);
        }
    }

}
