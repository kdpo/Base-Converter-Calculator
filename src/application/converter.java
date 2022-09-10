package application;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField; 
import javafx.fxml.Initializable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/** 
 * This class implements a Base Converter Calculator; It can convert any number from and to binary, decimal, octal, or hexadecimal.
 * It computes the sum and product of two numbers and converts it into different bases.
 * @author Klowee Po
 * @version 1.0
 */
public class converter implements Initializable {
	@FXML
    private Button submit, back;

	@FXML
	ChoiceBox<Integer> cb1, cb2, cb3, cb4;

	@FXML
	private TextField input1, input2;
	
	@FXML
	private TextArea sumTxt, productTxt;
	
	private Integer[] choices = {10, 2, 8, 16};	
	private BigInteger sum, product;
	
	private Map<Integer, String> sMap; 
	private Map<Integer, String> pMap;
    
	private ArrayList<Integer> list = new ArrayList<Integer>();
	private InputValidation validate = new InputValidation();

	/**
	 * This constructor initializes the hashmaps that will contain the sum and 
	 * product in different bases of the two entered numbers. The keys are 
	 * "10, 2, 8, and 16" which are Strings. The values are the sum/product
	 * in that base system.
	 * @param sMap contains the sum in binary, decimal, octal, or hexadecimal.
	 * @param pMap contains the product in binary, decimal, octal, or hexadecimal.
	 */
	public converter() {
		this.sMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;
		{
        	put(choices[0], "");
        	put(choices[1], "");
        	put(choices[2], "");
        	put(choices[3], "");
    	}};
	    this.pMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;
		{
        	put(choices[0], "");
        	put(choices[1], "");
        	put(choices[2], "");
        	put(choices[3], "");
    	}};
	}

	/**
	 * This initializes the default look of the JavaFX application. It sets the 
	 * check box choice as "base 10". It also listens to the user input; For example, 
	 * when the user selects the choice "base 2" in the sum/product, the application
	 * will automatically change the view and set the sum/product in "base 2". 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
        Collections.addAll(list, choices);
        ObservableList<Integer> obList = FXCollections.observableList(list);
              
        cb1.getItems().clear();
        cb1.setItems(obList);
        cb1.setValue(choices[0]);
        cb2.getItems().clear();
        cb2.setItems(obList);
        cb2.setValue(choices[0]);
        cb3.getItems().clear();
        cb3.setItems(obList);
        cb3.setValue(choices[0]);
        cb4.getItems().clear();
        cb4.setItems(obList);
        cb4.setValue(choices[0]);
        
        cb3.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
            	sumTxt.setText(sMap.get(list.get(number2.intValue())));
            }
          });
        
        cb4.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
            	productTxt.setText(pMap.get(list.get(number2.intValue())));
              }
          });
	}
	
	/**
	 * This method connects all the other methods together and is triggered whenever 
	 * the user clicks the solve button. It checks the user input, adds and multiplies the two numbers, 
	 * and replaces the content of the HashMaps with its appropriate value. 
	 * Hence, it refreshes the hashmaps at every click of the button.
	 * @param event is an ActionEvent that is connected to the "solve" button. 
	 */
	@FXML
	public void solve(ActionEvent event) {
		
		try {
			String text1 = validate.checkInput(input1.getText(), cb1.getValue());
			String text2 = validate.checkInput(input2.getText(), cb2.getValue());	
			BigInteger x = convertToDecimal(text1, cb1.getValue());
			BigInteger y = convertToDecimal(text2, cb2.getValue());
		
			sum = x.add(y);
			product = x.multiply(y);
			
			sMap.replace(choices[0], String.valueOf(sum));
			sMap.replace(choices[1], convertDecimalToBase(sum, choices[1]));
			sMap.replace(choices[2], convertDecimalToBase(sum, choices[2]));
			sMap.replace(choices[3], convertDecimalToBase(sum, choices[3]));
			
			pMap.replace(choices[0], String.valueOf(product));
			pMap.replace(choices[1], convertDecimalToBase(product, choices[1]));
			pMap.replace(choices[2], convertDecimalToBase(product, choices[2]));
			pMap.replace(choices[3], convertDecimalToBase(product, choices[3]));
			
			sumTxt.setText(sMap.get(cb3.getValue()));
			productTxt.setText(pMap.get(cb4.getValue()));
		} catch (Exception e) {
			for (int index = 0; index < 4; index++) {
				sMap.replace(choices[index], e.getMessage());
				pMap.replace(choices[index], e.getMessage());
			}
			sumTxt.setText(e.getMessage());
			productTxt.setText(e.getMessage());
		}
	}
	
	/**
	 * This method converts a decimal number and to a given base: either binary, decimal, octal, or hexadecimal.
	 * The decimal is continuously divided by the given base (2, 8, 16, etc.) while appending every remainder in a StringBuilder.
	 * The quotient and remainder is stored in a BigInteger array, where the zeroth index stores the quotient
	 * and the first index stores the remainder. This is done until the quotient finally reaches zero.
	 * Note that the numbers in the remainder are first converted into its letter form before being appended. (e.g. 10 into A).
	 * If the number is converted into binary, zeroes will be appended so that the length of result will be divisible by 8.
	 * Spaces will also be padded after every 8 digits. If the number is converted into hexadecimal, zeroes will be appended 
	 * so that the length of result will be divisible by 4. Spaces will also be padded after every 4 digits. 
	 * Lastly, the remainder will be reversed.
	 * @param decimal is the number to be converted. It is a BigInteger so that it can accommodate very large numbers.
	 * @param base is the given base that the number will be converted into.
	 * @return a numerical String which is the decimal number converted into a preferred base.
	 */
	public String convertDecimalToBase(BigInteger decimal, int base) {
		StringBuilder remainder = new StringBuilder();
		BigInteger[] r;
		
		while(decimal != BigInteger.ZERO) {
			r = decimal.divideAndRemainder(BigInteger.valueOf(base));
			remainder.append(numberstoLetters(r[1].intValue(), base));
			decimal = r[0];
		}
		
		if (base == 2) {
			while(remainder.length()%8 != 0) {
				remainder.append("0");
			}
			
			int length = 8;
			while(length < remainder.length()) {
				remainder.insert(length, " ");
				length +=9;
			}
		}
		
		else if (base == 16) {
			while(remainder.length()%4 != 0) {
				remainder.append("0");
			}
			
			int length = 4;
			while(length < remainder.length()) {
				remainder.insert(length, " ");
				length +=5;
			}
		}
		
		remainder.reverse();
		return remainder.toString();
	}
	
	/**
	 * This method converts the input from any base into a decimal. First, it removes all spaces before proceeding.
	 * The string is reversed - so that the position of each character will represent the power of the base.
	 * The input will be converted from String to BigInteger. Each digit is multiplied to its base raised to the positional power.
	 * Then, everything is summed up. 
	 * @param input is the numerical string to be converted into decimal
	 * @param base the base of the input
	 * @return decimal-converted input in BigInteger type. 
	 */
	public BigInteger convertToDecimal(String input, int base) {
		StringBuilder sb = new StringBuilder(input.replaceAll("\\s", ""));
		sb.reverse();
		String org = sb.toString();
		
		BigInteger decimal = new BigInteger("0");
		BigInteger Base = new BigInteger(String.valueOf(base));
		for (int i=0; i<org.length(); i++) {
			BigInteger Digit = new BigInteger(lettersToNumbers(org.charAt(i)));
			decimal = decimal.add(Digit.multiply(Base.pow(i))); 
		}		
		return decimal;
	}
	
	/**
	 * This method converts the letters into its corresponding numerical format.
	 * @param c is the alphabetical character to be converted.
	 * @return a converted character.
	 */
	private String lettersToNumbers(char c) {
		if (c >= '0' && c <= '9')
		    return String.valueOf(c);
		else
		    return String.valueOf(c - 'A' + 10);
	}
	
	/**
	 * This method converts the numbers into its lexical format (e.g. 10 to A).
	 * @param digit is the number to be converted.
	 * @param base is the base of the number to be converted.
	 * @return a lexically converted number.
	 */
	public char numberstoLetters(int digit, int base) {
		return Character.toUpperCase(Character.forDigit(digit, base));
	}
}