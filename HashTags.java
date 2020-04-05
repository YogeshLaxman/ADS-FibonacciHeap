import java.util.Comparator;

public class HashTags {
    String name;
    int frequency;

    public HashTags(String name, int frequency) {
        this.name = name;
        this.frequency = frequency;
    }
}

class HashtagComparator implements Comparator<HashTags> {
    @Override
    public int compare(HashTags o1, HashTags o2) {
        return o1.frequency - o2.frequency;
    }
}
