package fr.inria.diversify.syringe.injectors;

import spoon.support.reflect.code.CtCodeSnippetStatementImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * A parametrized code snippet.
 * <p/>
 * You can put parameters enclosing them in percent symbols (%%) like this:
 * <p/>
 * log%type%(%value%);
 * <p/>
 * Created by marodrig on 15/12/2015.
 */
public class CtParametrizedSnippetStatement extends CtCodeSnippetStatementImpl {

    private HashMap<String, Object> parameters = new HashMap<>();

    private String templateString = "";

    /**
     * Returns the value of a given parameter
     *
     * @param parameter The name of the parameter
     * @return
     */
    public Object getParameter(String parameter) {
        return parameters;
    }

    /**
     * Sets the value of a parameter
     *
     * @param parameter Name of the parameter
     * @param value     Value of the parameter
     */
    public void setParameter(String parameter, Object value) {
        this.parameters.put(parameter, value);
        String v = buildValue();
        super.setValue(v);
    }

    public void setParameters(HashMap<String, Object> parameters) {
        this.parameters = parameters;
        String v = buildValue();
        super.setValue(v);
    }

    /**
     * Indicates if it has a parameter
     *
     * @param parameter
     * @return
     */
    public boolean hasParameter(String parameter) {
        return this.parameters.containsKey(parameter);
    }

    /**
     * Template string for the parametrized snippet statement
     *
     * @param value value of the parametrized string
     */
    @Override
    public void setValue(String value) {
        templateString = value;
        String v = buildValue();
        super.setValue(v);
    }

    private String buildValue() {
        String value = templateString;
        for (Map.Entry<String, Object> e : parameters.entrySet()) {
            String p = e.getKey();
            try {
                value = value.replaceAll("%" + p + "%", e.getValue().toString());
            } catch (NullPointerException ex) {
                throw new RuntimeException("Null value exception when obtaining value of parameter " + p , ex);
            } catch (Exception ex) {
                throw new RuntimeException("Unexpected exception when building snippet", ex);
            }
        }
        return value;
    }

    /**
     * Returns the template string
     *
     * @return
     */
    public String getTemplateString() {
        return templateString;
    }


}
