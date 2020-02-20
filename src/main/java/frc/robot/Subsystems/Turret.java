/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANDigitalInput.LimitSwitch;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;

import frc.robot.components.LimeLight;

public class Turret extends SubsystemBase {

  // Turret Aiming System
  public WPI_TalonSRX turretPivot = new WPI_TalonSRX(5);
  public CANSparkMax turretElevate = new CANSparkMax(13, MotorType.kBrushless);
  private final double kMinPivotPower = .1;
  private final double kMaxElevatePower = .5;
  private final Double minHorizontalAngle = 3.5;
  private final double kMaxTurretPower = .5;
  private static DigitalInput turretRotateLeftSwitch;
  private static DigitalInput turretRotateRightSwitch;

  // Turret Firing System
  public static CANSparkMax thrower1 = new CANSparkMax(11, MotorType.kBrushless);
  public static CANSparkMax thrower2 = new CANSparkMax(12, MotorType.kBrushless);
  public SpeedControllerGroup thrower = new SpeedControllerGroup(thrower1, thrower2);
  private final double kMaxThrowerPower = 1;

  private LimeLight lime = new LimeLight();
  private boolean isInTrackingMode = false;

  public void init() {
    
    thrower2.setInverted(true);
    thrower2.setIdleMode(IdleMode.kBrake);
    thrower.set(0);
    turretPivot.set(0);
    turretElevate.set(0);
    lime.init("limelight-front");
  }

  @Override
  public void periodic() {
    // Add code here to update driver station with turret info (bearing and elevation)

  }

  public void enableTrackingMode() {
    isInTrackingMode = true;
  }
  public void disableTrackingMode() {
    isInTrackingMode = false;
    turretPivot.set(0);
  }

  public Turret() {
    turretRotateLeftSwitch = new DigitalInput(3);
    turretRotateRightSwitch = new DigitalInput(4);
  }

  public void leftLimit() {
    if (turretRotateLeftSwitch.get() == true) {
      turretPivot.set(kMinPivotPower);
    } else {
      turretPivot.set(0.0);
    }
  }

  public void rightLimit() {
    if (turretRotateRightSwitch.get() == true) {
      turretPivot.set(-kMinPivotPower);
    } else {
      turretPivot.set(0.0);
    }
  }

  public void lookLeft(){
    turretPivot.set(kMaxTurretPower);
  }

  public void lookRight(){
    turretPivot.set(-kMaxTurretPower);
  }

  public void climbUp(){
    thrower2.set(kMaxThrowerPower);
  }

  public void climbDown(){
    thrower2.set(-kMaxThrowerPower);
  }

  public void climbStop(){
    thrower2.set(0.0);
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
      turretPivot.set(-newZ);
    }

  }



}
