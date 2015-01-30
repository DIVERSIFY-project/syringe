package fr.inria.diversify.lightinstru.detectors;

import fr.inria.diversify.lightinstru.CodeFragmentEqualPrinter;
import fr.inria.diversify.lightinstru.injectors.Injector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spoon.reflect.code.*;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourceCodeFragment;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A transplantation point detector that assigns the same id to the TP point instrumentation that to his parent branch.
 *
 * This allows to connect the TP instrumentation method with the method of his parent.
 *
 * Created by marodrig on 17/12/2014.
 */
public class TransplantPointBranchDetector extends TransplantPointDetector {

    private CtElement branchParent;

    //Number of orphan transplant points detected without a parent branch (i.e. static fields)
    private int orphanTransplantPointsDetected;

    public TransplantPointBranchDetector(JSONArray persistence, int lineTolerance) {
        super(persistence, lineTolerance);
        orphanTransplantPointsDetected = 0;
    }

    /**
     * Obtains the parent branch statement
     *
     * @return An CtElement
     */
    private CtElement getParentBranchStatement(CtStatement e) {
        CtElement result = e.getParent();
        while (result != null &&
                !(result instanceof CtClass) &&
                !(result instanceof CtLoop) &&
                !(result instanceof CtExecutable) &&
                !(result instanceof CtCase)) {

            if (result.getParent() instanceof CtIf) {
                CtIf ctIf = (CtIf) result.getParent();
                if (result.equals(ctIf.getThenStatement()) || result.equals(ctIf.getElseStatement())) return result;
            }

            result = result.getParent();
        }
        return result;
    }

    @Override
    protected String getSignatureFromElement(CtElement e) {
        if ((branchParent instanceof CtClass) || branchParent == null) {
            orphanTransplantPointsDetected++;
            return super.getSignatureFromElement(e); //"Branch independent" Sosie
        } else if (branchParent instanceof CtCase) { //Make our signature, our parent's
            return super.getSignatureFromElement((CtStatement) ((CtCase) branchParent).getStatements().get(0));
        } else { //Make our signature, our parent's
            return super.getSignatureFromElement(branchParent);
        }
    }

    @Override
    public void process(CtStatement statement) {

        //Register the ID of the parent
        //By doing this, we ensure that our ID is the same as our parent...
        branchParent = getParentBranchStatement(statement);
        if (!(branchParent instanceof CtClass) && branchParent != null) {

            if (branchParent instanceof CtCase)
                branchParent = (CtElement) ((CtCase) branchParent).getStatements().get(0);
            else if (branchParent instanceof CtLoop)
                branchParent = ((CtLoop) branchParent).getBody();

            putSignatureIntoData(getSignatureFromElement(branchParent));

        }
        super.process(statement);
    }

    public int getOrphanTransplantPointsDetected() {
        return orphanTransplantPointsDetected;
    }

    public void setOrphanTransplantPointsDetected(int orphanTransplantPointsDetected) {
        this.orphanTransplantPointsDetected = orphanTransplantPointsDetected;
    }
}
