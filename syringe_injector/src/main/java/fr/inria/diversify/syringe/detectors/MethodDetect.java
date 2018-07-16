package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.events.BlockEvent;
import fr.inria.diversify.syringe.events.StatementDetectionEvent;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtExecutable;

import java.util.Arrays;
import java.util.Collection;

/**
 * Detect the first and last statement of a method (including constructors)
 * <p/>
 * Created by marodrig on 08/12/2014.
 */
public class MethodDetect extends AbstractDetector<CtExecutable> {

    public static String METHOD_BEGIN = "@Method.Begin@";
    public static String METHOD_END = "@Method.End@";

    @Override
    public Collection<String> eventsSupported() {
        return Arrays.asList(METHOD_BEGIN, METHOD_END);
    }

    @Override
    public void process(CtExecutable ex) {
        try {
            if (ex.getBody().getStatements().size() > 0) {
                if (listenerCount(METHOD_BEGIN) > 0)
                    notifyStatementDetection(ex.getBody().getStatement(0), METHOD_BEGIN);
                if (listenerCount(METHOD_END) > 0)
                    notifyStatementDetection(ex.getBody().getLastStatement(), METHOD_END);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
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
