package fr.inria.diversify.syringe;

//import fr.inria.diversify.util.Log;
import spoon.compiler.SpoonCompiler;
import spoon.reflect.factory.Factory;
import spoon.reflect.factory.FactoryImpl;
import spoon.support.DefaultCoreFactory;
import spoon.support.StandardEnvironment;
import spoon.support.compiler.jdt.JDTBasedSpoonCompiler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by marodrig on 16/06/2014.
 */
@Deprecated
public class SpoonMetaFactory {

    public Factory buildNewFactory(String srcDirectory, int javaVersion) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ArrayList<String> a = new ArrayList<String>();
        a.add(srcDirectory);
        return buildNewFactory(a, javaVersion);
    }
    public Factory buildNewFactory(Collection<String> srcDirectory,
                                   int javaVersion) throws
            ClassNotFoundException, IllegalAccessException, InstantiationException {
        //String srcDirectory = DiversifyProperties.getProperty("project") + "/" + DiversifyProperties.getProperty("src");

        StandardEnvironment env = new StandardEnvironment();
        env.setComplianceLevel(javaVersion);
        env.setVerbose(true);
        env.setDebug(true);
        //env.useSourceCodeFragments(true);

        DefaultCoreFactory f = new DefaultCoreFactory();
        Factory factory = new FactoryImpl(f, env);
        SpoonCompiler compiler = new JDTBasedSpoonCompiler(factory);


        for (String s : srcDirectory) {
            for (String dir : s.split(System.getProperty("path.separator"))) {
                //Log.debug("add {} to classpath", dir);
                File dirFile = new File(dir);
                if (dirFile.isDirectory()) {
                    compiler.addInputSource(dirFile);
                }
            }
        }
        try {
            compiler.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return factory;
    }


}
