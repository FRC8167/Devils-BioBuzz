package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public static Follower create(HardwareMap h) {
         return new Follower(
                 new PinpointLocalizer(h, Constants.localizerConfig),
                 new Mecanum(h, Constants.drivetrainConfig),
                 new Foresight(Constants.foresightConfig)
         );
    }

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("LeftFront");
        c.frontRightName.set("RightFront");
        c.backLeftName.set("LeftRear");
        c.backRightName.set("RightRear");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);

        c.manualBrakeMode.set(true); //Note: Not sure what this does but added for now
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(0.0); // AutoTuneVal: -0.3587657823337345 //TODO: Test OnShape values and localizer
        c.yPodOffset.set(-1.41732); //-36mm // AutoTuneVal: -0.8867398960383858
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.27396863573908575);
                Controller secondaryTranslationalForward = Controller.proportional(0.10122409032882027);
                Controller primaryTranslationalLateral = Controller.proportional(0.35452299404841214);
                Controller secondaryTranslationalLateral = Controller.proportional(0.13098677327202007);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.011801094333732049));
                c.brake.set(Controller.proportionalFeedforward(0.010030930183672241));

                c.headingFeedback.set(Controller.proportional(5.289946911547581));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.05696039356968459, 0.007112278709758184));

                c.linearBrakeCoefficients.set(Matrix.diag(0.08825990249287555, 0.05000002625948696));
                c.quadraticBrakeCoefficients.set(Matrix.diag(9.675490572054315E-4, 0.0016324282581253787));

                c.maxAchievableForwardVelocity.set(84.49218881439788);
                c.maxAchievableStrafeVelocity.set(69.7074454748948);
                c.naturalForwardDeceleration.set(30.821626836788905);
                c.naturalStrafeDeceleration.set(63.06149630129685);
            }
    );
}