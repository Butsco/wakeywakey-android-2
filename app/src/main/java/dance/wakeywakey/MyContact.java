package dance.wakeywakey;

import java.util.HashSet;

/**
 * Created by bert on 18/10/14.
 */
final public class MyContact {
    public int id;
    public String firstName = "";
    public String lastName = "";
    public HashSet<String> tels = new HashSet<String>();

    public String getName() {
        if (firstName == null || firstName.equals("")) {
            return lastName;
        } else if (lastName == null || lastName.equals("")) {
            return firstName;
        }
        return firstName + " " + lastName;
    }
}

