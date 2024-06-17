package eu.deschler.dapro;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.component.checkbox.CheckboxGroup;

public class CustomerForm extends FormLayout{
    private static final long serialVersionUID = 1L;
    private CustomerView customerView;
    private TextField firstName = new TextField("Vorname");
    private TextField lastName = new TextField("Name");
    private DatePicker dateOfBirth = new DatePicker("Geburtsdatum");
    private CheckboxGroup<String> drivingLicenseClasses = new CheckboxGroup<>();
    private Button save = new Button("Speichern");
    private Button delete = new Button("Löschen");
    private Binder<Customer> binder = new Binder<>(Customer.class);
    private CustomerDao dao = CustomerDao.getInstance();

    public CustomerForm(CustomerView customerView) {
        this.customerView = customerView;
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(firstName, lastName, dateOfBirth,drivingLicenseClasses, buttons);

        drivingLicenseClasses.setLabel("Führerscheinklassen");
        drivingLicenseClasses.setItems("A", "B", "C", "D");
        binder.bindInstanceFields(this);

        save.addClickListener(event -> {
            if (binder.getBean().getId() == null) {
                binder.getBean().setCustomerNo(dao.getCustomerCount() + 1);
                saveNewCustomer();
            } else {
                updateCustomer();
            }
        });
        delete.addClickListener(event -> delete());
    }

    public void setCustomer(Customer customer) {
        binder.setBean(customer);

        if (customer == null) {
            setVisible(false);
        } else {
            setVisible(true);
            firstName.focus();
        }
    }

    private void saveNewCustomer() {
        Customer customer = binder.getBean();
        dao.saveCustomer(customer);
        customerView.updateList();
        setCustomer(null);
    }

    private void updateCustomer() {
        Customer customer = binder.getBean();
        dao.updateCustomer(customer);
        customerView.updateList();
        setCustomer(null);
    }
    private void delete() {
        Customer customer = binder.getBean();
        dao.deleteCustomer(customer);
        customerView.updateList();
        setCustomer(null);
    }

}