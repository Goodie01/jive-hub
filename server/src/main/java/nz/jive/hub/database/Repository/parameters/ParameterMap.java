package nz.jive.hub.database.Repository.parameters;

import nz.jive.hub.Parameters;

import java.util.List;
import java.util.Map;

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

    Map<Parameters, String> getAll();
}
