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
  private final double kMaxElevatePower = .25;
  private final Double minHorizontalAngle = 1.5;
  private final double kMaxTurretPower = .5;
  private static DigitalInput turretMaxLeftSwitch = new DigitalInput(2);
  private static DigitalInput turretMaxRightSwitch = new DigitalInput(1);
  private double hoodEncoderPosition = 0;
  private double hoodEncoderMinPosition = -25;  // assume same as hoodEncoderPosition at robot init
  private double hoodEncoderMaxPosition = -0.25;

  // OUTER PORT is 8 ft. 2¼ in. (~249 cm)
  private static final double targetHeight = 98.25; // Inches
  private static final double optimalDistance = 84; // Inches
  private static final double cameraHeight = 30.0;  // Inches
  private static final double cameraMountingAngle = 23.0; // degrees

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
    Double foo = (Double)hoodEncoderPosition;
    System.out.println(foo.toString());
    return hoodEncoderPosition <= hoodEncoderMaxPosition;
  }

  public boolean hoodIsAtMinElevation() {
    hoodEncoderPosition = hoodEncoder.getPosition();
    Double foo = (Double)hoodEncoderPosition;
    System.out.println(foo.toString());
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
      Double verticalOffset = lime.getTargetOffsetVertical();
      // see https://docs.limelightvision.io/en/latest/cs_estimating_distance.html
      Double targetDistance = (targetHeight - cameraHeight) / Math.tan((cameraMountingAngle + verticalOffset) * Math.PI / 180); // in inches
      Double hoodEncoderPosition = hoodEncoder.getPosition();
      int shooterSpeed = -thrower2.getSelectedSensorVelocity();

      SmartDashboard.putNumber("Target/TargetDistance", (double)Math.round(targetDistance * 100d) / 100d);
      SmartDashboard.putNumber("Target/HorizontalOffset: ", horizontalOffset);
      SmartDashboard.putNumber("Target/MinHorizontalAngle: ", minHorizontalAngle);
      SmartDashboard.putNumber("Target/Hood_Encoder", hoodEncoderPosition);
      SmartDashboard.putNumber("Target/Shooter_Speed", shooterSpeed);
      if (shooterSpeed >= 18500) {
        SmartDashboard.putBoolean("Target/ShooterAtSpeed", true);
      }
      else { SmartDashboard.putBoolean("Target/ShooterAtSpeed", false);}
  
      Double newZ = 0.0;
      if (Math.abs(horizontalOffset) > minHorizontalAngle) {
        newZ = kMinPivotPower * (int) Math.signum(horizontalOffset) + Math.tan(horizontalOffset * Math.PI / 180);
      }
      if (newZ < 0 && !isAtLeftLimit()) {
        turretPivot.set(-newZ);
      } else if (newZ > 0 && !isAtRightLimit()) {
        turretPivot.set(-newZ);
      }
// System.out.println(hoodEncoderPosition.toString());
      // ADD CODE HERE TO LOOKUP HOOD ANGLE BASED ON DISTANCE
      Double hoodAngle = 0.0;
      if (targetDistance <= 55) {
        hoodAngle = -hoodEncoderMinPosition;
      } else if (targetDistance > 55 && targetDistance <= 123) {
        hoodAngle = -16.76;
      } else if (targetDistance > 123 && targetDistance <= 174) {
        hoodAngle = -21.8;
      } else if (targetDistance > 174 && targetDistance <= 215) {
        hoodAngle = -21.976;
      } else if (targetDistance > 215) {
        hoodAngle = -23.476;
      }
      pivotToHoodAngle(hoodAngle);
    }
  }

  public void pivotToHoodAngle(Double hoodAngle) {
    Double hoodEncoderPosition = hoodEncoder.getPosition();
    if (hoodEncoderPosition > hoodAngle) {
      pivotDown();
    } else if (hoodEncoderPosition < hoodAngle ) {
      pivotUp();
    } else {
      pivotStop();
    }
  }

  public void pivotUp() {
    // if (!hoodIsAtMaxElevation()) {
    if(hoodEncoder.getPosition() <= hoodEncoderMaxPosition){
      turretElevate.set(kMaxElevatePower);
    } else {
      pivotStop();
    }
  }

  public void pivotDown() {
    // if (!hoodIsAtMinElevation()) {
    if(hoodEncoder.getPosition() >= hoodEncoderMinPosition){
      turretElevate.set(-kMaxElevatePower);
    } else {
      pivotStop();
    }
  }

  public void pivotStop() {
    turretElevate.set(0.0);
  }

}
