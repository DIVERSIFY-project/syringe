package fr.inria.diversify.syringe;

import spoon.Launcher;

import java.util.ArrayList;

/**
 * Created by elmarce on 04/08/16.
 */
public class LauncherBuilder {

    public static Launcher build(ArrayList<String> src, String outputDir) {
        Launcher result = new Launcher();
        result.getEnvironment().setNoClasspath(true);
        for (String s : src) result.addInputResource(s);
        result.setSourceOutputDirectory(outputDir);
        return result;
    }

}
