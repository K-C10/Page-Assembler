import static java.lang.System.*; //temp
import java.util.*;

public class entry implements Comparable<entry> {
    private String[] HeadingData = new String[4];
    private String[] fatherData = new String[6];
    private String[] motherData = new String[6];
    private String anniversary;
    private String[] children = new String[0];
    private int familyType = 0;

    /*
     * father/mother data format
     * [0] Name (BOLD)
     * [1] Work
     * [2] Work Number
     * [3] Cell Number
     * [4] Email
     * [5] BirthDay Month/day
     * 
     * children
     * [*] Name Birthdate(Month/Day)
     */
    // data format
    // DATA:coord~Data:coord

    /*
     * heading data format
     * [0] Family name
     * [1] Address
     * [2] ZipCode
     * [3] HomePhone
     * 
     * this.familyType
     * 2 = single
     * 3 = single with kids
     * 4 = couple
     * 5 = couple with kids
     * 
     */
    // entry format father data mother data
    // Family Name,address,zip
    // code,homephone,2,Work~WorkNumber~cellNumber~Email~Birthday:Work~WorkNumber~cellNumber~Email~Birthday,
    // anniversary date, list of children

    public entry(String input) {
        int count;
        out.println("(Entry.init) Entry given data " + input);

        for (count = 0; count < 4; count++) {
            HeadingData[count] = input.split(Character.toString(Database.Primarydivider))[count].trim();
        }
        // out.println(Arrays.toString(HeadingData));

        if (Integer.parseInt(input.split(Character.toString(Database.Primarydivider))[count++]) == 2) { // if there is
                                                                                                        // the presence
                                                                                                        // of mother and
                                                                                                        // father data,
                                                                                                        // father data
                                                                                                        // goes first =
                                                                                                        // 2, else 1 =
                                                                                                        // single
            // out.println("Complete Family");
            String temp = input.split(Character.toString(Database.Primarydivider))[count]
                    .split(Character.toString(Database.ParentDivider))[0];

            for (int i = count; i < count + 6; i++) { // loading father data
                try {
                    fatherData[i - count] = temp.split(Character.toString(Database.ParentDataDivider), -1)[i - count].trim();
                } catch (ArrayIndexOutOfBoundsException err) {
                } // very scary oneliner but is necessary
                out.println("this is " + (i - count));
            }
            this.familyType += 2;
            // out.println(Arrays.toString(fatherData));

            temp = input.split(Character.toString(Database.Primarydivider))[count]
                    .split(Character.toString(Database.ParentDivider))[1];

            for (int i = count; i < count + 6; i++) { // loading mother data
                try {
                    motherData[i - count] = temp.split(Character.toString(Database.ParentDataDivider))[i - count].trim();
                } catch (ArrayIndexOutOfBoundsException err) {
                    out.println("(entry.init) Error! Arrayindexoutofbounds with married person");
                }
            }
            this.familyType += 2;
            // out.println(Arrays.toString(motherData));
            out.println(Arrays.toString(input.split(Character.toString(Database.Primarydivider))) + " >< " + input);
            count++;
            anniversary = input.split(Character.toString(Database.Primarydivider), -1)[count];
            // out.println(anniversary);
        } else { // if person is single then we hijack the father data to use as anyperson data
                 // and keep the mother data NULL
            // out.println("Single person");

            String temp = input.split(Character.toString(Database.Primarydivider))[count];
            out.println(temp);
            for (int i = count; i < count + 6; i++) { // loading father data

                out.println("(entry.init) DEBUG DATA [Single person] -> "
                        + Arrays.toString(temp.split(Character.toString(Database.ParentDataDivider), -1)));
                try {
                    fatherData[i - count] = temp.split(Character.toString(Database.ParentDataDivider), -1)[i - count];
                } catch (ArrayIndexOutOfBoundsException err) {
                    out.println("(entry.init) Error! Arrayindexoutofbounds with single person");
                } // very scary oneliner but is necessary
            }
            this.familyType += 2;
            // out.println(Arrays.toString(fatherData));
        }

        count++;

        // children list loader
        // out.println(input.split(Character.toString(Database.Primarydivider)).length +
        // " != " + count);
        if (count != input.split(Character.toString(Database.Primarydivider)).length) { // checking to see if any list
                                                                                        // of kids are present to
                                                                                        // prevent array AOOB error
            this.familyType++;
            String temp = input.split(Character.toString(Database.Primarydivider), -1)[count];
            for (int i = 0; i < temp.split(Character.toString(Database.childrenDelimiter)).length; i++) { // loading
                                                                                                              // children
                                                                                                              // list
                children = Toolbox.addarrindex(children);
                children[i] = temp.split(Character.toString(Database.childrenDelimiter))[i].trim();
            }
            // out.println(Arrays.toString(children));
        }

        // out.println("\n\n");
        for (int index = 0; index < HeadingData.length; index++) {
            if (HeadingData[index].length() == 0)
                HeadingData[index] += " ";
        }

        out.println("(entry.init) Entry loaded\n");
    }

    public String gitInfo() { // function to package the data up to a single string to be saved
        String output = "";
        int count = 0;
        for (count = 0; count < 4; count++)
            output += HeadingData[count] + Character.toString(Database.Primarydivider);

        if (motherData[0] != null) {// if entry on family has both parents
            output += "2" + Database.Primarydivider;
            for (int i = count; i < count + 6; i++)
                output += fatherData[i - count] + "" + Character.toString(Database.ParentDataDivider);
            output = output.substring(0, output.length() - 1);
            output += Character.toString(Database.ParentDivider);

            for (int i = count; i < count + 6; i++)
                output += motherData[i - count] + "" + Character.toString(Database.ParentDataDivider);
            output = output.substring(0, output.length() - 1);
            output += Character.toString(Database.Primarydivider) + "" + anniversary + ""
                    + Character.toString(Database.Primarydivider);

        } else {
            output += "1" + Database.Primarydivider;
            for (int i = count; i < count + 6; i++)
                output += fatherData[i - count] + "" + Character.toString(Database.ParentDataDivider);
            output = output.substring(0, output.length() - 1);
            output += Character.toString(Database.Primarydivider);
        }

        for (int i = 0; i < children.length; i++)
            output += children[i] + "" + Database.childrenDelimiter;
        output = output.substring(0, output.length() - 1);

        return output;
    }

    public String getKey() {
        try {
        return HeadingData[0].substring(0, 1).toUpperCase()
                + HeadingData[0].substring(1, HeadingData[0].length()).toLowerCase();
        } catch(StringIndexOutOfBoundsException err) {
            return " ";
        }
    }

    // sorting magic to sort by the family's last name
    public int compareTo(entry input) {
        if (input.getKey().equals(this.getKey()))
            return 0;
        if (input.getKey().compareTo(this.getKey()) < 0)
            return 1;
        else
            return -1;
    }

    public boolean compareKey(String comKey) {

        if (HeadingData[0].equals(comKey))
            return true;
        return false;
    }

    public int getFamilyType() {
        return this.familyType;
    }

    public String getaddress() {
        return HeadingData[1];
    }

    public String getGeneralLocation() {
        return HeadingData[2];
    }

    public String getHomePhoneNumber() {
        return HeadingData[3];
    }

    public String[] getHeadingData() {
        return HeadingData;
    }

    public String[] getFatherData() {
        return fatherData;
    }

    public String[] getMotherData() {
        return motherData;
    }

    public String getAnniversary() {
        return anniversary;
    }

    public String[] getChildren() {
        return children;
    }

    // here is overwriting data position
    public boolean writeHeadingData(String[] input) {
        for (int i = 0; i < HeadingData.length; i++) {
            this.HeadingData[i] = input[i];
        }
        return true;
    }

    public boolean writeFatherData(String[] input) {
        for (int i = 0; i < this.fatherData.length; i++) {
            fatherData[i] = input[i];
        }
        return true;
    }

    public boolean writeMotherData(String[] input) {
        for (int i = 0; i < this.motherData.length; i++) {
            this.motherData[i] = input[i];
        }
        return true;
    }

    public boolean writeAnniversaryDate(String input) {
        anniversary = input;
        return true;
    }

    public boolean writeChildren(String[] input) {
        this.children = new String[input.length];
        for (int i = 0; i < input.length; i++) {
            this.children[i] = input[i];
        }
        return true;
    }

    public boolean writeFamType(int i) {
        familyType = i;
        return true;
    }

}
