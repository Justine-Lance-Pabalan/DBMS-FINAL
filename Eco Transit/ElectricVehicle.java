public class ElectricVehicle extends Vehicle {
    private double batteryCapacity;

    public ElectricVehicle(String model, int year, double fuelEfficiency, double batteryCapacity) {
        super(model, year, fuelEfficiency);
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public double calculateCarbonFootprint() {
        return 0; // Zero direct emissions
    }

    public double getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(double batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }
}
