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
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class OI extends SubsystemBase {

  public final Joystick m_stick;
  public final XboxController m_controller;
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
  public JoystickButton intakePivotOut;
  public JoystickButton intakePivotIn;
  public JoystickButton intakePivotOutXbox;
  public JoystickButton intakePivotInXbox;
  public JoystickButton runIntake;
  public JoystickButton runIntakeXbox;
  public JoystickButton intakeJam;
  public JoystickButton intakeJamXbox;
  public final JoystickButton retractClawButton;
  public final JoystickButton extendClawButton;
  public final JoystickButton stage2button;
  public final JoystickButton stage3button;
  // public final JoystickButton climbUpButton;
  // public final JoystickButton climbDownButton;

  /**
   * Creates a new OI.
   */
  public OI() {
    m_stick = new Joystick(0);
    m_controller = new XboxController(1);

    // Joystick Buttons
    fireControl = new JoystickButton(m_controller, 8);
    fireControl.whileHeld(new Shoot());

    runIntake = new JoystickButton(m_stick, 1);
    runIntake.whileHeld(new PickupBall());

    runIntakeXbox = new JoystickButton(m_controller, 1);
    runIntakeXbox.whileHeld(new PickupBall());

    intakeJam = new JoystickButton(m_stick, 2);
    intakeJam.whileHeld(new UnjamIntake());

    intakeJamXbox = new JoystickButton(m_controller, 4);
    intakeJamXbox.whileHeld(new UnjamIntake());

    turretControlButton = new JoystickButton(m_controller, 6);
    turretControlButton.whileHeld(new AimTurret());

    intakePivotOut = new JoystickButton(m_stick, 3);
    intakePivotOut.whenPressed(() -> Robot.m_intake.pivotOut()).whenReleased(() -> Robot.m_intake.pivotStop());

    intakePivotOutXbox = new JoystickButton(m_controller, 2);
    intakePivotOutXbox.whenPressed(() -> Robot.m_intake.pivotOut()).whenReleased(() -> Robot.m_intake.pivotStop());

    intakePivotIn = new JoystickButton(m_stick, 4);
    intakePivotIn.whenPressed(() -> Robot.m_intake.pivotIn()).whenReleased(() -> Robot.m_intake.pivotStop());

    intakePivotInXbox = new JoystickButton(m_controller, 3);
    intakePivotInXbox.whenPressed(() -> Robot.m_intake.pivotIn()).whenReleased(() -> Robot.m_intake.pivotStop());

    stage2button = new JoystickButton(m_stick, 7);
    stage2button.whileHeld(new ColorWheelStage2());

    stage3button = new JoystickButton(m_stick, 11);
    stage3button.whileHeld(new ColorWheelStage3());

    retractClawButton = new JoystickButton(m_controller, 5);
    retractClawButton.whenPressed(() ->
    Robot.m_climb.retractClaw()).whenReleased(() -> Robot.m_climb.climbStop());

    extendClawButton = new JoystickButton(m_controller, 7);
    extendClawButton.whenPressed(() ->
    Robot.m_climb.extendClaw()).whenReleased(() -> Robot.m_climb.climbStop());
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
    if (m_stick.getPOV() == 90  || m_controller.getRawAxis(2) > 0.2) {
      Robot.m_turret.lookRight();
    } else if (m_stick.getPOV() == 270 || m_controller.getRawAxis(2) < -0.2) {
      Robot.m_turret.lookLeft();
    } else {
      Robot.m_turret.stopTurret();
    }

    Robot.m_turret.turretElevate.set(m_controller.getY(Hand.kLeft) * 0.5);

    // Robot.m_turret.turretPivot.set(m_controller.getRawAxis(2));


  }
}
