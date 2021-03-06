/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;

public class Intake extends SubsystemBase {

  public static WPI_TalonSRX pivotMotor = new WPI_TalonSRX(9);
  public static CANSparkMax intakeMotor = new CANSparkMax(15, MotorType.kBrushless);
  public static CANSparkMax intake2Motor = new CANSparkMax(12, MotorType.kBrushless);
  private final double kMaxPivotMotorPower = 0.5;
  private static DigitalInput intakeFullyExtendedSwitch;
  private static DigitalInput intakeFullyRetractedSwitch;

  /**
   * Creates a new Intake.
   */
  public Intake() {
    intakeFullyExtendedSwitch = new DigitalInput(3);
    intakeFullyRetractedSwitch = new DigitalInput(4);
    intake2Motor.setInverted(true);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void pivotIn() {
    if (intakeFullyRetractedSwitch.get() == true) {
      SmartDashboard.putBoolean("LimitOut", intakeFullyExtendedSwitch.get());
      SmartDashboard.putBoolean("LimitIn", intakeFullyRetractedSwitch.get());
      pivotMotor.set(kMaxPivotMotorPower);
    } else {
      pivotStop();
    }
  }

  public void pivotOut() {
    if (intakeFullyExtendedSwitch.get() == true) {
      SmartDashboard.putBoolean("LimitOut", intakeFullyExtendedSwitch.get());
      SmartDashboard.putBoolean("LimitIn", intakeFullyRetractedSwitch.get());
      pivotMotor.set(-kMaxPivotMotorPower);
    } else {
      pivotStop();
    }
  }

  public void pivotStop(){
    pivotMotor.set(0);
  }

  public void intakeOn() {
    intakeMotor.set(0.9);
    intake2Motor.set(0.9);
  }

  public void intakeOff() {
    intakeMotor.set(0);
    intake2Motor.set(0);
  }

  public void intakeRevers(){
    intakeMotor.set(-0.9);
    intake2Motor.set(-0.9);
  }
}
