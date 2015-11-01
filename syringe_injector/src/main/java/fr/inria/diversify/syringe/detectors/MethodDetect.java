package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.injectors.BaseInjector;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourceCodeFragment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtExecutable;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Detect the begin of a method
 *
 * Created by marodrig on 08/12/2014.
 */
public class MethodDetect extends BaseDetector<CtExecutable> {

    public static String BEGIN_KEY = "@Method.Begin@";
    public static String END_KEY = "@Method.End@";

    /**
     * Injectors to inject in the begining of the method
     */
    Collection<BaseInjector> beginInjectors;

    /**
     * Injectors to inject at the end of the method
     */
    Collection<BaseInjector> endInjectors;



    /**
     * Method detector constructor
     */
    public MethodDetect() {
        data = new DetectionData();
     }

    public static String getEventName() {
        return BEGIN_KEY;
    }

    /**
     * Paired detection of begin end of the method
     * @return
     */

    @Override
    public void collectInjectors(AbstractMap<String, Collection<BaseInjector>> injectors) {
        beginInjectors = injectors.containsKey(BEGIN_KEY) ? injectors.get(BEGIN_KEY) : new ArrayList<BaseInjector>();
        endInjectors = injectors.containsKey(END_KEY) ? injectors.get(END_KEY) : new ArrayList<BaseInjector>();
    }

    @Override
    public void process(CtExecutable executable) {
        CtBlock body = executable.getBody();
        if ( body == null ) return;
        int bodySize = body.getStatements().size();
        if ( bodySize == 0 || (executable instanceof CtConstructor && bodySize <= 1) ) return;
        CtStatement stmt;
        if ( executable instanceof CtConstructor && body.getStatement(0) instanceof CtInvocation) {
            stmt = body.getStatement(1);
        } else stmt = body.getStatement(0);
        if ( stmt == null ) return;

        //Set id of the element to the id map
        elementsDetected++;
        //putSignatureIntoData(executable.getDeclaringType().getQualifiedName() + "." + executable.getSimpleName());
        putSignatureIntoData(getSignatureFromElement(executable));

        //Begin snippet
        String snippet = "\ttry{\n\t" + getSnippet(beginInjectors, executable, data) + System.lineSeparator();

        SourcePosition sp = stmt.getPosition();
        CompilationUnit compileUnit = sp.getCompilationUnit();

        int index;
        if(stmt.getPosition().getLine() == executable.getPosition().getLine()) {
            index = sp.getSourceStart();
        } else {
            index = compileUnit.beginOfLineIndex(sp.getSourceStart());
        }
        compileUnit.addSourceCodeFragment(new SourceCodeFragment(index, snippet, 0));

        sp = body.getLastStatement().getPosition();
        compileUnit = sp.getCompilationUnit();

        //End snippet:
        snippet = "\n\t} finally {" + getSnippet(endInjectors, executable, data) + " }";
        compileUnit.addSourceCodeFragment(new SourceCodeFragment(sp.getSourceEnd()+2 ,snippet , 0));
    }
}
