package org.wings.prpc.remote;

import java.util.Objects;

public class Attachment {

    private String name;
    private String type;
    private byte[] bytes;

    public Attachment(String name, String type, byte[] bytes) {
        this.name = name;
        this.type = type;
        this.bytes = bytes;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", bytes=" + bytes +
                '}';
    }
}
