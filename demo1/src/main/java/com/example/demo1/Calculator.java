package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Calculator implements Initializable {
    @FXML
    private TextField result;

    private LinkedList<String> numbers = new LinkedList<>();
    private LinkedList<String> operator = new LinkedList<>();

    private BigDecimal bigDecimal;
    Pattern pattern = Pattern.compile("\\d+|\\d.?\\d");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void pressNumberButton(ActionEvent event) {
        String number = getButtonValue(event);
        String textResult = result.getText();
        if (".".equals(number.trim())) {
            boolean contains = textResult.contains(".");
            if (contains || isBlank(textResult)) {
                return;
            } else {
                result.setText(textResult + ".");
                return;
            }
        }
        result.setText(result.getText() + number);
    }

    public void pressOperator(ActionEvent event) {
        String text = result.getText();

        if (!pattern.matcher(text).matches()) return;
        if (isBlank(text)) return;

        numbers.add(text);
        String buttonValue = getButtonValue(event);
        operator.add(buttonValue);
        result.setText("");
    }

    public void pressEqual(ActionEvent event) {
        boolean numbersEmpty = numbers.isEmpty();
        boolean operatorEmpty = operator.isEmpty();

        if (numbersEmpty || operatorEmpty) {
            result.setText(result.getText());
            return;
        }

        if (numbers.size() > 0) {
            numbers.add(result.getText());
            bigDecimal = new BigDecimal(numbers.getFirst());

            for (int i = 1; i < numbers.size(); i++) {
                switch (operator.get(i - 1)) {
                    case "+":
                        bigDecimal = bigDecimal.add(new BigDecimal(numbers.get(i)));
                        break;
                    case "-":
                        bigDecimal = bigDecimal.subtract(new BigDecimal(numbers.get(i)));
                        break;
                    case "/":
                        BigDecimal decimal = new BigDecimal(numbers.get(i));
                        int compareTo = decimal.compareTo(new BigDecimal(0));
                        if (compareTo == 0) {
                            result.setText("The divisor cannot be 0");
                            numbers.clear();
                            operator.clear();
                            return;
                        }
                        bigDecimal = bigDecimal.divide(new BigDecimal(numbers.get(i)), 20, BigDecimal.ROUND_UP);
                        break;
                    case "*":
                        bigDecimal = bigDecimal.multiply(new BigDecimal(numbers.get(i)));
                        break;
                }
            }
            result.setText(bigDecimal.stripTrailingZeros().toString());
            numbers.clear();
            operator.clear();
        }
    }

    public void pressClear(ActionEvent event) {
        result.setText("");
        operator.clear();
        numbers.clear();
    }

    public void pressBackspace(ActionEvent event) {
        String text = result.getText();
        if (text != null && text != "" && text.length() > 0) {
            text = text.substring(0, text.length() - 1);
            result.setText(text);
        }
    }

    private boolean isBlank(String text) {
        if (text != null && text != "" && text.length() > 0) {
            return false;
        }
        return true;
    }

    private String getButtonValue(ActionEvent event) {
        Button button = (Button)event.getSource();
        return button.getText();
    }

}
