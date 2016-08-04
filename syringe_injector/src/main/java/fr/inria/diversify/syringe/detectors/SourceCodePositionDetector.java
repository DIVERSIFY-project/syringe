package fr.inria.diversify.syringe.detectors;

import fr.inria.diversify.syringe.CodeFragmentEqualPrinter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;

import java.util.*;

/**
 * Detects an element with a given source code position in the form <qualified_name>:<line_number>, for example
 * my.package.Class:290
 *
 * Created by marodrig on 17/12/2014.
 */
@Deprecated
public class SourceCodePositionDetector extends AbstractDetector<CtStatement> {

    public static String BEGIN_KEY = "@TP.Begin@";

    private final JSONArray persistence;

    //Defines how many lines of tolerance
    private int lineTolerance;

    private Collection<CtStatement> statements;

    //The source code signatures, indexed by position in order to perform a "exact" fragment location.
    //This method allows no flexibility. Pros: No mistakes, Cons: Unable to find a fragment if shifted out of postion
    //or if the signature has minimum changes like \n for \r\rn
    private HashMap<String, HashSet<String>> exactLocationInfo;

    public SourceCodePositionDetector(JSONArray persistence, int lineTolerance) {
        this.persistence = persistence;
        statements = new ArrayList<>();
        this.lineTolerance = lineTolerance;

    }

    protected HashMap<String, HashSet<String>> getExactLocationInfo() throws JSONException {
        if (exactLocationInfo == null) {
            exactLocationInfo = new HashMap<>();
            for (int i = 0; i < persistence.length(); i++) {
                JSONObject o = persistence.getJSONObject(i);
                String pos = o.getString("position");
                String srcSig = o.has("sourceCode") ? o.getString("sourceCode") : o.getString("sourcecode");
                if (!exactLocationInfo.containsKey(pos)) {
                    exactLocationInfo.put(pos, new HashSet<String>());
                }
                exactLocationInfo.get(pos).add(srcSig);

            }
        }
        return exactLocationInfo;
    }



    public boolean isTP(CtStatement element) {
        try {
            CodeFragmentEqualPrinter pp = new CodeFragmentEqualPrinter(this.getEnvironment());

            element.accept(pp);
            String equalString = pp.toString();

            String pos = element.getPosition().getCompilationUnit().getMainType().getQualifiedName()
                    + ":" + element.getPosition().getLine();
            return getExactLocationInfo().containsKey(pos) && getExactLocationInfo().get(pos).contains(equalString);
            /*
            int p = Integer.MAX_VALUE;

            for (int i = 0; i < persistence.length(); i++) {
                JSONObject o = persistence.getJSONObject(i);
                String[] pPos = o.getString("position").split(":");
                if (decType.equals(pPos[0])) {
                    int pLine = Integer.parseInt(pPos[1]);
                    String srcSig = o.has("sourceCode") ? o.getString("sourceCode") : o.getString("sourcecode");
                    if ( StringSimilarity.CompareStrings(srcSig, equalString) >= 0.95 ) {
                        if (pLine == elemPos) return true;
                        else {
                            int k = Math.abs(pLine - elemPos);
                            if ( p > k ) p = k;
                        }
                    }
                }
            }

            if (p < lineTolerance) return true;
            */
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean isToBeProcessed(CtStatement statement) {
        return isTP(statement);
    }

    private String detectSnippet( CtElement statement ) {
        /*
        elementsDetected++;
        putSignatureIntoData(getSignatureFromElement(statement));
        return getSnippet(beginInjectors, statement, data);
        */
        return null;
    }


    @Override
    public void process(CtStatement statement) {

        CtElement e = statement;
        //data.setEndWithSemiColon(true);
/*
        boolean stop = false;
        while (stop == false) {
            try {
                stop = true;
                if (e != null) {
                    if (e.getParent() instanceof CtClass) {
                        stop = true;
                    } else if (e instanceof CtBreak) {
                        String probeStr = detectSnippet(e);
                        cu.addSourceCodeFragment(new SourceCodeFragment(e.getPosition().getSourceStart(), probeStr + ";", 0));
                    } else if (e instanceof CtBlock) {
                        e = e.getParent();
                        stop = false;
                    } else if (e.getParent() instanceof CtBlock) {
                        String probeStr = detectSnippet(e);
                        int b = cu.beginOfLineIndex(e.getPosition().getSourceStart());
                        cu.addSourceCodeFragment(new SourceCodeFragment(b, probeStr + ";", 0));
                    } else if (e instanceof CtIf) {
                        //data.setEndWithSemiColon(false);
                        CtExpression c = ((CtIf) e).getCondition();
                        String probeStr = detectSnippet(e);
                        cu.addSourceCodeFragment(new SourceCodeFragment(
                                c.getPosition().getSourceStart(), probeStr + "||", 0));
                    } else {
                        e = e.getParent();
                        stop = false;
                    }
                }
            } catch (NullPointerException np) {
                throw new RuntimeException(np);
            }
        }*/
    }

    @Override
    public Collection<String> eventsSupported() {
        return Arrays.asList(BEGIN_KEY);
    }
}
