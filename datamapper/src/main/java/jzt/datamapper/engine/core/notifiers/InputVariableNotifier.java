
package jzt.datamapper.engine.core.notifiers;


import jzt.datamapper.engine.core.exceptions.JSException;
import jzt.datamapper.engine.core.exceptions.ReaderException;
import jzt.datamapper.engine.core.exceptions.SchemaException;

public interface InputVariableNotifier {

    void notifyInputVariable(Object variable) throws SchemaException, JSException, ReaderException;
}
