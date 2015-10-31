package fr.inria.diversify.syringe.program;

import java.util.Collection;
import java.util.Map;

/**
* Created by marodrig on 19/02/2015.
*/
public class ModuleConfiguration {

    private String source;

    private Collection<String> detect;

    private Map<String, String> inject;

    private String logger;

    /**
     * Detectors of the module
     */
    public Collection<String> getDetect() {
        return detect;
    }

    public void setDetect(Collection<String> detect) {
        this.detect = detect;
    }

    /**
     * Injectors of the module
     */
    public Map<String, String> getInject() {
        return inject;
    }

    public void setInject(Map<String, String> inject) {
        this.inject = inject;
    }

    /**
     * Logger of the module
     */
    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
