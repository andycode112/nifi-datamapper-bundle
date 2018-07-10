

package jzt.datamapper.engine.utils;

public enum InputOutputDataType {

    CSV("CSV"),
    XML("XML"),
    JSON("JSON");

    private String dataTypeValue;

    InputOutputDataType(String dataTypeValue) {
        this.dataTypeValue = dataTypeValue;
    }

    // Use to get the DataType from the relevant input and output data type
    public static InputOutputDataType fromString(String dataType) {
        if (dataType != null) {
            for (InputOutputDataType definedTypes : InputOutputDataType.values()) {
                if (dataType.equalsIgnoreCase(definedTypes.toString())) {
                    return definedTypes;
                }
            }
        }
        throw new IllegalArgumentException("Invalid input type found : " + dataType);
    }
}
