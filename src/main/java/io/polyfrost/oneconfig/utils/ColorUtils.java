package io.polyfrost.oneconfig.utils;

import io.polyfrost.oneconfig.config.OneConfigConfig;

import java.awt.*;

public class ColorUtils {

    public static int getColor(int currentColor, int colorPalette, boolean hover, boolean click) {
        float[] color = splitColor(currentColor);
        if(click) {
            switch (colorPalette) {
                case -1:
                    return OneConfigConfig.GRAY_500_80;
                default:
                case 0:
                    return OneConfigConfig.GRAY_400_80;
                case 1:
                    return OneConfigConfig.BLUE_600_80;
            }
        }

        switch (colorPalette) {
            case -1:
                return getColorComponents(color, splitColor(OneConfigConfig.TRANSPARENT), splitColor(OneConfigConfig.GRAY_500), hover, 10f);
            default:
            case 0:
                return getColorComponents(color, splitColor(OneConfigConfig.GRAY_500), splitColor(OneConfigConfig.GRAY_400), hover, 130f);
            case 1:
                return getColorComponents(color, splitColor(OneConfigConfig.BLUE_600), splitColor(OneConfigConfig.BLUE_500), hover, 150f);

        }

    }

    private static float[] splitColor(int color) {
        return new float[] { (color >> 16 & 255) / 255f, (color >> 8 & 255) / 255f, (color & 255) / 255f, (color >> 24 & 255) /255f };
    }

    private static int getColorComponents(float[] currentColor, float[] initColor, float[] finalColor, boolean hover, float speed) {
        currentColor[0] = smooth(currentColor[0], initColor[0], finalColor[0], hover, speed);
        currentColor[1] = smooth(currentColor[1], initColor[1], finalColor[1], hover, speed);
        currentColor[2] = smooth(currentColor[2], initColor[2], finalColor[2], hover, speed);
        currentColor[3] = smooth(currentColor[3], initColor[3], finalColor[3], hover, speed);

        return new Color(currentColor[0], currentColor[1], currentColor[2], currentColor[3]).getRGB();

    }

    private static float smooth(float current, float min, float max, boolean moveToFinal, float speed) {
        current = MathUtils.easeOut(current, moveToFinal ? 1f : 0f, speed);
        if(current <= min) {
            current = min;
        }

        if(current >= max) {
            current = max;
        }
        return current;
    }
}