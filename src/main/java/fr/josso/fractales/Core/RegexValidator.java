package fr.josso.fractales.Core;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public record RegexValidator(Pattern authorizedChars, Pattern validator,
                             TextField textField) implements ChangeListener<String> {

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!authorizedChars.matcher(newValue).matches() && !newValue.equals("")) {
            ((StringProperty) observable).setValue(oldValue);
            return;
        }

        if (!validator.matcher(newValue).matches() && !newValue.equals("")) {
            textField.setStyle("-fx-background-color: red;");
        } else {
            textField.setStyle(null);
        }
    }
}
