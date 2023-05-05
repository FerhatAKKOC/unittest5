package org.unittest.parameterresolvers;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class FooParameterResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {

        // returns true if the parameter’s type is supported (Foo in this example)
        return parameterContext.getParameter().getType() == Foo.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {

        // serves up an object of the correct type (a new Foo instance in this example), which will then be injected in your test method
        return new Foo("Ferat","Akkoc");
    }
}
