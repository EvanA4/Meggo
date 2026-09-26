package net.eabbott.verdi.util.math;

public class ViewBounds {
    public double minPitch;
    public double minYaw;
    public double maxPitch;
    public double maxYaw;

    public ViewBounds(double minPitch, double minYaw, double maxPitch, double maxYaw) {
        this.minPitch = minPitch;
        this.minYaw = minYaw;
        this.maxPitch = maxPitch;
        this.maxYaw = maxYaw;
    }
}
