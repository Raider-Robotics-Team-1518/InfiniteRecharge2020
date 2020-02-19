package frc.robot.components;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;

// private final addressableLED(0);

public class LED {
    private static final int num_leds = 12;
    private static AddressableLED m_led = new AddressableLED(0);
    private static AddressableLEDBuffer m_ledBuffer;
    private static int m_rainbowFirstPixelHue = 0;
    private static boolean rainbowEnabled = true;
    
    public enum Colors { RED, YELLOW, GREEN, BLUE, BLACK, WHITE }

    public LED() {
        m_ledBuffer = new AddressableLEDBuffer(num_leds);
        m_led.setLength(num_leds);
    }

    private void draw(AddressableLEDBuffer buffer) {
        m_led.setData(buffer);
        m_led.start();

    }

    public void setSolidColor(Colors color) {
        int r = 0;
        int g = 0;
        int b = 0;
        if (color == Colors.RED) {
            r = 255;
            g = 0;
            b = 0;
        } else if (color == Colors.YELLOW) {
            r = 255;
            g = 255;
            b = 0;
        } else if (color == Colors.GREEN) {
            r = 0;
            g = 255;
            b = 0;
        } else if (color == Colors.BLUE) {
            r = 0;
            g = 0;
            b = 255;
        } else if (color == Colors.WHITE) {
            r = 220;
            g = 220;
            b = 220;
        }
        for (var i = 0; i < num_leds; i++) {
            // Sets the specified LED to the RGB values for red
            m_ledBuffer.setRGB(i, r, g, b);
         }
        draw(m_ledBuffer);
    }

    public void setRainbow() {
        if (rainbowEnabled == false) {
            return;
        }
        // For every pixel
    for (var i = 0; i < num_leds; i++) {
        // Calculate the hue - hue is easier for rainbows because the color
        // shape is a circle so only one value needs to precess
        final var hue = (m_rainbowFirstPixelHue + (i * 180 / num_leds)) % 180;
        // Set the value
        m_ledBuffer.setHSV(i, hue, 255, 128);
      }
      draw(m_ledBuffer);
      // Increase by to make the rainbow "move"
      m_rainbowFirstPixelHue += 3;
      // Check bounds
      m_rainbowFirstPixelHue %= 180;
    }

    public void enableRainbow() {
        rainbowEnabled = true;
    }

    public void disableRainbow() {
        rainbowEnabled = false;
    }
}
