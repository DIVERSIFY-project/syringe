package fr.inria.diversify.syringe.injectors;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by marodrig on 15/12/2015.
 */
public class CtParametrizedSnippetStatementTest {

    @Test
    public void testSetParameter() throws Exception {
        CtParametrizedSnippetStatement snippet = new CtParametrizedSnippetStatement();
        snippet.setParameter("type", "double");
        snippet.setParameter("value", 1);
        snippet.setValue("log%type%(%value%)");
        assertEquals("logdouble(1)", snippet.getValue());
    }

    @Test
    public void testSetValue() throws Exception {
        CtParametrizedSnippetStatement snippet = new CtParametrizedSnippetStatement();
        snippet.setValue("log%type%(%value%)");
        snippet.setParameter("type", "double");
        snippet.setParameter("value", 1);
        assertEquals("logdouble(1)", snippet.getValue());
    }
}