public class User {
    private String name;
    private String address;

    public User(String name, String address) {
        this.name = name;
        this.address = address;
    }
    public void edit(String name, String address){
        setName(name);
        setAddress(address);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
