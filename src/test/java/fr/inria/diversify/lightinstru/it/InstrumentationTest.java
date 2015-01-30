package fr.inria.diversify.lightinstru.it;

import fr.inria.diversify.lightinstru.Configuration;

import java.net.URISyntaxException;

/**
 * Created by marodrig on 09/12/2014.
 */
public class InstrumentationTest {

    protected String SRC_DIR = "/src/main";
    protected String TEST_DIR = "/src/test";
    protected String PROJECT_DIR = "test-project";

    protected String getResPath(String name) throws URISyntaxException {
        return getClass().getResource("/" + name).toURI().getPath();
    }

    protected void instrument(Configuration conf) {

    }

}
