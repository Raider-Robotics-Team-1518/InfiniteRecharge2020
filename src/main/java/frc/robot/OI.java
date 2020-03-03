/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.commands.*;
import frc.robot.subsystems.Climb;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class OI extends SubsystemBase {

  public final Joystick m_stick;
  public static double liveX;
  public static double liveY;
  public static double liveZ;
  public static double livePow;
  // public final JoystickButton turretUp = new JoystickButton(m_stick, 6);
  // public final JoystickButton turretDown = new JoystickButton(m_stick, 4);
  // public final JoystickButton turretLeft = new JoystickButton(m_stick, 3);
  // public final JoystickButton turretRight = new JoystickButton(m_stick, 5);
  public final JoystickButton fireControl;
  public final JoystickButton turretControlButton;
  public final JoystickButton intakePivotOut;
  public final JoystickButton intakePivotIn;
  public final JoystickButton runIntake;
  public final JoystickButton climbUpButton;
  public final JoystickButton climbDownButton;
  public final JoystickButton wedgieButton;
  public final JoystickButton testButton;
  public final JoystickButton retractClawButton;
  public final JoystickButton extendClawButton;

  private static boolean wedgieIsExtended = false;

  /**
   * Creates a new OI.
   */
  public OI() {
    System.out.println("OI is being loaded");
    m_stick = new Joystick(0);
    fireControl = new JoystickButton(m_stick, 1);
    fireControl.whileHeld(new Shoot());
    turretControlButton = new JoystickButton(m_stick, 2);
    turretControlButton.whileHeld(new AimTurret());

    intakePivotOut = new JoystickButton(m_stick, 5);
    intakePivotIn = new JoystickButton(m_stick, 6);
    runIntake = new JoystickButton(m_stick, 7);
    runIntake.whileHeld(new PickupBall());

    wedgieButton = new JoystickButton(m_stick, 9);
    climbUpButton = new JoystickButton(m_stick, 12);
    climbDownButton = new JoystickButton(m_stick, 11);
    testButton = new JoystickButton(m_stick, 10);
    retractClawButton = new JoystickButton(m_stick, 3);
    extendClawButton = new JoystickButton(m_stick, 4);
    testButton.whileHeld(new UnjamIntake());
    wedgieButton.whenPressed(() -> {
      if (wedgieIsExtended) {
        Wedgie.lockWedgie();
      } else {
        Wedgie.unlockWedgie();
      }
    }).whenReleased(() -> {
      wedgieIsExtended = !wedgieIsExtended;
    });
    extendClawButton.whenPressed(() -> {
      Climb.extendClaw();
    }).whenReleased(() -> {
      Climb.climbStop();
    });
    retractClawButton.whenPressed(() -> {
      Climb.retractClaw();
    }).whenReleased(() -> {
      Climb.climbStop();
    });
  }

  public void get() {
    liveX = m_stick.getX();
    liveY = m_stick.getY();
    liveZ = m_stick.getZ();
    livePow = m_stick.getThrottle();

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (m_stick.getPOV() == 90) {
      Robot.m_turret.lookRight();
    } else if (m_stick.getPOV() == 270) {
      Robot.m_turret.lookLeft();
    } else {
      Robot.m_turret.stopTurret();
    }



  }
}
