package models;

public class TestModel {

    private String name;
    private boolean isValid;

    public TestModel(String name, boolean isValid) {
        this.name = name;
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
