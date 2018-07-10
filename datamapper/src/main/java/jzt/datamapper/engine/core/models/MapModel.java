
package jzt.datamapper.engine.core.models;

import java.util.Map;

/**
 * This class implements {@link Model} interface with a {@link Map} to hold data
 */
public class MapModel implements Model<Map<String, Object>> {

    private Map<String, Object> mapDataHolder;

    public MapModel(Map<String, Object> model) {
        mapDataHolder = model;
    }

    @Override public void setModel(Map<String, Object> model) {
        mapDataHolder = model;
    }

    @Override public Map<String, Object> getModel() {
        return mapDataHolder;
    }
}
