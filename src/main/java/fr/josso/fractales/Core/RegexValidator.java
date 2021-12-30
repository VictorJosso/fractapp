package fr.josso.fractales.Core;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;

/**
 * The type Regex validator.
 */
public record RegexValidator(Pattern authorizedChars, Pattern validator,
                             TextField textField) implements ChangeListener<String> {

    /**
     * Set the background of the field in red if invalid.
     * @param observable the observed field.
     * @param oldValue the old value.
     * @param newValue the new value.
     */
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
