package fr.inria.diversify.syringe.detectors;

import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourceCodeFragment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtExecutable;

import java.util.Arrays;
import java.util.Collection;

/**
 * Detect the first and last statement of a method (including constructors)
 *
 * Created by marodrig on 08/12/2014.
 */
public class MethodDetect extends AbstractDetector<CtExecutable> {

    public static String BEGIN_KEY = "@Method.Begin@";
    public static String END_KEY = "@Method.End@";

    @Override
    public Collection<String> eventsSupported() {
        return Arrays.asList(BEGIN_KEY, END_KEY);
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
        //putSignatureIntoData(getSignatureFromElement(executable));

        //Begin snippet
        String snippet = "";//"\ttry{\n\t" + getSnippet(beginInjectors, executable, data) + System.lineSeparator();

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
        //snippet = "\n\t} finally {" + getSnippet(endInjectors, executable, data) + " }";
        compileUnit.addSourceCodeFragment(new SourceCodeFragment(sp.getSourceEnd()+2 ,snippet , 0));
    }


    @Override
    public int getElementsDetectedCount() {
        return elementsDetected;
    }
}
