/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

public class ColorWheel extends SubsystemBase {

  // Color Sensor
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
  private final ColorMatch m_colorMatcher = new ColorMatch();
  private final Color kBlueTarget = ColorMatch.makeColor(0.129, .428, .443);
  private final Color kGreenTarget = ColorMatch.makeColor(.172, .577, .251);
  private final Color kRedTarget = ColorMatch.makeColor(.519, .347, .133);
  private final Color kYellowTarget = ColorMatch.makeColor(.319, .558, .124);
  private static String previousColor = "";
  private static int colorChanges = 0;
  private static boolean colorHasChanged = false;

  // Motor to rotate
  public static WPI_TalonSRX colorWheelMotor = new WPI_TalonSRX(8);
  private final static double kMaxRotateMotorPower = 0.25;
  private final static double kMinRotateMotorPower = 0.15;

  

  public ColorWheel() {
    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kGreenTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kYellowTarget);
    //colorWheelMotor.setIdleMode(IdleMode.kBrake);
   
  }

  /*
   * @Override public void periodic() {
   * 
   * }
   */
  public void stageThreeSpin() {
    // Update color value from sensor to dashboard
    // Send color data to dashboard
    Color detectedColor = colorSensor.getColor();
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
    String gameData = DriverStation.getInstance().getGameSpecificMessage();
    if (previousColor == ""){
      previousColor = getColorName(match);
      colorWheelMotor.set(kMinRotateMotorPower);
    }
    if (getColorName(match) == previousColor){
      return;
    }
    if (gameData.length() > 0) {
      switch (gameData.charAt(0)) {
        case 'B':
          if (match.color == kRedTarget) {
            System.out.println("we found blue");
            colorWheelSpinStop();
          }
          previousColor = getColorName(match);
          SmartDashboard.putString("FMS Color", "BLUE");
          break;
        case 'G':
          if (match.color == kYellowTarget) {
            System.out.println("we found green");
            colorWheelSpinStop();
          }
          previousColor = getColorName(match);
          SmartDashboard.putString("FMS Color", "GREEN");
          break;
        case 'R':
          if (match.color == kBlueTarget) {
            System.out.println("we found red");
            colorWheelSpinStop();
          }
          previousColor = getColorName(match);
          SmartDashboard.putString("FMS Color", "RED");
          break;
        case 'Y':
          if (match.color == kGreenTarget) {
            System.out.println("we found yellow");
            colorWheelSpinStop();
          }
          previousColor = getColorName(match);
          SmartDashboard.putString("FMS Color", "YELLOW");
          break;
        default:
          // This is corrupt data
          break;
      }
    } else {
      // Code for no data received yet
      colorWheelSpinStop();
    }





  }

  public void stageTwoSpin() {
    if(colorChanges > 30){
      colorWheelSpinStop();
      previousColor = "";
      return;
    }
    colorWheelMotor.set(kMaxRotateMotorPower);
    Color detectedColor = colorSensor.getColor();
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
    if(getColorName(match) != previousColor){
      previousColor = getColorName(match);
      colorChanges++;
      int numRotations = (int)Math.floor(colorChanges/8);
      SmartDashboard.putNumber("ColorWheel/Rotations", numRotations);
    }
  }
  
  public void colorWheelSpinStop() {
    colorWheelMotor.set(0.0);
  }

  
  
  
  private String getColorName(ColorMatchResult match){
    if (match.color == kRedTarget) {
      return "red";
    } else if (match.color == kYellowTarget) {
      return "yellow";
    }  else if (match.color == kBlueTarget) {
      return "blue";
    }  else if (match.color == kGreenTarget) {
      return "green";
    } else {return "";}
  }
  
    

  
  @Override
  public void periodic() {
  }

 }

