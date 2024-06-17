package eu.deschler.dapro;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class CarSearchForm extends FormLayout {
    private CarView carView;

    private TextField bezeichnung = new TextField("Bezeichnung");
    private TextField hersteller = new TextField("Hersteller");
    private IntegerField autoart = new IntegerField("Autoart");
    private IntegerField sitzplaetze = new IntegerField("Sitzplätze");
    private TextField treibstoff = new TextField("Treibstoffart");
    private Button search = new Button("Suchen");
    private Binder<Car> binder = new Binder<>(Car.class);
    private CarDao dao = CarDao.getInstance();

    public CarSearchForm(CarView carView) {
        this.carView = carView;
        HorizontalLayout buttons = new HorizontalLayout(search);
        setVisible(false);
        search.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        //autoart.setValue(1);
        autoart.setHasControls(true);
        autoart.setStep(1);
        autoart.setMin(1);
        autoart.setMax(7);

        //sitzplaetze.setValue(1);
        sitzplaetze.setHasControls(true);
        sitzplaetze.setStep(1);
        sitzplaetze.setMin(1);
        sitzplaetze.setMax(6);

        add(bezeichnung, hersteller, autoart,sitzplaetze, treibstoff, buttons);

        binder.bindInstanceFields(this);

        binder.setBean(new Car()); // Set a new Car object as the bean

        search.addClickListener(event -> {
            String filter = "";
            boolean isFirst = true;

            if (binder.getBean().getBezeichnung() != null && !binder.getBean().getBezeichnung().isEmpty()) {
                filter += " WHERE Bezeichnung LIKE '%" + binder.getBean().getBezeichnung() + "%'";
                isFirst = false;
            }
            if (binder.getBean().getHersteller() != null && !binder.getBean().getHersteller().isEmpty()) {
                if (isFirst) {
                    filter += " WHERE Hersteller LIKE '%" + binder.getBean().getHersteller() + "%'";
                    isFirst = false;
                } else {
                    filter += " AND Hersteller LIKE '%" + binder.getBean().getHersteller() + "%'";
                }
            }
            if (binder.getBean().getAutoart() != 0) {
                if (isFirst) {
                    filter += " WHERE Autoart = " + binder.getBean().getAutoart();
                    isFirst = false;
                } else {
                    filter += " AND Autoart = " + binder.getBean().getAutoart();
                }
            }
            if (binder.getBean().getSitzplaetze() != 0) {
                if (isFirst) {
                    filter += " WHERE Sitzplaetze = " + binder.getBean().getSitzplaetze();
                    isFirst = false;
                } else {
                    filter += " AND Sitzplaetze = " + binder.getBean().getSitzplaetze();
                }
            }
            if (binder.getBean().getTreibstoff() != null && !binder.getBean().getTreibstoff().isEmpty()) {
                if (isFirst) {
                    filter += " WHERE Treibstoff LIKE '%" + binder.getBean().getTreibstoff() + "%'";
                } else {
                    filter += " AND Treibstoff LIKE '%" + binder.getBean().getTreibstoff() + "%'";
                }
            }
            dao.setFilter(filter);
            carView.updateList();
        });
    }
}