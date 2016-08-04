package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.events.BlockEvent;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtExecutable;
import spoon.support.reflect.code.CtCodeSnippetStatementImpl;

import java.util.Arrays;
import java.util.Collection;

/**
 * Detect the first and last statement of a method (including constructors)
 * <p/>
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
        int bCount = listenerCount(BEGIN_KEY);
        int eCount = listenerCount(END_KEY);
        if (bCount > 0 || eCount > 0) {
            BlockEvent event = new BlockEvent(executable, executable.getBody());
            putSignatureIntoEvent(event, executable);
            if (bCount > 0) notify(BEGIN_KEY, event);
            if (eCount > 0) notify(END_KEY, event);
            elementsDetected++;
        }
    }


    @Override
    public int getElementsDetectedCount() {
        return elementsDetected;
    }


        /*
        if(stmt.getPosition().getLine() == executable.getPosition().getLine()) {
            index = sp.getSourceStart();
        } else {
            index = compileUnit.beginOfLineIndex(sp.getSourceStart());
        }
        compileUnit.add addSourceCodeFragment(new SourceCodeFragment(index, snippet, 0));
*/

}
