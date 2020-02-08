/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.components.LimeLight;

public class Turret extends SubsystemBase {

  // Turret Aiming System
  public WPI_TalonSRX turretPivot = new WPI_TalonSRX(5);
  public CANSparkMax turretElevate = new CANSparkMax(13, MotorType.kBrushless);
  private final double kMaxPivotPower = .75;
  private final double kMaxElevatePower = .5;
  private final Double minHorizontalAngle = 2.0;

  // Turret Firing System
  public static CANSparkMax thrower1 = new CANSparkMax(11, MotorType.kBrushless);
  public static CANSparkMax thrower2 = new CANSparkMax(12, MotorType.kBrushless);
  public SpeedControllerGroup thrower = new SpeedControllerGroup(thrower1, thrower2);
  private final double kMaxThrowerPower = .75;

  private LimeLight lime = new LimeLight();
  private boolean isInTrackingMode = false;

  public Turret() {
  
  }

  public void init() {
    thrower2.setInverted(true);
    thrower.set(0);
    turretPivot.set(0);
    turretElevate.set(0);
    lime.init("limelight-turret");
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
  }

  public void lockOnTarget() {
    if (isInTrackingMode) {
      Double horizontalOffset = lime.getTargetOffsetHorizontal();
      // Double verticalOffset = lime.getTargetOffsetVertical();
      double newZ = 0;
      if (Math.abs(horizontalOffset) > minHorizontalAngle) {
        newZ = kMaxPivotPower * (int) Math.signum(horizontalOffset) + Math.sin(horizontalOffset * Math.PI / 180);
      }
      System.out.println("horizontalOffset: " + horizontalOffset.toString());
      System.out.println("minHorizontalAngle: " + minHorizontalAngle.toString());
      turretPivot.set(newZ);
    }

  }


}
