package snnu.ljw.tags;

public class Tag {

    private String name;
    private String describ;

    public Tag(String name, String describ) {
        this.name = name;
        this.describ = describ;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getdescrib() {
        return describ;
    }

    public void setdescrib(String describ) {
        this.describ = describ;
    }

    public Tag() {
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                ", describ='" + describ + '\'' +
                '}';
    }
}
