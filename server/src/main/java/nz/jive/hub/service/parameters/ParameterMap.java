package nz.jive.hub.service.parameters;

import nz.jive.hub.Parameters;

import java.util.List;

/**
 * @author Goodie
 */
public interface ParameterMap {
    String stringVal(Parameters tParameter);

    boolean boolVal(Parameters tParameter);
//    boolean booleanVal(Parameter tParameter);
//    int intVal(Parameter tParameter);

    List<String> stringListVal(Parameters tParameter);

    void set(Parameters tParameter, String value);

    void set(Parameters tParameter, List<Object> value);
}
