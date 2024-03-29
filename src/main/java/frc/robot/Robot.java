/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.ColorWheel;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Turret;
// import frc.robot.commands.AimTurret;
import frc.robot.commands.Auto_DriveAndShoot;
import frc.robot.commands.Auto_DriveOffLine;
import frc.robot.subsystems.Intake;
import frc.robot.OI;
// import frc.robot.commands.AutonomousTest;
import frc.robot.components.LED;
// import edu.wpi.first.wpilibj.Relay;

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
  private final SendableChooser<Command> m_chooser = new SendableChooser<>();
  private static LED m_led = new LED();
  public static DriveTrain m_driveTrain = new DriveTrain();
  public static OI m_oi;
  public static Turret m_turret = new Turret();
  public static Intake m_intake = new Intake();
  public static ColorWheel m_colorwheel = new ColorWheel();
  public static Climb m_climb = new Climb();
  public static UsbCamera cam0;
  private static Alliance alliance;
  private static Command m_autoMode;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    super.robotInit();
    m_oi = new OI();
    m_turret.init();
    cam0 = CameraServer.getInstance().startAutomaticCapture();
    cam0.setVideoMode(PixelFormat.kMJPEG, 160, 120, 15);

    // Add our autonomous routines
    m_chooser.setDefaultOption("No Auto", null);
    m_chooser.addOption("Drive off line", new Auto_DriveOffLine());
    m_chooser.addOption("Drive and shoot", new Auto_DriveAndShoot());
    SmartDashboard.putData("Auto choices", m_chooser);

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
    alliance = DriverStation.getInstance().getAlliance();
    m_turret.init();
    m_led.disableRainbow();
    setLights();
    m_driveTrain.resetAllEncoders();
    m_driveTrain.resetGyro();
    m_autoMode = m_chooser.getSelected();
    if (m_autoMode != null) {
      
      m_autoMode.execute();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    alliance = DriverStation.getInstance().getAlliance();
    setLights();
    m_driveTrain.resetGyro();
    m_turret.hoodEncoder.setPosition(0);
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



  }

  @Override
  public void disabledInit() {
    super.disabledInit();
    m_driveTrain.setCoastMode();
    m_led.enableRainbow();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  private void setLights() {
    m_led.disableRainbow();
    if (alliance == Alliance.Red){
      m_led.setSolidColor(LED.Colors.RED);
    }
    else if (alliance == Alliance.Blue){
      m_led.setSolidColor(LED.Colors.BLUE);  
    }
    else {
      m_led.setSolidColor(LED.Colors.GREEN);
    }
  }

}
