import static java.lang.System.*;
import java.util.*;
import java.io.*;

public class Database {
	private static entry[] Database;
	private static String[] DatabaseKeys;
	public static int databaseLength = 0;
	public static char Primarydivider = 0x00, ParentDivider = 0x01, ParentDataDivider = 0x02, childrenDelimiter = ',';
	// example entries
	// String FamilyEntry = "Family Name,address,zip
	// code,homephone,2,name~Work~WorkNumber~cellNumber~Email~Birthday:name~Work~WorkNumber~cellNumber~Email~Birthday,anniversary
	// date,child1~child2~child3";
	// String Singlehaskids = "Family name,address,zip
	// code,homephone,1,name~Work~WorkNumber~cellNumber~Email~Birthday,child1~child2~child3";
	// String singlenokids = "Family name,address,zip
	// code,homephone,1,name~Work~WorkNumber~cellNumber~Email~Birthday";

	// String corseFamily = "Corse,14001 Hwy 152,Pampa Texas 79065,

	public static boolean loadDatabase(String file) throws IOException {

		try {
			if (!new File(file).exists())
				return false; // returns if there is no database file
		} catch (NoSuchElementException err) {
			return false;
		} catch (NullPointerException err) {
			return false;
		}
		Scanner dbFile = new Scanner(new File(file));
		int length = 0;
		while (dbFile.hasNextLine()) { // getting the length of the database
			dbFile.nextLine();
			length++;
		}

		dbFile = new Scanner(new File(file));

		Database = new entry[length]; // initalizing database and databasekeys
		DatabaseKeys = new String[length];
		databaseLength = length;

		for (int index = 0; index < length; index++) {

			String temp = dbFile.nextLine();

			out.println("(Database.loadDatabase) Loading Entry[" + index + "] with info read as [" + temp + "]");

			Database[index] = new entry(temp);
			DatabaseKeys[index] = Database[index].getKey().toLowerCase();
		}
		Arrays.sort(Database);

		dbFile.close();

		return true;
	}

	public static void printDatabase() {
		out.println("(Database.printDatabase) Printing Database --------------------");
		try {
			for (int index = 0; index < Database.length; index++) {
				out.println("| [ " + Database[index].gitInfo() + " ]");
			}
		} catch (NullPointerException err) {
		}
		out.println("--------------- End Of (Database.printDatabase) ---------------\n");
	}

	public static entry getEntry(int index) {
		return Database[index];
	}

	public static boolean addEntry() {
		entry[] temp = new entry[Database.length];
		for (int i = 0; i < Database.length; i++) {
			temp[i] = new entry(Database[i].gitInfo());
		}
		Database = new entry[temp.length + 1];
		for (int i = 0; i < temp.length; i++) {
			Database[i] = new entry(temp[i].gitInfo());
		}

		Database[Database.length - 1] = new entry(
				Primarydivider + "" + Primarydivider + "" + Primarydivider + "" + Primarydivider + "1" + Primarydivider
						+ "" + ParentDataDivider + "" + ParentDataDivider + "" + ParentDataDivider + ""
						+ ParentDataDivider + "" + ParentDataDivider + "" + Primarydivider + "" + Primarydivider);
		databaseLength++;
		return true;
	}

	public static int getLastIndex() {
		return Database.length - 1;
	}

	public static boolean saveDatabase() {
		try {
			if (Database.length == 0) {
				return false;
			}
			FileWriter output = new FileWriter(new File("Database.dat"));

			for (entry item : Database) {
				output.write(item.gitInfo() + "\n");
			}

			output.close();
			return true;
		} catch (Exception err) {
			return false;
		}
	}
}