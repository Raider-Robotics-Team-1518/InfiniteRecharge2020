/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climb extends SubsystemBase {

  public static CANSparkMax climbMotor = new CANSparkMax(14, MotorType.kBrushless);
  private final static double kMinClimbPower = .5;

  public Climb() {

    climbMotor.setIdleMode(IdleMode.kBrake);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public static void extendClaw() {
    climbMotor.set(kMinClimbPower);
  }

  public static void climbStop() {
    climbMotor.set(0.0);
  }

  public static void retractClaw() {
    climbMotor.set(-kMinClimbPower);
  }
}
