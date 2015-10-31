package fr.inria.diversify.syringe.program;

import fr.inria.diversify.syringe.SyringeInstrumenter;
import fr.inria.diversify.syringe.SyringeInstrumenterImpl;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

/**
 * Takes a syringe program and turns it nto a SyringeInstrumenter
 *
 * Created by marodrig on 19/02/2015.
 */
public class SyringeProgramImpl implements SyringeProgram {

    /**
     * Factory method to create a SyringeInstrumenter
     * @return
     */
    protected SyringeInstrumenter getInstrumenter(String projectDir, String productionDir, String outputDir) {
        return new SyringeInstrumenterImpl(projectDir, productionDir, outputDir);
    }

    protected ProgramConfiguration getConfiguration(InputStreamReader reader) {
        //Read the configuration
        Constructor constructor = new Constructor(ProgramConfiguration.class);
        TypeDescription programDesc = new TypeDescription(ProgramConfiguration.class);
        programDesc.putListPropertyType("instrument", ModuleConfiguration.class);
        constructor.addTypeDescription(programDesc);
        Yaml yaml = new Yaml(constructor);
        return  (ProgramConfiguration) yaml.load(reader);
    }

    /**
     *  Creates the instrumenter
     */
    protected void createInstrumenter(InputStreamReader reader) {
        ProgramConfiguration configuration = getConfiguration(reader);
        //SyringeInstrumenter instrumenter = getInstrumenter();
    }

    /**
     * Executes a program
     * @param path Path to the project
     */
    public void execute(String path) {
        try {
            execute(new InputStreamReader(new FileInputStream(path)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Executes a program
     * @param reader Reader to obtain the instrumentation program from
     */
    public void execute(InputStreamReader reader) {
        createInstrumenter(reader);
    }


}
