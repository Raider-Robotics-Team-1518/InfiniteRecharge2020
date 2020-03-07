/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.ColorWheel;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Turret;
import frc.robot.commands.AimTurret;
import frc.robot.subsystems.Intake;
import frc.robot.OI;
import frc.robot.commands.AutonomousTest;
import frc.robot.components.LED;
import edu.wpi.first.wpilibj.Relay;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // private static final String kDefaultAuto = "Default";
  // private static final String kCustomAuto = "My Auto";
  // private String m_autoSelected;
  // private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private static LED m_led = new LED();
  // private static String gameData = "";
  public static DriveTrain m_driveTrain = new DriveTrain();
  public static OI m_oi;
  public static Turret m_turret = new Turret();
  public static Intake m_intake = new Intake();
  public final static Relay m_relay = new Relay(0);
  public static ColorWheel m_colorwheel = new ColorWheel();
  public static Climb m_climb = new Climb();
  CommandBase at;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    super.robotInit();
    // m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    // m_chooser.addOption("My Auto", kCustomAuto);
    // SmartDashboard.putData("Auto choices", m_chooser);
    m_oi = new OI();
    m_turret.init();
    // m_colorWheel = new ColorWheel();
    m_relay.setDirection(Relay.Direction.kBoth);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    m_led.setRainbow();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_driveTrain.resetAllEncoders();
    m_driveTrain.resetGyro();
    // m_autoSelected = m_chooser.getSelected();
    // System.out.println("Auto selected: " + m_autoSelected);
    at = (CommandBase) new AutonomousTest();
    at.initialize();
    at.execute();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    m_led.disableRainbow();

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    // switch (m_autoSelected) {
    // case kCustomAuto:
    // // Put custom auto code here
    // break;
    // case kDefaultAuto:
    // default:
    // // Put default auto code here
    // break;
    // }
    // SmartDashboard.putNumber("mFrontAvg",
    // at.autosystem.a_drive.getFrontAverage());
    // SmartDashboard.putNumber("mLeftAvg",
    // at.autosystem.a_drive.getLeftStrafeAverage());
    // SmartDashboard.putNumber("mRightAvg",
    // at.autosystem.a_drive.getRightStrafeAverage());
    // SmartDashboard.putNumber("mRearAvg", at.autosystem.a_drive.getRearAverage());

  }

  @Override
  public void teleopInit() {
    m_led.disableRainbow();
    m_led.setSolidColor(LED.Colors.WHITE);
    m_driveTrain.resetGyro();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    m_driveTrain.drive();

    // For testing the thrower
    // thrower.set(m_oi.m_stick.getThrottle());

    // For testing the turret ring (lazy susan)
    // m_turret.turretPivot.set(m_oi.m_stick.getThrottle());

    // m_aTurret.execute(); we found a loophole to check if this was working


  }

  @Override
  public void disabledInit() {
    super.disabledInit();
    m_driveTrain.setCoastMode();
    m_led.enableRainbow();
    // m_relay.set(Relay.Value.kOff);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
