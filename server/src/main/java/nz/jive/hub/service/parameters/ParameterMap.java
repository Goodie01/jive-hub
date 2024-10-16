package nz.jive.hub.service.parameters;

import java.util.List;
import nz.jive.hub.Parameter;

/**
 * @author Goodie
 */
public interface ParameterMap {
    String stringVal(Parameter tParameter);
//    boolean booleanVal(Parameter tParameter);
//    int intVal(Parameter tParameter);

    List<String> stringListVal(Parameter tParameter);

    void set(Parameter tParameter, String value);

    void set(Parameter tParameter, List<Object> value);
}
