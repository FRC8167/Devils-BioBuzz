package org.firstinspires.ftc.teamcode.OpModes;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Cogintilities.DefaultMotorInfo;
import org.firstinspires.ftc.teamcode.SubSystems.ConfigurableConstants;
import org.firstinspires.ftc.teamcode.SubSystems.Motor1D;

record Rectangle(double width, double height) {
    public double area() {
        return width * height;
    }
}

@TeleOp(name="TeleOpTestJava", group="Testing")
public class TeleOpTestJava extends LinearOpMode {

    @Override
    public void runOpMode() {

        var testRect = new Rectangle(10, 20);

        waitForStart();

        while (opModeIsActive()) {
            var area = testRect.area();

            telemetry.addData("Area", area);
            telemetry.update();
        }

    }



}
