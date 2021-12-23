package fr.josso.fractales.Core;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public class RegexValidator implements ChangeListener<String> {
    private final Pattern authorizedChars;
    private final Pattern validator;
    private final TextField textField;

    public RegexValidator(Pattern authorizedChars, Pattern validator, TextField textField) {
        this.authorizedChars = authorizedChars;
        this.validator = validator;
        this.textField = textField;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!authorizedChars.matcher(newValue).matches() && !newValue.equals("")) {
            ((StringProperty) observable).setValue(oldValue);
            return;
        }

        if (!validator.matcher(newValue).matches() && !newValue.equals("")){
            textField.setStyle("-fx-background-color: red;");
        } else {
            textField.setStyle(null);
        }
    }
}
