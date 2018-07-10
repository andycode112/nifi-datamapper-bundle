
package jzt.datamapper.engine.input.readers.events;

public enum ReaderEventType {

    OBJECT_START("ObjectStart"),
    OBJECT_END("ObjectEnd"),
    ARRAY_START("ArrayStart"),
    ARRAY_END("ArrayEnd"),
    FIELD("Field"),
    TERMINATE("Terminate"),
    ANONYMOUS_OBJECT_START("AnonymousObjectStart"),
    PRIMITIVE("Primitive");
    private final String value;

    ReaderEventType(String value) {
        this.value = value;
    }

    @Override public String toString() {
        return value;
    }

}

