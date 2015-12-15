package fr.inria.diversify.syringe.program;

import java.util.Collection;
import java.util.List;

/**
 * Syringe configuration as the example: *

 instrument:

 - source: src/main/java
 detect: [method, loop, ifs]
 listen:
 at method : .method() with_iD
 at loop:    .loop()   with_iD
 at if:      .if()     with_iD
 logger: fr.inria.MyLogger

 - source: src/test/java
 detect: [test, assertions]
 listen:
 at method : .method() with_iD
 at loop:    .loop()   with_iD
 at if:      .if()     with_iD
 logger: fr.inria.MyLogger
 *
 * Created by marodrig on 19/02/2015.
 */
public class ProgramConfiguration {

    private String perform;

    /**
     * Classes the injection program uses
     */
    public Collection<String> getUses() {
        return uses;
    }

    public void setUses(Collection<String> uses) {
        this.uses = uses;
    }

    /**
     * Custom detection classes
     */
    public Collection<String> getDetects() {
        return detects;
    }

    public void setDetects(Collection<String> detects) {
        this.detects = detects;
    }

    /**
     * Base path for the project
     */
    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    /**
     * Output path for the injection
     */
    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * Module configuration
     */
    public List<ModuleConfiguration> getInstrument() {
        return instrument;
    }

    public void setInstrument(List<ModuleConfiguration> instrument) {
        this.instrument = instrument;
    }

    private Collection<String> uses;

    private Collection<String> detects;

    private String project;

    private String output;

    private List<ModuleConfiguration> instrument;

    /**
     * What to do when executing the program
     */
    public String getPerform() {
        return perform;
    }

    public void setPerform(String perform) {
        this.perform = perform;
    }
}
