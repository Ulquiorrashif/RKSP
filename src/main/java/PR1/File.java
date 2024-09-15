package PR1;

public class File {
    private  String type;
    private int size;

    public File(String type, int size) {
        this.type = type;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }
}
