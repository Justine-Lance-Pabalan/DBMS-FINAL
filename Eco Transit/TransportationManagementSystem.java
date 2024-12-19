import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TransportationManagementSystem {
    private Connection connection;

    public TransportationManagementSystem() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/transportation", "username", "password");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public void addVehicle(Vehicle vehicle) {
        String query = "INSERT INTO vehicles (model, year, efficiency, battery_capacity, emissions_rate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, vehicle.getModel());
            statement.setInt(2, vehicle.getYear());
            statement.setDouble(3, vehicle.getFuelEfficiency());

            if (vehicle instanceof ElectricVehicle) {
                statement.setDouble(4, ((ElectricVehicle) vehicle).getBatteryCapacity());
                statement.setNull(5, Types.DOUBLE); // Hybrid vehicle doesn't need emissions_rate
            } else if (vehicle instanceof HybridVehicle) {
                statement.setNull(4, Types.DOUBLE); // Electric vehicle doesn't need battery_capacity
                statement.setDouble(5, ((HybridVehicle) vehicle).getEmissionsRate());
            }

            statement.executeUpdate();
            System.out.println("Vehicle added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding vehicle to DB: " + e.getMessage());
        }
    }

    public void removeVehicle(Scanner scanner) {
        if (fleetIsEmpty()) return;

        System.out.println("\n--- Current Fleet ---");
        displayFleet();
        displayFleetEmissions();

        System.out.print("\nEnter the model of the vehicle you want to remove: ");
        String model = scanner.nextLine();

        String query = "DELETE FROM vehicles WHERE model = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, model);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Vehicle with model '" + model + "' removed successfully.");
            } else {
                System.out.println("No vehicle with model '" + model + "' found.");
            }
        } catch (SQLException e) {
            System.out.println("Error removing vehicle from DB: " + e.getMessage());
        }
    }

    public boolean fleetIsEmpty() {
        String query = "SELECT COUNT(*) FROM vehicles";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next() && resultSet.getInt(1) == 0) {
                System.out.println("\nThe fleet is empty. No vehicles to remove.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error checking if fleet is empty: " + e.getMessage());
        }
        return false;
    }

    public void displayFleet() {
        String query = "SELECT * FROM vehicles";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                System.out.println("No vehicles in the fleet.");
                return;
            }

            do {
                String model = resultSet.getString("model");
                int year = resultSet.getInt("year");
                double efficiency = resultSet.getDouble("efficiency");
                double batteryCapacity = resultSet.getDouble("battery_capacity");
                double emissionsRate = resultSet.getDouble("emissions_rate");

                System.out.println("Model: " + model + ", Year: " + year + ", Fuel Efficiency: " + efficiency + " km/l");
                if (batteryCapacity > 0) {
                    System.out.println("Battery Capacity: " + batteryCapacity + " kWh");
                }
                if (emissionsRate > 0) {
                    System.out.println("Emissions Rate: " + emissionsRate + " g/km");
                }
                System.out.println("---");
            } while (resultSet.next());
        } catch (SQLException e) {
            System.out.println("Error displaying fleet: " + e.getMessage());
        }
    }

    public void displayFleetEmissions() {
        String query = "SELECT SUM(emissions_rate) AS total_emissions FROM vehicles WHERE emissions_rate IS NOT NULL";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                double totalEmissions = resultSet.getDouble("total_emissions");
                System.out.println("Total Fleet Emissions: " + totalEmissions + " g/km");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating fleet emissions: " + e.getMessage());
        }
    }
}
