package eu.deschler.dapro;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.component.notification.Notification;

public class CarReservationForm extends FormLayout {
    private IntegerField customerNo = new IntegerField("Kundennummer");
    private int carID;
    private DatePicker reservationStart = new DatePicker("Reservierungsbeginn");
    private DatePicker reservationEnd = new DatePicker("Reservierungsende");
    private Button saveButton = new Button("Speichern");
    private CarDao dao = CarDao.getInstance();

    public CarReservationForm(CarView carView){
        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton);
        setVisible(false);

        customerNo.setRequiredIndicatorVisible(true);
        reservationStart.setRequired(true);
        reservationEnd.setRequired(true);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> save());

        add(customerNo, reservationStart, reservationEnd, buttonsLayout);
    }

    public void startReservation(Car car) {
        carID = car.getId();
        setVisible(true);
    }

    private void save() {
        if(customerNo.getValue() == null) {
            showNotification("Bitte geben Sie eine Kundennummer ein!", NotificationVariant.LUMO_ERROR);
            return;
        }
        if(dao.isReservationValid(carID, customerNo.getValue(), reservationStart.getValue(), reservationEnd.getValue())) {
            dao.reserve(carID, customerNo.getValue(), reservationStart.getValue(), reservationEnd.getValue());
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
