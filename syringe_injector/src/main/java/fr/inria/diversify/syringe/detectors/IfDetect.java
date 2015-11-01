package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.injectors.BaseInjector;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtStatement;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourceCodeFragment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Detect the begin of a method
 *
 * Created by marodrig on 08/12/2014.
 */
public class IfDetect extends BaseDetector<CtIf> {

    public static String BEGIN_KEY = "@If.Begin@";
    public static String END_KEY = "@If.End@";

    public static String ELSE_BEGIN_KEY = "@Else.Begin@";
    public static String ELSE_END_KEY = "@Else.End@";

    /**
     * Injectors to inject in the begining of the method
     */
    Collection<BaseInjector> beginInjectors;

    /**
     * Injectors to inject at the end of the method
     */
    Collection<BaseInjector> endInjectors;

    /**
     * Injectors to inject in the begining of the method
     */
    Collection<BaseInjector> elseBeginInjectors;

    /**
     * Injectors to inject at the end of the method
     */
    Collection<BaseInjector> elseEndInjectors;

    /**
     * Method detector constructor
     */
    public IfDetect() {
        data = new DetectionData();
     }

    @Override
    public void collectInjectors(AbstractMap<String, Collection<BaseInjector>> injectors) {
        beginInjectors = injectors.containsKey(BEGIN_KEY) ? injectors.get(BEGIN_KEY) : new ArrayList<BaseInjector>();
        endInjectors = injectors.containsKey(END_KEY) ? injectors.get(END_KEY) : new ArrayList<BaseInjector>();
        elseBeginInjectors = injectors.containsKey(ELSE_BEGIN_KEY) ? injectors.get(ELSE_BEGIN_KEY) : new ArrayList<BaseInjector>();
        elseEndInjectors = injectors.containsKey(ELSE_END_KEY) ? injectors.get(ELSE_END_KEY) : new ArrayList<BaseInjector>();
    }



    private void processStatement(CtElement e, Collection<BaseInjector> begin, Collection<BaseInjector> end) {
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
        cu.addSourceCodeFragment(new SourceCodeFragment(sp.getSourceEnd() ,snippet , 0));

    }

    @Override
    public void process(CtIf element) {
        processStatement(element.getThenStatement(), beginInjectors, endInjectors);
        CtStatement els = element.getElseStatement();

        //The else if are else statement wich are instances of CtIf.
        //These must be processed in the process call for the CtIf
        if ( els != null && !(els instanceof CtIf) ) {
            processStatement(element.getElseStatement(), elseBeginInjectors, elseEndInjectors);
        }
    }
}
