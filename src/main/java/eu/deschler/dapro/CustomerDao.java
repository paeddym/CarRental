package eu.deschler.dapro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.*;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.client.model.Sorts;
import org.bson.types.ObjectId;

public class CustomerDao implements Serializable {

    private static final long serialVersionUID = -8151490228254675155L;
    private static CustomerDao instance = new CustomerDao();

    private static final String DATABASE = "dapro_01"; //TODO replace with your database number
    private static final String CONNECTION_STRING = "mongodb://dapro_01:dapro_01@localhost/dapro_01"; //TODO replace x 3
    private MongoClient mongoClient;
    private String filter = "";
    
    public CustomerDao() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        ConnectionString connectionString = new ConnectionString(CONNECTION_STRING);
        CodecRegistry pojoCodecRegistry = CodecRegistries
                .fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry);

        MongoClientSettings clientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
                .codecRegistry(codecRegistry).build();

        mongoClient = MongoClients.create(clientSettings);

    }


    List<Customer> fetchCustomers(String filter) {
        MongoCollection<Customer> customers = mongoClient.getDatabase(DATABASE).getCollection("customers",
                Customer.class);
        List<Customer> custList = new ArrayList<>();
        if(filter == null || "".equals(filter)) {
            customers.find().into(custList);
        }
        else {
            customers.find(eq("last_name",filter)).into(custList);
        }
        return custList;
    }

    List<Customer> fetchAllCustomers() {
        MongoCollection<Customer> customers = mongoClient.getDatabase(DATABASE).getCollection("customers",
                Customer.class);
        List<Customer> custList = new ArrayList<>();
        customers.find().into(custList);
        System.out.println("Return customers");
        return custList;
    }

    List<Customer> fetchCustomers(int offset, int limit) {
        MongoCollection<Customer> customers = mongoClient.getDatabase(DATABASE).getCollection("customers",
                Customer.class);
        List<Customer> custList = new ArrayList<>();
        if(filter == null || "".equals(filter)) {
            customers.find().skip(offset).limit(limit).into(custList);
        }
        else {
            customers.find(eq("last_name",filter)).skip(offset).limit(limit).into(custList);
        }
        return custList;
    }

    public void saveCustomer(Customer customer) {
        MongoCollection<Customer> customers = mongoClient.getDatabase(DATABASE).getCollection("customers",
                Customer.class);
        customers.insertOne(customer);
    }

    int getCustomerCount() {
        MongoCollection<Customer> customers = mongoClient.getDatabase(DATABASE).getCollection("customers",
                Customer.class);
        if(filter == null || "".equals(filter)) {
            return (int) customers.countDocuments();
        }
        else {
            return (int) customers.countDocuments(eq("last_name",filter));
        }
        
    }

    void deleteCustomer(Customer c) {
        MongoCollection<Customer> customers = mongoClient.getDatabase(DATABASE).getCollection("customers",
        Customer.class);
        customers.deleteOne(eq("_id",c.getId()));
    }

    void updateCustomer(Customer c) {
        MongoCollection<Customer> customers = mongoClient.getDatabase(DATABASE).getCollection("customers",
        Customer.class);
        customers.replaceOne(eq("_id",c.getId()),c);
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public boolean customerWithNumberExists(int customerNo) {
        MongoCollection<Customer> customers = mongoClient.getDatabase(DATABASE).getCollection("customers",
        Customer.class);
        return customers.find(eq("customer_no",customerNo)).first() != null;
    }

    public Customer getCustomerByCustomerNo(int customerNo) {
        MongoCollection<Customer> customers = mongoClient.getDatabase(DATABASE).getCollection("customers",
        Customer.class);
        return customers.find(eq("customer_no",customerNo)).first();
    }

    public static CustomerDao getInstance() {
        return instance;
    }


}
