# Syringe

Syringe is a lightweight library to instrument Java source code. Built on top of the Spoon open compiler, Syringe allows the user to detect most elements of the Java language using the Spoon meta-model. Custom filtering is allowed to separate the interesting elements of those who are not.

Once interesting elements are detected, a string defined by the user is injected. Using strings to inject code makes the definition of the injection much more simpler. Obviously, if the injection is executable code, the injected string may refer to a valid method call. In that case, Syringe automatically copy the classes containing the injected methods into the target project’s structure.

Finally, Syringe can be extended with custom detectors and injectors classes, enabling the user with more fine-grained control.

# Example of use

```java
	TestLogger.totalCount = 0;
	
	//You must configure the Syringe by adding detectors 
	//and injectors. Detectors detect interesting parts in the code
	//And injectors inject the code.
	
	//Tell the configuration the path of the source code
	//you want to instrument
	Configuration confSrc = new Configuration(SRC_DIR + "/java");
	
	//Add a detector. In this simple case, we will detect methods and loops only
	confSrc.addDetector(new MethodDetect());
	confSrc.addDetector(new LoopDetect());
	
	//When you detect an element, you may want to inject something:
	//In this case we will inject a static method called... "method" (I'm so original :)
	String injection = TestLogger.class.getCanonicalName() + ".method();";
	confSrc.addInjector(MethodDetect.getEventName(), new GenericInjector(injection));
	
	//And when loops are detected, we inject another string:
	injection = TestLogger.class.getCanonicalName() + ".loopBegin();";
	confSrc.addInjector(LoopDetect.getEventName(), new GenericInjector(injection));
	
	//If you want to inject executable code, 
	//Syringe may copy classes to a location 
	//that can be found by the production code:
	confSrc.addLogger(TestLogger.class);
	
	//Now we configure a different path in the same project.
	//Using different a configuration objects allows us to detect and inject 
	//different elements in the path:
	Configuration confTest = new Configuration(TEST_DIR + "/java");
	confTest.addDetector(new TestDetect());
	injection = TestLogger.class.getCanonicalName() + ".test();";
	confTest.addInjector(TestDetect.BEGIN_KEY, new GenericInjector(injection));
	confTest.addLogger(TestLogger.class);
	
	//Instrument!
	LightInstru l = new LightInstru(getResPath(PROJECT_DIR), SRC_DIR, getResPath("") + OUT_FOLDER);
	l.instrument(confSrc);
	l.instrument(confTest);
	
	//Don't execute the program your self!
	//Syringe does that for you for only 99.99EU/mo!!!
	l.runTests();
	
	//Just kidding about the 99.99EU/mo
	//Is really more... ;)
```	
	