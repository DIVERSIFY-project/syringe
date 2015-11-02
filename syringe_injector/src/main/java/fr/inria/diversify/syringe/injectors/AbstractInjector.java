package fr.inria.diversify.syringe.injectors;

/**
 * Created by marodrig on 02/11/2015.
 */
public abstract class AbstractInjector implements Injector {

    //A generic injection string
    private String injectionString;

    @Override
    public String getInjectionString() {
        return injectionString;
    }

    @Override
    public void setInjectionString(String s) {
        injectionString = s;
    }

}
