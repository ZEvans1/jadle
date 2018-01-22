package models;

public class Foodtype {
    private String name;
    private int id;

    public Foodtype(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Foodtype foodtype = (Foodtype) o;

        if (id != foodtype.id) return false;
        return name.equals(foodtype.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id;
        return result;
    }
}
