package eu.deschler.dapro;

import java.util.List;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.component.button.Button;

public class CarView extends VerticalLayout {
    private static final long serialVersionUID = -5150635303857129026L;
    private CarDao dao = CarDao.getInstance();
    private Grid<Car> grid = new Grid<>();
    private CarSearchForm form = new CarSearchForm(this);
    private CarReservationForm reservationForm = new CarReservationForm(this);

    private DataProvider<Car, Void> dp =
            DataProvider.fromCallbacks(
                    query -> {
                        int offset = query.getOffset();

                        int limit = query.getLimit();

                        List<Car> cars = dao
                                .fetchCars(offset, limit);

                        return cars.stream();
                    },

                    query -> dao.getCarCount());

    public CarView() {

        grid.setDataProvider(dp);
        grid.addColumn(Car::getId).setHeader("ID");
        grid.addColumn(Car::getBezeichnung).setHeader("Bezeichnung");
        grid.addColumn(Car::getHersteller).setHeader("Hersteller");
        grid.addColumn(Car::getAutoart).setHeader("Autoart");
        grid.addColumn(Car::getSitzplaetze).setHeader("Sitzplätze");
        grid.addColumn(Car::getKw).setHeader("KW");
        grid.addColumn(Car::getTreibstoff).setHeader("Treibstoff");
        grid.addColumn(Car::getPreisTag).setHeader("Preis/Tag");
        grid.addColumn(Car::getPreisKm).setHeader("Preis/Km");
        grid.addColumn(Car::getAchsen).setHeader("Achsen");
        grid.addColumn(Car::getLadeVolumen).setHeader("Ladevolumen");
        grid.addColumn(Car::getZuladung).setHeader("Zuladung");
        grid.addColumn(Car::getFuehrerschein).setHeader("Führerscheinklassen");

        HorizontalLayout mainContent = new HorizontalLayout(grid, form, reservationForm);
        mainContent.setSizeFull();
        grid.setWidthFull();

        grid.asSingleSelect().addValueChangeListener(event -> {
            Car selectedCar = event.getValue();
            form.setVisible(false);
            reservationForm.startReservation(selectedCar);
        });

        Button searchButton = new Button("Fahrzeug suchen");
        searchButton.addClickListener(event -> {
            form.setVisible(true);
            reservationForm.setVisible(false);
        });

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(mainContent, searchButton);

        add(mainContent, verticalLayout);
    }

    public void updateList() {
        dp.refreshAll();
    }
}
