package fr.inria.diversify.syringe.program;

import java.io.InputStreamReader;

/**
 * Takes a syringe program and turns it nto a SyringeInstrumenter
 *
 * Created by marodrig on 19/02/2015.
 */
public interface SyringeProgram {

    /**
     * Instrument a program
     * @param path Path to the project
     */
    public void execute(String path);

    /**
     * Instrument a program
     * @param reader Reader to obtain the instrumentation program from
     */
    public void execute(InputStreamReader reader);

}
