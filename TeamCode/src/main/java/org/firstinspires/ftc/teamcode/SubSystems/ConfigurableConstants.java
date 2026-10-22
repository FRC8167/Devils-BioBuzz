package org.firstinspires.ftc.teamcode.SubSystems;

import androidx.annotation.NonNull;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.jetbrains.annotations.Contract;

@Configurable
public class ConfigurableConstants {
    private ConfigurableConstants() {}

    private static PIDFCoefficients pidfCoefficients = new PIDFCoefficients();

    public static double kP = 1.5; // P = 0.1 × F
    public static double kI = 0; // I = 0.1 × P
    public static double kD = 0; // D = 0;
    public static double kF = 5; // F = 32767 ÷ maxV(t/s)!!! TODO: implement this!

    @NonNull
    @Contract(value = " -> new", pure = true)
    public static PIDFCoefficients getPidfCoefficients() {
        return new PIDFCoefficients(pidfCoefficients);
    }

    public static boolean updatePIDFCoefficients() {

        boolean different =
                pidfCoefficients.p != kP ||
                pidfCoefficients.i != kI ||
                pidfCoefficients.d != kD ||
                pidfCoefficients.f != kF;

        pidfCoefficients.p = kP;
        pidfCoefficients.i = kI;
        pidfCoefficients.d = kD;
        pidfCoefficients.f = kF;
        return different;
    }

}
