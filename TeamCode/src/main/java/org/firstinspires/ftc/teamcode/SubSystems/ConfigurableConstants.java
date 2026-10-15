package org.firstinspires.ftc.teamcode.SubSystems;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Configurable
public class ConfigurableConstants {
    private ConfigurableConstants() {}

    private static PIDFCoefficients pidfCoefficients = new PIDFCoefficients();

    public static double kP = 1.5;
    public static double kI = 0;
    public static double kD = 0;
    public static double kF = 5;

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
