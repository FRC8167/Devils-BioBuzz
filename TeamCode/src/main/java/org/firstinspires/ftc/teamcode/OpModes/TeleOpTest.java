package org.firstinspires.ftc.teamcode.OpModes;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Cogintilities.DefaultMotorInfo;
import org.firstinspires.ftc.teamcode.SubSystems.ConfigurableConstants;
import org.firstinspires.ftc.teamcode.SubSystems.Motor1D;

@TeleOp(name="TeleOpTest", group="Testing")
public class TeleOpTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        Telemetry dsTelemetry = telemetry;
        telemetry = new JoinedTelemetry(PanelsTelemetry.INSTANCE.getFtcTelemetry(), dsTelemetry);

        Motor1D motor = new Motor1D(
                hardwareMap.get(DcMotorEx.class, "Shooter"),
                Motor1D.Mode.VELOCITY_BASED,
                DefaultMotorInfo.GOBILDA_6000RPM
        );
        motor.setVelocityPIDFCoefficients(ConfigurableConstants.getPidfCoefficients());

        int targetRPM = 0;

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.yWasPressed())
                targetRPM += 100;
            else if (gamepad1.aWasPressed())
                targetRPM -= 100;
            else if (gamepad1.bWasPressed())
                targetRPM += 10;
            else if (gamepad1.xWasPressed())
                targetRPM  -= 10;

            motor.setVelocity(targetRPM);

            if (ConfigurableConstants.updatePIDFCoefficients())
                motor.setVelocityPIDFCoefficients(ConfigurableConstants.getPidfCoefficients());
            telemetry.addData("Target RPM", targetRPM);
            telemetry.addData("Actual RPM", motor.getVelocityRPM());
            telemetry.update();
        }

    }



}
