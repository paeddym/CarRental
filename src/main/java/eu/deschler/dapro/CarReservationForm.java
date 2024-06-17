package eu.deschler.dapro;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.notification.Notification;

public class CarReservationForm extends FormLayout {
    private int carID;
    private IntegerField customer_no = new IntegerField("Kundennummer");
    private DatePicker reservationStart = new DatePicker("Reservierungsbeginn");
    private DatePicker reservationEnd = new DatePicker("Reservierungsende");
    private Button saveButton = new Button("Speichern");
    private CarDao dao = CarDao.getInstance();

    public CarReservationForm(){
        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton);
        setVisible(false);

        customer_no.setRequiredIndicatorVisible(true);
        reservationStart.setRequired(true);
        reservationEnd.setRequired(true);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> save());

        add(customer_no, reservationStart, reservationEnd, buttonsLayout);
    }

    public void startReservation(Car car) {
        carID = car.getId();
        setVisible(true);
    }

    private void save() {
        if(customer_no.getValue() == null) {
            showNotification("Bitte geben Sie eine Kundennummer ein!", NotificationVariant.LUMO_ERROR);
            return;
        }
        if(dao.isReservationValid(carID, customer_no.getValue(), reservationStart.getValue(), reservationEnd.getValue())) {
            dao.reserve(carID, customer_no.getValue(), reservationStart.getValue(), reservationEnd.getValue());
            showNotification("Reservierung erfolgreich!", NotificationVariant.LUMO_SUCCESS);
            setVisible(false);
        }
    }
    
    public static void showNotification(String message, NotificationVariant variant) {
        Notification notification = Notification
                .show(message);
        notification.addThemeVariants(variant);
    }
}
