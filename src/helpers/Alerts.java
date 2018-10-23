package helpers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

/**
 * This is a helper class that shows user appropiate alerts
 */
public class Alerts {

    /**
     * This method shows the user alert depending on the inputs
     * @param alertText
     * @param firstButton
     * @param secondButton
     * @return
     */
    public static Alert show(String alertText, ButtonType firstButton, ButtonType secondButton) {
        Alert alert = null;
        if (secondButton == null) {
            alert = new Alert(Alert.AlertType.INFORMATION, alertText, firstButton);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION, alertText, firstButton, secondButton);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
        return alert;
    }
}
