package eu.deschler.dapro;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarDao {
    private static final long serialVersionUID = -5150635303857129026L;
    private static CarDao instance;
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/DPGD_1";
    private String username = "root";
    private String password = "my_secret_password";
    private String filter = "";

    private CarDao() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static CarDao getInstance() {
        if (instance == null) {
            instance = new CarDao();
        }
        return instance;
    }

    public List<Car> fetchCars(int offset, int limit) {
        List<Car> cars = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Automodell" + filter + " LIMIT ? OFFSET ?");
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getInt("ID"));
                car.setBezeichnung(resultSet.getString("Bezeichnung"));
                car.setHersteller(resultSet.getString("Hersteller"));
                car.setAutoart(resultSet.getInt("Autoart"));
                car.setSitzplaetze(resultSet.getInt("Sitzplaetze"));
                car.setKw(resultSet.getInt("KW"));
                car.setTreibstoff(resultSet.getString("Treibstoff"));
                car.setPreisTag(resultSet.getFloat("PreisTag"));
                car.setPreisKm(resultSet.getFloat("PreisKm"));
                car.setAchsen(resultSet.getString("Achsen"));
                car.setLadeVolumen(resultSet.getString("LadeVolumen"));
                car.setZuladung(resultSet.getString("Zuladung"));
                car.setFuehrerschein(resultSet.getString("Fuehrerschein"));
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public int getCarCount() {
        int count = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM Automodell" + filter);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void reserve(int carID, int customer_no, LocalDate reservationStart, LocalDate reservationEnd) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Reservierung (KundeID, ModellID, Beginn, Ende) VALUES (?, ?, ?, ?)");
            statement.setInt(1, customer_no);
            statement.setInt(2, carID);
            statement.setDate(3, Date.valueOf(reservationStart));
            statement.setDate(4, Date.valueOf(reservationEnd));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isReservationValid(int carID, int customerID, LocalDate reservationStart, LocalDate reservationEnd) {
        CustomerDao customerDao = CustomerDao.getInstance();

        //Sind die Reservierungstermine sinnvoll?
        if (reservationStart == null || reservationEnd == null || reservationStart.isAfter(reservationEnd)) {
            CarReservationForm.showNotification("Das Enddatum ist vor dem Anfangsdatum!", NotificationVariant.LUMO_ERROR);
            return false;
        }

        //Existiert der Kunde?
        if (!customerDao.customerWithNumberExists(customerID)) {
            CarReservationForm.showNotification("Kunde existiert nicht!", NotificationVariant.LUMO_ERROR);
            return false;
        }

        //Hat der Kunde den passenden Führerschein?
        Customer customer = customerDao.getCustomerByCustomerNo(customerID);
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT Fuehrerschein FROM Automodell WHERE ID = ?");
            statement.setInt(1, carID);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String fuehrerschein = resultSet.getString("Fuehrerschein");
            if (!customer.getDrivingLicenseClasses().contains(fuehrerschein)) {
                CarReservationForm.showNotification("Kunde hat nicht den passenden Führerschein!", NotificationVariant.LUMO_ERROR);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Ist in der Zeit das Auto bereits reserviert?
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Reservierung WHERE (ModellID = ? AND Beginn <= ? AND Ende >= ?) OR (ModellID = ? AND Beginn <= ? AND Ende >= ?) OR (ModellID = ? AND Beginn <= ? AND Ende >= ?)");
            statement.setInt(1, carID);
            statement.setDate(2, Date.valueOf(reservationStart));
            statement.setDate(3, Date.valueOf(reservationStart));
            statement.setInt(4, carID);
            statement.setDate(5, Date.valueOf(reservationEnd));
            statement.setDate(6, Date.valueOf(reservationEnd));
            statement.setInt(7, carID);
            statement.setDate(8, Date.valueOf(reservationEnd));
            statement.setDate(9, Date.valueOf(reservationStart));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                CarReservationForm.showNotification("Auto ist in diesem Zeitraum bereits reserviert!", NotificationVariant.LUMO_ERROR);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }
}