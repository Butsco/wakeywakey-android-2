package dance.wakeywakey;

import java.util.Comparator;

/**
 * Created by bert on 18/10/14.
 */
public class NameComparator implements Comparator<MyContact> {
    @Override
    public int compare(MyContact o1, MyContact o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
