
package jzt.datamapper.engine.core.models;

/**
 * Interface to represent data in generic way in the data mapper engine.
 */
public interface Model<T> {

    void setModel(T model);

    T getModel();

}
