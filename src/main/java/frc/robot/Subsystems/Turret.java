/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.components.LimeLight;

public class Turret extends SubsystemBase {

  // Turret Aiming System
  public WPI_TalonSRX turretPivot = new WPI_TalonSRX(11);
  public CANSparkMax turretElevate = new CANSparkMax(13, MotorType.kBrushless);
  public CANEncoder hoodEncoder = turretElevate.getEncoder();
  private final double kMinPivotPower = .1;
  private final double kMaxElevatePower = .5;
  private final Double minHorizontalAngle = 1.5;
  private final double kMaxTurretPower = .5;
  private static DigitalInput turretMaxLeftSwitch = new DigitalInput(2);
  private static DigitalInput turretMaxRightSwitch = new DigitalInput(1);
  private double hoodEncoderPosition = 0;
  private double hoodEncoderMinPosition = -1;  // assume same as hoodEncoderPosition at robot init
  private double hoodEncoderMaxPosition = -25;

  // Turret Firing System
  public static WPI_TalonFX thrower1 = new WPI_TalonFX(5);
  public static WPI_TalonFX thrower2 = new WPI_TalonFX(6);
  public SpeedControllerGroup thrower = new SpeedControllerGroup(thrower1, thrower2);
  private final double kMaxThrowerPower = -1;
  public static WPI_TalonSRX feeder = new WPI_TalonSRX(7);
  private final double kMaxFeedPower = -1;

  private LimeLight lime = new LimeLight();
  private boolean isInTrackingMode = false;

  public void init() {
    thrower2.setInverted(true);
    thrower.set(0);
    turretPivot.setNeutralMode(NeutralMode.Brake);
    turretPivot.set(0);
    turretElevate.setIdleMode(IdleMode.kBrake);
    turretElevate.set(0);
    lime.init("limelight");
    hoodEncoder.setPosition(0);
  }

  @Override
  public void periodic() {
    // Add code here to update driver station with turret info (bearing and elevation)
    SmartDashboard.putNumber("_Hood_Encoder", hoodEncoder.getPosition());
    SmartDashboard.putNumber("_Shooter_Speed", thrower1.getSelectedSensorVelocity());

  }

  public void enableTrackingMode() {
    isInTrackingMode = true;
  }
  public void disableTrackingMode() {
    isInTrackingMode = false;
    turretPivot.set(0);
  }

  public Turret() {
  }

  public boolean isAtLeftLimit() {
    return turretMaxLeftSwitch.get();
  }

  public boolean isAtRightLimit() {
    return turretMaxRightSwitch.get();
  }

  public boolean hoodIsAtMaxElevation() {
    hoodEncoderPosition = hoodEncoder.getPosition();
    return hoodEncoderPosition <= hoodEncoderMaxPosition;
  }

  public boolean hoodIsAtMinElevation() {
    hoodEncoderPosition = hoodEncoder.getPosition();
    return hoodEncoderPosition >= hoodEncoderMinPosition;
  }

  public void lookLeft(){
    if (isAtLeftLimit()) {
      turretPivot.set(kMaxTurretPower);
    } else {
      stopTurret();
    }
  }

  public void lookRight(){
    if (isAtRightLimit()) {
      turretPivot.set(-kMaxTurretPower);
    } else {
      stopTurret();
    }
  }

  public void shooterRun(){
    thrower.set(kMaxThrowerPower);
  }

  public void clearJam(){
    thrower.set(-kMaxThrowerPower * 0.5);
    feeder.set(-kMaxFeedPower);
  }

  public void shooterStop(){
    thrower.set(0.0);
  }

  public void fireOn(){
    feeder.set(kMaxFeedPower);
  }

  public void fireOff(){
    feeder.set(0.0);
  }

  public void stopTurret() {
    turretPivot.set(0);
  }

  public void lockOnTarget() {
    if (isInTrackingMode) {
      Double horizontalOffset = lime.getTargetOffsetHorizontal();
      // Double verticalOffset = lime.getTargetOffsetVertical();
      double newZ = 0;
      if (Math.abs(horizontalOffset) > minHorizontalAngle) {
        newZ = kMinPivotPower * (int) Math.signum(horizontalOffset) + Math.tan(horizontalOffset * Math.PI / 180);
      }
      System.out.println("horizontalOffset: " + horizontalOffset.toString());
      System.out.println("minHorizontalAngle: " + minHorizontalAngle.toString());
      if (newZ < 0 && !isAtLeftLimit()) {
        turretPivot.set(-newZ);
      } else if (newZ > 0 && !isAtRightLimit()) {
        turretPivot.set(-newZ);
      }
    }

  }

  public void pivotUp() {
    if (!hoodIsAtMaxElevation()) {
      turretElevate.set(kMaxElevatePower);
    } else {
      pivotStop();
    }
  }

  public void pivotDown() {
    if (!hoodIsAtMinElevation()) {
      turretElevate.set(-kMaxElevatePower);
    } else {
      pivotStop();
    }
  }

  public void pivotStop() {
    turretElevate.set(0.0);
  }

}
