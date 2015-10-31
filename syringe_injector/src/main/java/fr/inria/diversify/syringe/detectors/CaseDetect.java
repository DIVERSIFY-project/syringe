package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.injectors.Injector;
import spoon.reflect.code.CtCase;
import spoon.reflect.code.CtStatement;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourceCodeFragment;
import spoon.reflect.cu.SourcePosition;
import spoon.support.reflect.code.CtLocalVariableImpl;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Detect the begin of a method
 * <p>
 * Created by marodrig on 08/12/2014.
 */
public class CaseDetect extends BaseDetector<CtCase> {

    public static String BEGIN_KEY = "@Case.Begin@";

    public static String END_KEY = "@Case.End@";

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
    public CaseDetect() {
        data = new DetectionData();
    }

    @Override
    public void collectInjectors(AbstractMap<String, Collection<Injector>> injectors) {
        beginInjectors = injectors.containsKey(BEGIN_KEY) ? injectors.get(BEGIN_KEY) : new ArrayList<Injector>();
        endInjectors = injectors.containsKey(END_KEY) ? injectors.get(END_KEY) : new ArrayList<Injector>();
    }

    private void processStatement(List<CtStatement> es, Collection<Injector> begin, Collection<Injector> end) {
        SourcePosition sp = es.get(0).getPosition();
        CompilationUnit cu = sp.getCompilationUnit();
        int index = sp.getSourceStart();

        if ( es.get(0) instanceof CtLocalVariableImpl) {
            CtLocalVariableImpl l = (CtLocalVariableImpl)es.get(0);
            index = l.getPosition().getCompilationUnit().beginOfLineIndex(l.getDefaultExpression().getPosition().getSourceStart());
        }

        //Set id of the element to the id map
        elementsDetected++;
        putSignatureIntoData(getSignatureFromElement(es.get(0)));

        //Begin snippet
        String snippet = " try{" + getSnippet(begin, es.get(0), data);
        cu.addSourceCodeFragment(new SourceCodeFragment(index, snippet, 0));

        //TODO have into consideration if is not a break statement it may need a semicolon

        sp = es.get(es.size()-1).getPosition();
        snippet = "} finally {" + getSnippet(end, es.get(es.size()-1), data) + " }";
        cu.addSourceCodeFragment(new SourceCodeFragment(sp.getSourceEnd() + 1, snippet, 0));
    }

    @Override
    public void process(CtCase element) {
        if ( element.getStatements().size() > 0 ) {
            processStatement(element.getStatements(), beginInjectors, endInjectors);
        }
    }
}
