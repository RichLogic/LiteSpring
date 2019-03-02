package org.litespring.beans;

public class PropertyValue {

    private String name;
    private Object value;
    private boolean converted = false;
    private Object convertedValue;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setConverted(boolean converted) {
        this.converted = converted;
    }

    public boolean isConverted() {
        return converted;
    }

    public Object getConvertedValue() {
        return convertedValue;
    }
}
