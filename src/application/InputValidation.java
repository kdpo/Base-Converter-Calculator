package application;

/**
 * This class is for validating the user's input.
 * @author Klowee Po
 *
 */
public class InputValidation {
	public String text;
	
	/**
	 * This method validates the user input and raises a customized Exception whenever there is an error in the input.
	 * If the base of the user input is 10, it makes sure that the input only contains the digits 0-9.
	 * If the base of the user input is 2, it makes sure that the input only contains the digits 0-1.
	 * If the base of the user input is 8, it makes sure that the input only contains the digits 0-7.
	 * If the base of the user input is 16, it makes sure that the input only contains the digits 0-9 and characters A-F.
	 * @param text is the user input.
	 * @param base is the base of the user input.
	 * @return The original input with no spaces and capitalized letters. 
	 * @throws InputErrorException raised whenever there is an unacceptable input.
	 */
	public String checkInput(String text, int base) throws InputErrorException {
		if (base == 10) {
			if (text.matches("[0-9 ]+")) {
				text = text.replaceAll("\\s+","");
			}	
			else throw new InputErrorException("Invalid Input!");
		}
		
		else if (base == 2) {
			if (text.matches("[0-1 ]+")) {
				text = text.replaceAll("\\s+","");
			}
			else throw new InputErrorException("Invalid Input!");
		}
		
		else if (base == 8) {
			if (text.matches("[0-7 ]+")) {
				text = text.replaceAll("\\s+","");
			}
			else throw new InputErrorException("Invalid Input!");
		}
		
		else if (base == 16) {
			if (text.matches("[A-Fa-f0-9 ]+")) {
				text = text.toUpperCase().replaceAll("\\s+","");
			}
			else throw new InputErrorException("Invalid Input!");
		}	
		return text;
	}
}
