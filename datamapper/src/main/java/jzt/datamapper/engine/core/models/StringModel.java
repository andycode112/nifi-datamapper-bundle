
package jzt.datamapper.engine.core.models;

/**
 * This class implements {@link Model} interface to hold String data
 */
public class StringModel implements Model<String> {

    private String mapDataHolder;

    public StringModel(String model) {
        mapDataHolder = model;
    }

    @Override
    public String getModel() {
        return mapDataHolder;
    }

    @Override
    public void setModel(String model) {
        mapDataHolder = model;
    }
}
