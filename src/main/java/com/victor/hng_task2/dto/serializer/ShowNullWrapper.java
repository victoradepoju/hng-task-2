package com.victor.hng_task2.dto.serializer;

public class ShowNullWrapper {
    private final String value;
    private final boolean explicitlySet;

    public ShowNullWrapper(String value, boolean explicitlySet) {
        this.value = value;
        this.explicitlySet = explicitlySet;
    }

    public String getValue() {
        return value;
    }

    public boolean isExplicitlySet() {
        return explicitlySet;
    }

    public static ShowNullWrapper of(String value) {
        return new ShowNullWrapper(value, true);
    }

    public static ShowNullWrapper absent() {
        return new ShowNullWrapper(null, false);
    }
}
