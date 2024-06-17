package eu.deschler.dapro;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;

public class CarReservationForm extends FormLayout {
    private int carID;
    private IntegerField customer_no = new IntegerField("Kundennummer");
    private DatePicker reservationStart = new DatePicker("Reservierungsbeginn");
    private DatePicker reservationEnd = new DatePicker("Reservierungsende");
    private Button confirmButton = new Button("Reservieren");
    private CarDao dao = CarDao.getInstance();

    public CarReservationForm(){
        HorizontalLayout buttonsLayout = new HorizontalLayout(confirmButton);
        setVisible(false);

        customer_no.setRequiredIndicatorVisible(true);
        reservationStart.setRequired(true);
        reservationEnd.setRequired(true);
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(event -> confirm());

        add(customer_no, reservationStart, reservationEnd, buttonsLayout);
    }

    public void startReservation(Car car) {
        carID = car.getId();
        setVisible(true);
    }

    private void confirm() {
        if(customer_no.getValue() == null) {
            CarView.showNotification("Bitte geben Sie eine Kundennummer ein!", NotificationVariant.LUMO_ERROR);
            return;
        }
        if(dao.isReservationValid(carID, customer_no.getValue(), reservationStart.getValue(), reservationEnd.getValue())) {
            dao.reserve(carID, customer_no.getValue(), reservationStart.getValue(), reservationEnd.getValue());
            CarView.showNotification("Reservierung erfolgreich!", NotificationVariant.LUMO_SUCCESS);
            setVisible(false);
        }
    }
}
