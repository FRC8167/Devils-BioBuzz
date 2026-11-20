package org.firstinspires.ftc.teamcode.Robot;

//import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.teamcode.Cogintilities.MirrorUtility;
import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import org.firstinspires.ftc.teamcode.SubSystems.PollenGate;
import org.firstinspires.ftc.teamcode.SubSystems.Intake;

import org.firstinspires.ftc.teamcode.SubSystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.SubSystems.NectarShooter;
import org.firstinspires.ftc.teamcode.SubSystems.PollenShooter;


import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;


import java.util.List;


public class Robot extends com.seattlesolvers.solverslib.command.Robot{

    private static final Robot instance = new Robot();
    public static Robot getInstance() {
        return instance;
    }

    public enum OpModeType {
        AUTO,
        TELEOP
    }

    public enum Alliance {
        RED,
        BLUE,
        UNSPECIFIED
    }

    //field poses

    private static Alliance alliance = Alliance.UNSPECIFIED;

    public Telemetry telemetry;


    public static OpModeType OP_MODE_TYPE;
    static List<LynxModule> ctrlHubs;

    public GoBildaPinpointDriver pinpoint;

    public Follower follower;
    public MecanumDrive mecanumDrive;
    public Intake intake;
    public PollenShooter pollenShooter;
    public NectarShooter nectarShooter;
    public PollenGate pollenGate;



    public void init(HardwareMap hardwareMap) throws InterruptedException {

        // Motors
        MotorEx driveMotorRF = hardwareMap.get(MotorEx.class, "RightFront");
        MotorEx driveMotorLF = hardwareMap.get(MotorEx.class, "LeftFront");
        MotorEx driveMotorLR = hardwareMap.get(MotorEx.class, "LeftRear");
        MotorEx driveMotorRR = hardwareMap.get(MotorEx.class, "RightRear");
        MotorEx intakeMotor  = hardwareMap.get(MotorEx.class, "intake");
        DcMotorEx pollenShooterMotor = hardwareMap.get(DcMotorEx.class, "PollenMotor");
        DcMotorEx nectarShooterMotor = hardwareMap.get(DcMotorEx.class, "NectarShooter");
        Servo pollenGateServo = hardwareMap.get(Servo.class, "pollenGate");

//Placeholder when adding auto later
//        follower = Constants.create(hardwareMap);
//        follower.setPose(startPose);
//        follower.update();

        ctrlHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : ctrlHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }


        //Instantiate Subsystems
        mecanumDrive = new MecanumDrive(driveMotorLF, driveMotorLR, driveMotorRF, driveMotorRR);
        intake  = new Intake(intakeMotor);
        pollenShooter  = new PollenShooter(pollenShooterMotor);
        nectarShooter = new NectarShooter(nectarShooterMotor);
        pollenGate = new PollenGate(pollenGateServo);


        //Register Subsystems
        register(mecanumDrive, intake, pollenShooter, nectarShooter, pollenGate);

        if (OP_MODE_TYPE.equals(OpModeType.AUTO)) {
            initHasMovement();
        }
    }

    public void initHasMovement() {
        //TODO what goes here??
    }

    public void setAlliance(Alliance ally) {
        alliance = ally;
    }

    public Alliance getAlliance() {
        return alliance;
    }




}