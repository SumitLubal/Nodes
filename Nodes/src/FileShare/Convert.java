package FileShare;
import java.util.Scanner;

public class Convert {

	private static class Conversion {

		public String getConversion(int inchInput) {
			int yards = (inchInput - (inchInput % 36)) / 36;
			int feet = (inchInput % 36) - ((inchInput % 36) % 12);
			int inches = (inchInput % 36) % 12;

			return yards + "yards, " + feet + "feet, and " + inches + "inches.";
		
		}
		

	} // end of class Conversion

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int inchInput;
		Conversion conversion;
		conversion = new Conversion();

		// prompt
		System.out.println("Please enter an amout of inches (integer): ");
		inchInput = scanner.nextInt();
		String output = conversion.getConversion(inchInput);
	} // end of method main()
	
	

} // enf of class Convert