
package jzt.datamapper.engine.input.readers.events;

public class ReaderEvent {

    private ReaderEventType eventType;
    private String name;
    private Object value;
    private String fieldType;

    public ReaderEvent(ReaderEventType eventType, String name, Object value, String fieldType) {
        this.eventType = eventType;
        this.name = name;
        this.value = value;
        this.fieldType = fieldType;
    }

    public ReaderEvent(ReaderEventType eventType, String name, Object value) {
        this.eventType = eventType;
        this.name = name;
        this.value = value;
    }

    public ReaderEvent(ReaderEventType eventType, String name) {
        this.eventType = eventType;
        this.name = name;
    }

    public ReaderEvent(ReaderEventType eventType) {
        this.eventType = eventType;
    }

    public void setEventType(ReaderEventType eventType) {
        this.eventType = eventType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ReaderEventType getEventType() {
        return eventType;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
}
