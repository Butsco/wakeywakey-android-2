package dance.wakeywakey;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.apache.commons.lang3.StringUtils;

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

    public String getFirstMsisdn() {
        for (String number : tels) {
            try {
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                Phonenumber.PhoneNumber phoneNumber = null;
                phoneNumber = phoneUtil.parse(number, "BE");
                boolean isValid = phoneUtil.isValidNumber(phoneNumber);
                if (isValid) {
                    number = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                }

                return StringUtils.substring(number, 1);
            } catch (NumberParseException e) {
                e.printStackTrace();
            }
        }

        return "";
    }
}

