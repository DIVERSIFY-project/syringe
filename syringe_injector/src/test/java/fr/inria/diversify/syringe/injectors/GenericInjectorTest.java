package fr.inria.diversify.syringe.injectors;

import fr.inria.diversify.syringe.ProcessingTest;
import fr.inria.diversify.syringe.detectors.AssertDetector;
import fr.inria.diversify.syringe.detectors.CaseDetect;
import org.junit.Test;
import spoon.reflect.code.CtAssert;
import spoon.reflect.declaration.CtElement;

import java.util.HashMap;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Created by marodrig on 15/12/2015.
 */
public class GenericInjectorTest extends ProcessingTest {

    @Test
    public void testListen() throws Exception {

        GenericInjector injector = new GenericInjector();
        injector.setInjectAt(GenericInjector.InjectionPosition.BEFORE);
        injector.setInjectionTemplate("logAssert(%name%)");
        injector.setParameterCollector(new ParameterCollector<CtAssert>() {
            @Override
            public HashMap<String, Object> collectParameters(CtAssert element) {
                HashMap<String, Object> params = new HashMap<>();
                return null;
            }
        });

        CaseDetect d = new CaseDetect();
        d.addListener(AssertDetector.ASSERT_DETECTED, injector);
        String folder = this.getClass().getResource("/control").toURI().getPath();
        process(d, folder);
    }
}