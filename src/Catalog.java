import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Catalog {

	public static final String SEP = "        ";
	private final String filepath;
	private BPlusTree<String, String> db;

	public Catalog() {
		this("partfile.txt");
	}

	public Catalog(String filepath) {
		this.filepath = filepath;
		System.out.println("Initializing B+ Tree...");

		db = new BPlusTree<>();

		ArrayList<Pair<String, String>> data = loadData();

		for (Pair<String, String> pair : data) {
			db.insert(pair.getKey(), pair.getValue());
		}
	}

	private ArrayList<Pair<String, String>> loadData() {
		System.out.println("Loading data from flat file " + filepath + "...");
		ArrayList<Pair<String, String>> data;

		File file = new File(filepath);
		try {
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String st;
				String[] temp;
				StringBuilder sb;
				data = new ArrayList<>();
				while ((st = br.readLine()) != null) {
					temp = st.split(SEP);
					sb = new StringBuilder();

					for (int i = 1; i < temp.length; i++) {
						sb.append(temp[i].trim());
						if (i < temp.length - 1) {
							sb.append("     ");
						}
					}

					data.add(new Pair<>(temp[0].trim(), sb.toString()));
				}
			}
			return data;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	public String search(String key) {
		return db.search(key);
	}

	public boolean add(String key, String value) {
		if(db.search(key) == null) {
			db.insert(key, value);
			return true;
		}
		return false;
	}

	public boolean update(String key, String newVal) {
		if (db.search(key) == null) {
			return false;
		}
		
		return db.update(key, newVal);
	}

	public boolean delete(String key) {
		if (db.search(key) == null) {
			return false;
		}
		
		db.delete(key);
		return true;
	}

	public void getNextTen(String key) {
		LeafNode<String, String> leafNode = db.find(key);
		if (leafNode == null) {
			System.out.println("Part with Key="+key+" Not Found");
			return;
		}

		int count = 0;
		boolean found = false;

		for (int i = 0; i < leafNode.getKeysSize(); i++) {
			String k = leafNode.getKey(i);
			if (key.equals(k)) {
				found = true;
			}

			if (found == true && count < 10) {
				System.out.println(k + " : " + leafNode.getValue(i));
				count++;
			}

			if (count == 10) {
				return;
			}
		}

		leafNode = leafNode.next;
		while (found == true && count < 10 && leafNode != null) {
			for (int i = 0; i < leafNode.getKeysSize(); i++) {
				String k = leafNode.getKey(i);
				if (count < 10) {
					System.out.println(k + " : " + leafNode.getValue(i));
					count++;
				}

				if (count == 10) {
					return;
				}
			}
			leafNode = leafNode.next;
		}
	}

	public ArrayList<Pair<String, String>> toArrayList() {
		return db.toArrayList();
	}

	public void save() {
		ArrayList<Pair<String, String>> data = toArrayList();
		dumpData(data, this.filepath);
	}

	private void dumpData(ArrayList<Pair<String, String>> data, String filepath) {
		try {
			File file = new File(filepath);
			if (file.createNewFile()) {
				System.out.println("New file created: " + file.getName());
			} else {
				System.out.println("File already exists, Overwritting previous content.");
			}

			try (FileWriter writer = new FileWriter(file)) {

				for (Pair<String, String> pair : data) {
					writer.write(pair.getKey() + SEP + pair.getValue() + "\n");
				}

			}

			System.out.println("File Saved.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public void showStats() {
		db.showStats();
	}

	public static void main(String[] args) {
		String key, value;

		Catalog catalog = new Catalog("partfile.txt");

		try (Scanner sc = new Scanner(System.in)) {
			System.out.println("A: Add a new part");
			System.out.println("U: Update a part");
			System.out.println("D: Delete a part");
			System.out.println("L: Search a part");
			System.out.println("P: Print next 10 parts");
			System.out.println("S: Show Stats");
			System.out.println("Q: Quit");
			System.out.print("Select from above options: ");

			String choice = sc.nextLine().toUpperCase();

			while (!choice.equals("Q")) {
				switch (choice) {
					case "A":
						System.out.println("ADD Option Selected");
						System.out.print("Enter the Part Number: ");
						key = sc.nextLine();

						System.out.print("Enter the Part Description: ");
						value = sc.nextLine();

						if(catalog.add(key, value) == true) {
							System.out.println("Added a new part >> " + key + " : " + value);
						} else {
							System.out.println("Part with Key=" + key + " Already Exists.");
						}
						break;
					case "U":
						System.out.println("UPDATE Option Selected");
						System.out.print("Enter the Part Number to Update: ");
						key = sc.nextLine();

						System.out.print("Enter the New Part Description: ");
						value = sc.nextLine();
						if (catalog.update(key, value) == true) {
							System.out.println("Part with Key=" + key + " is Updated");
						} else {
							System.out.println("Part with Key=" + key + " Not Found");
						}
						break;
					case "L":
						System.out.println("LOOKUP Option Selected");
						System.out.print("Enter the Part Number to Search: ");
						key = sc.nextLine();
						value = catalog.search(key);
						if (value != null) {
							System.out.println("Part Description: " + value);
						} else {
							System.out.println("Part with Key=" + key + " Not Found");
						}
						break;
					case "D":
						System.out.println("DELETE Option Selected");
						System.out.print("Enter the Part Number to Delete: ");
						key = sc.nextLine();
						if (catalog.delete(key) == true) {
							System.out.println("Successfully deleted Key=" + key);
						} else {
							System.out.println("Part with Key=" + key + " Not Found");
						}
						break;
					case "P":
						System.out.println("NEXT 10 Option Selected");
						System.out.print("Enter the Part Number: ");
						key = sc.nextLine();
						catalog.getNextTen(key);
						break;
					case "S":
						catalog.showStats();
						break;
					case "Q":
						break;
					default:
						System.out.println("Invalid Option");

				}
				System.out.print("\nEnter the choice [A/U/D/L/P/S/Q]: ");
				choice = sc.nextLine().toUpperCase();
			}

			System.out.print("\nDo you want to save the data [Y/N]? ");
			choice = sc.nextLine();
			while (!choice.equalsIgnoreCase("Y") && !choice.equalsIgnoreCase("N")) {
				System.out.print("Invalid Choice. Do you want to save the data [Y/N]? ");
				choice = sc.nextLine();
			}

			if (choice.equalsIgnoreCase("Y")) {
				System.out.println("Saving...");
				catalog.save();
			} else {
				System.out.println("Exiting without Saving.");
			}

			System.out.println();
			catalog.showStats();
			System.out.println();
		}
	}

}
