/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.Joystick;

public class DriveTrain extends SubsystemBase {

  private final WPI_TalonFX leftFront = new WPI_TalonFX(1);
  private final WPI_TalonFX leftRear = new WPI_TalonFX(2);
  private final WPI_TalonFX rightRear = new WPI_TalonFX(4);
  private final WPI_TalonFX rightFront = new WPI_TalonFX(3);
  private final MecanumDrive m_drive;
  private final double rampRate = 0.5;
  private final double deadband = 0.1;
  private final double stickPowerFactor = 0.65;
  private final double rotationPowerFactor = 0.5;

  private Joystick m_stick;
  private static final int kJoystickChannel = 0;
  
   // Encoders on DriveTrain motors
  public double leftFrontEncoder = 0;
  public double leftRearEncoder = 0;
  public double rightFrontEncoder = 0;
  public double rightRearEncoder = 0;

  // Gyro Sensor
	public Gyro rioGyro;
  /**
   * Creates a new DriveTrain.
   */
  public DriveTrain() {
    rioGyro = new ADXRS450_Gyro();
    m_drive = new MecanumDrive(leftFront, leftRear, rightFront, rightRear);

    m_stick = new Joystick(kJoystickChannel); 

    m_drive.setDeadband(deadband);
    resetLeftFrontEncoder();
    resetLeftRearEncoder();
    resetRightFrontEncoder();
    resetRightRearEncoder();

    leftFront.configOpenloopRamp(rampRate);
    leftRear.configOpenloopRamp(rampRate);
    rightFront.configOpenloopRamp(rampRate);
    rightRear.configOpenloopRamp(rampRate);

    setBrakeMode();

  }

  public void resetLeftFrontEncoder(){
    leftFront.setSelectedSensorPosition(0);
  }

  public void resetLeftRearEncoder(){
    leftRear.setSelectedSensorPosition(0);
  }

  public void resetRightFrontEncoder(){
    rightFront.setSelectedSensorPosition(0);
  }

  public void resetRightRearEncoder(){
    rightRear.setSelectedSensorPosition(0);
  }

  public void resetAllEncoders() {
    resetLeftFrontEncoder();
    resetLeftRearEncoder();
    resetRightFrontEncoder();
    resetRightRearEncoder();
  }

  public void resetGyro() {
    rioGyro.reset();
  }

  public double getFrontAverage(){
    updateEncoders();
    return Math.abs(leftFront.getSelectedSensorPosition()) + Math.abs(rightFrontEncoder) / 2;
  }

  public double getRearAverage(){
    // motors are oriented opposite of each other so abs value is neccessary
    updateEncoders();
    return Math.abs(leftRearEncoder) + Math.abs(rightRearEncoder) / 2;
  }

  public double getLeftStrafeAverage() {
    // when strafing, motors are turning in opposite directions so one must be negated
    updateEncoders();
    return (leftFront.getSelectedSensorPosition()) + -(leftRearEncoder) /4;
  }

  public double getRightStrafeAverage() {
    updateEncoders();
    return (rightFrontEncoder) + -(rightRearEncoder) /4;
  }

  public void drive() {
      //  m_drive.driveCartesian(liveX, liveY, liveZ);
    double stickAngle = m_stick.getDirectionDegrees();
    double stickPower = m_stick.getMagnitude();
    double gyroAngle = rioGyro.getAngle() % 360;
    double driveAngle = stickAngle - gyroAngle;
    SmartDashboard.putNumber("aGyro", gyroAngle);
    SmartDashboard.putNumber("aStick:", stickAngle);
    SmartDashboard.putNumber("aDrive: ", driveAngle);
    m_drive.drivePolar(Math.pow(stickPower, 3) * stickPowerFactor, driveAngle, Math.pow(m_stick.getZ(),3) * rotationPowerFactor);
   }

  public void updateEncoders(){
    leftFrontEncoder = leftFront.getSelectedSensorPosition();
    leftRearEncoder = leftRear.getSelectedSensorPosition();
    rightFrontEncoder = rightFront.getSelectedSensorPosition();
    rightRearEncoder = rightRear.getSelectedSensorPosition();
    SmartDashboard.putNumber("Encoders/LF", leftFrontEncoder);
    SmartDashboard.putNumber("Encoders/LR", leftRearEncoder);
    SmartDashboard.putNumber("Encoders/RF", rightFrontEncoder);
    SmartDashboard.putNumber("Encoders/RR", rightRearEncoder);

  }

  public void setCoastMode(){
    leftFront.setNeutralMode(NeutralMode.Coast);
    rightFront.setNeutralMode(NeutralMode.Coast);
    leftRear.setNeutralMode(NeutralMode.Coast);
    rightRear.setNeutralMode(NeutralMode.Coast);
  
  }

  public void setBrakeMode(){
    leftFront.setNeutralMode(NeutralMode.Brake);
    rightFront.setNeutralMode(NeutralMode.Brake);
    leftRear.setNeutralMode(NeutralMode.Brake);
    rightRear.setNeutralMode(NeutralMode.Brake);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    
  }
}
