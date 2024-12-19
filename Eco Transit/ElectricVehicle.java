public class ElectricVehicle extends Vehicle {
    private double batteryCapacity;

    public ElectricVehicle(String model, int year, double fuelEfficiency, double batteryCapacity) {
        super(model, year, fuelEfficiency);
        this.batteryCapacity = batteryCapacity;
    }

    public double getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(double batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public double calculateCarbonFootprint() {
        return 0.0; // Electric vehicles have zero carbon footprint
    }
}
