package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.injectors.BaseInjector;
import org.apache.log4j.Logger;
import spoon.reflect.code.CtLoop;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourceCodeFragment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Detect the begin of a method
 * <p/>
 * Created by marodrig on 08/12/2014.
 */
public class LoopDetect extends BaseDetector<CtLoop> {

    final static Logger logger = Logger.getLogger(LoopDetect.class);

    //Injector to inject just before the first statement of the loop
    public static String BEGIN_KEY = "@Loop.Begin@";

    //Injectos to inject just before the loop
    public static String BEFORE_KEY = "@Loop.BEFORE@";

    //Injector to inject just after the last statement in the loop
    public static String END_KEY = "@Loop.End@";

    //Injector to inject just after the loop
    public static String AFTER_KEY = "@Loop.AFTER@";

    /**
     * Injectors to inject in the begining of the method
     */
    protected Collection<BaseInjector> beforeInjectors;

    /**
     * Injectors to inject at the end of the method
     */
    protected Collection<BaseInjector> afterInjectors;

    /**
     * Injectors to inject in the begining of the method
     */
    protected Collection<BaseInjector> beginInjectors;

    /**
     * Injectors to inject at the end of the method
     */
    protected Collection<BaseInjector> endInjectors;

    /**
     * Loop detector constructor
     */
    public LoopDetect() {
        data = new DetectionData();
    }

    @Override
    public void collectInjectors(AbstractMap<String, Collection<BaseInjector>> injectors) {
        beginInjectors = injectors.containsKey(BEGIN_KEY) ? injectors.get(BEGIN_KEY) : new ArrayList<BaseInjector>();
        endInjectors = injectors.containsKey(END_KEY) ? injectors.get(END_KEY) : new ArrayList<BaseInjector>();
        beforeInjectors = injectors.containsKey(BEFORE_KEY) ? injectors.get(BEFORE_KEY) : new ArrayList<BaseInjector>();
        afterInjectors = injectors.containsKey(AFTER_KEY) ? injectors.get(AFTER_KEY) : new ArrayList<BaseInjector>();
    }

    protected void processBeforeAfter(CtLoop e, Collection<BaseInjector> before, Collection<BaseInjector> after) {
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

    private void processBeginEnd(CtElement e, Collection<BaseInjector> begin, Collection<BaseInjector> end) {
        SourcePosition sp = e.getPosition();
        CompilationUnit cu = sp.getCompilationUnit();
        int index = sp.getSourceStart() + 1;

        //Begin snippet
        String snippet = "\ttry{\n\t" + getSnippet(begin, e, data);
        cu.addSourceCodeFragment(new SourceCodeFragment(index, snippet, 0));
        snippet = "\n\t} finally {" + getSnippet(end, e, data) + " }";
        cu.addSourceCodeFragment(new SourceCodeFragment(sp.getSourceEnd(), snippet, 0));

    }

    @Override
    public void process(CtLoop element) {
        try {
            //Process the before and after
            processBeforeAfter(element, beforeInjectors, afterInjectors);
            //Process the begin and end of the loop. Don't process empty loop bodies
            if (element.getBody() != null &&
                    element.getBody().getElements(new TypeFilter<>(CtElement.class)).size() > 0) {
                processBeginEnd(element.getBody(), beginInjectors, endInjectors);
            }
        } catch (Exception e) {
            logger.warn(e);
            throw e;
        }
    }

    public void setBeginInjectors(Collection<BaseInjector> beginInjectors) {
        this.beginInjectors = beginInjectors;
    }

    public void setEndInjectors(Collection<BaseInjector> endInjectors) {
        this.endInjectors = endInjectors;
    }

    public Collection<BaseInjector> getBeginInjectors() {

        return beginInjectors;
    }

    public Collection<BaseInjector> getEndInjectors() {
        return endInjectors;
    }

}
