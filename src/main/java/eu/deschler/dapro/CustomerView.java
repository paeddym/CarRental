package eu.deschler.dapro;

import java.util.List;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.component.button.Button;


public class CustomerView extends VerticalLayout {
    private static final long serialVersionUID = -70572001777680901L;
    private CustomerDao dao = CustomerDao.getInstance();
    private Grid<Customer> grid = new Grid<>();
    private TextField filterText = new TextField();
    private CustomerForm form = new CustomerForm(this);

    private DataProvider<Customer, Void> dp =
        DataProvider.fromCallbacks(
            // First callback fetches items based on a query
            query -> {
                // The index of the first item to load
                int offset = query.getOffset();

                // The number of items to load
                int limit = query.getLimit();

                List<Customer> persons = dao
                        .fetchCustomers(offset, limit);

                return persons.stream();
            },
            // Second callback fetches the number of items
            // for a query
            query -> dao.getCustomerCount());

    public CustomerView() {

        filterText.setPlaceholder("Suche nach Name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList());
        grid.setDataProvider(dp);
        grid.addColumn(Customer::getCustomerNo).setHeader("Kundennr.");
        grid.addColumn(Customer::getLastName).setHeader("Name");
        grid.addColumn(Customer::getFirstName).setHeader("Vorname");
        grid.addColumn(new LocalDateRenderer<>(Customer::getDateOfBirth, "dd.MM.yyyy")).setHeader("Geburtsdatum");
        //grid.setWidthFull();
        grid.asSingleSelect().addValueChangeListener(e ->
            form.setCustomer(grid.asSingleSelect().getValue())
        );
        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();
        grid.setWidthFull();


        // Hinzufügen des "Neuen Kunden anlegen"-Buttons
        Button addButton = new Button("Neuen Kunden anlegen");
        addButton.addClickListener(event -> {
            form.setCustomer(new Customer()); // Leeres Customer-Objekt für neuen Kunden
            form.setVisible(true); // Kundenformular anzeigen
        });

        // Vertikales Layout für die gesamte Ansicht
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(filterText, addButton, mainContent);

        add(filterText, mainContent);
        updateList();
        form.setCustomer(null);
        add(verticalLayout);
    }

    public void updateList() {
        dao.setFilter(filterText.getValue());
        dp.refreshAll();
        //grid.setItems(service.fetchCustomers(filterText.getValue()));
    }
}
