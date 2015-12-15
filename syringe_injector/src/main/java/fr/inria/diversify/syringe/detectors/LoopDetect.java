package fr.inria.diversify.syringe.detectors;

import org.apache.log4j.Logger;
import spoon.reflect.code.CtLoop;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourceCodeFragment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Detect the begin of a method
 * <p/>
 * Created by marodrig on 08/12/2014.
 */
public class LoopDetect extends AbstractDetector<CtLoop> {

    final static Logger logger = Logger.getLogger(LoopDetect.class);

    //Injector to listen just before the first statement of the loop
    public static String BEGIN_KEY = "@Loop.Begin@";

    //Injectos to listen just before the loop
    public static String BEFORE_KEY = "@Loop.BEFORE@";

    //Injector to listen just after the last statement in the loop
    public static String END_KEY = "@Loop.End@";

    //Injector to listen just after the loop
    public static String AFTER_KEY = "@Loop.AFTER@";

/*
    protected void processBeforeAfter(CtLoop e, Collection<BaseEventListener> before, Collection<BaseEventListener> after) {
        SourcePosition sp = e.getPosition();
        CompilationUnit cu = sp.getCompilationUnit();
        int index = sp.getSourceStart();

        //Set id of the element to the id map
        elementsDetected++;
        putSignatureIntoData(getSignatureFromElement(e));

        String snippet = getSnippet(before, e, data);
        cu.addSourceCodeFragment(new SourceCodeFragment(index, snippet, 0));

        snippet = getSnippet(after, e, data);
        cu.addSourceCodeFragment(new SourceCodeFragment(sp.getSourceEnd() + 1, snippet, 0));
    }

    private void processBeginEnd(CtElement e, Collection<BaseEventListener> begin, Collection<BaseEventListener> end) {
        SourcePosition sp = e.getPosition();
        CompilationUnit cu = sp.getCompilationUnit();
        int index = sp.getSourceStart() + 1;

        //Begin snippet
        String snippet = "\ttry{\n\t" + getSnippet(begin, e, data);
        cu.addSourceCodeFragment(new SourceCodeFragment(index, snippet, 0));
        snippet = "\n\t} finally {" + getSnippet(end, e, data) + " }";
        cu.addSourceCodeFragment(new SourceCodeFragment(sp.getSourceEnd(), snippet, 0));

    }
*/
    @Override
    public void process(CtLoop element) {
        try {
            /*
            //Process the before and after
            processBeforeAfter(element, beforeInjectors, afterInjectors);
            //Process the begin and end of the loop. Don't process empty loop bodies
            if (element.getBody() != null &&
                    element.getBody().getElements(new TypeFilter<>(CtElement.class)).size() > 0) {
                processBeginEnd(element.getBody(), beginInjectors, endInjectors);
            }*/
        } catch (Exception e) {
            logger.warn(e);
            throw e;
        }
    }


    @Override
    public Collection<String> eventsSupported() {
        return Arrays.asList(AFTER_KEY, BEFORE_KEY, BEGIN_KEY, END_KEY);
    }
}
