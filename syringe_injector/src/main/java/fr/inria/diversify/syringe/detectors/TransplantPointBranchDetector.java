package fr.inria.diversify.syringe.detectors;

import org.json.JSONArray;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;

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
    public void process(CtStatement statement) {

        //Register the ID of the parent
        //By doing this, we ensure that our ID is the same as our parent...
        branchParent = getParentBranchStatement(statement);
        if (!(branchParent instanceof CtClass) && branchParent != null) {

            if (branchParent instanceof CtCase)
                branchParent = (CtElement) ((CtCase) branchParent).getStatements().get(0);
            else if (branchParent instanceof CtLoop)
                branchParent = ((CtLoop) branchParent).getBody();

            //putSignatureIntoData(getSignatureFromElement(branchParent));

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
