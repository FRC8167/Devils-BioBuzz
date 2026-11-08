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

import java.util.List;

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

        var list = List.of(1, 2, 3);
        var sum = 0;

        waitForStart();

        for (var item : list) {
            sum += item;
        }

        var area = testRect.area();

        telemetry.addData("Area", area);
        telemetry.addData("Sum", sum);

        telemetry.addLine(inspect("Test?"));
        telemetry.addLine(inspect("Test!!!"));
        telemetry.addLine(inspect(testRect));

        telemetry.update();

        while (opModeIsActive()) {
            sleep(100);
        }

    }
    public static String inspect(Object obj) {
        // Tests both pattern binding and inline '&&' evaluation
        if (obj instanceof String s && s.length() > 5) {
            return "Long string: " + s.toUpperCase();
        }

        // Tests standard pattern binding
        if (obj instanceof String s) {
            return "Short string: " + s.toLowerCase();
        }

        // Handles non-Strings and null safely
        return "Not a string";
    }



}
