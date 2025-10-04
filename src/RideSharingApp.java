import java.util.ArrayList;
import java.util.List;

//Abstract User Class
// Abstraction: User defines common properties and behavior of all users
abstract class User {
    private String name;  // Encapsulation: private field
    private int id;       // Encapsulation: private field

    public User(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }
    public int getId() { return id; }

    public abstract void showDetails(); // Abstract method
}

// Customer Class
// Inheritance: Customer extends User
class Customer extends User {
    private List<Ride> rideHistory = new ArrayList<>(); // Composition

    public Customer(String name, int id) {
        super(name, id);
    }

    @Override
    public void showDetails() {
        System.out.println("Customer: " + getName() + " (ID: " + getId() + ")");
    }

    public void addRideToHistory(Ride ride) {
        rideHistory.add(ride);
    }

    public void showRideHistory() {
        System.out.println("Ride History for " + getName() + ":");
        for (Ride r : rideHistory) {
            System.out.println(r);
        }
    }
}

// Driver Class
// Inheritance: Driver extends User
class Driver extends User {
    private boolean available = true;  // Encapsulation

    public Driver(String name, int id) {
        super(name, id);
    }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public void showDetails() {
        System.out.println("Driver: " + getName() + " (ID: " + getId() + ")");
    }
}

// Abstract Vehicle Class
// Abstraction: Vehicle defines common behavior of all vehicles
abstract class Vehicle {
    protected double farePerKm;  // Protected allows access in subclasses

    public Vehicle(double farePerKm) {
        this.farePerKm = farePerKm;
    }

    public abstract double calculateFare(double distance); // Polymorphism
}

// Vehicle Subclasses
// Inheritance + Polymorphism: Different vehicle types calculate fares differently
class Bike extends Vehicle {
    public Bike() { super(5); }  // $5 per km

    @Override
    public double calculateFare(double distance) {
        return distance * farePerKm;
    }
}

class Car extends Vehicle {
    public Car() { super(10); } // $10 per km

    @Override
    public double calculateFare(double distance) {
        return (distance * farePerKm) + 20; // Extra fixed charge
    }
}

// Payment Interface
// Abstraction: Different payment methods implement this interface
interface Payment {
    void pay(double amount);
}

class CreditCardPayment implements Payment {
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using Credit Card.");
    }
}

class UPIPayment implements Payment {
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using UPI.");
    }
}

// Ride Class
// Composition: Ride contains Customer, Driver, Vehicle
class Ride {
    private Customer customer;
    private Driver driver;
    private Vehicle vehicle;
    private double distance;

    public Ride(Customer customer, Driver driver, Vehicle vehicle, double distance) {
        this.customer = customer;
        this.driver = driver;
        this.vehicle = vehicle;
        this.distance = distance;
    }

    // Start a ride
    public void startRide(Payment paymentMethod) {
        if (!driver.isAvailable()) {
            System.out.println("Driver " + driver.getName() + " is not available.");
            return;
        }

        double fare = vehicle.calculateFare(distance); // Polymorphism
        System.out.println(customer.getName() + " is taking a ride in a " +
                vehicle.getClass().getSimpleName() + " for " + distance + " km.");
        paymentMethod.pay(fare); // Polymorphism
        driver.setAvailable(false); // Driver is now busy
        System.out.println("Ride started with driver: " + driver.getName());

        // Add ride to customer history
        customer.addRideToHistory(this);
    }

    @Override
    public String toString() {
        return vehicle.getClass().getSimpleName() + " ride of " + distance + " km with driver " +
                driver.getName();
    }
}

// Main class
public class RideSharingApp {
    public static void main(String[] args) {
        // Create Customers and Drivers
        Customer c1 = new Customer("Alice", 101);
        Customer c2 = new Customer("Bob", 102);

        Driver d1 = new Driver("John", 201);
        Driver d2 = new Driver("Mike", 202);

        // Show details
        c1.showDetails();
        d1.showDetails();

        //Select Vehicles
        Vehicle bike = new Bike();
        Vehicle car = new Car();

        //Create and Start Rides
        Ride ride1 = new Ride(c1, d1, car, 12); // 12 km ride
        Payment payment1 = new CreditCardPayment();
        ride1.startRide(payment1);

        Ride ride2 = new Ride(c2, d2, bike, 5); // 5 km ride
        Payment payment2 = new UPIPayment();
        ride2.startRide(payment2);

        // Show Ride History
        System.out.println();
        c1.showRideHistory();
        c2.showRideHistory();
    }
}
