# syringe

Syringe is a lightweight library to instrument Java source code. Built on top of the Spoon open compiler, Syringe allows the user to detect most elements of the Java language using the Spoon meta-model. Custom filtering is allowed to separate the interesting elements of those who are not.

Once interesting elements are detected, a string defined by the user is injected. Using strings to inject code makes the definition of the injection much more simpler. Obviously, if the injection is executable code, the injected string may refer to a valid method call. In that case, Syringe automatically copy the classes containing the injected methods into the target project’s structure.

Finally, Syringe can be extended with custom detectors and injectors classes, enabling the user with more fine-grained control.