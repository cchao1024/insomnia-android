package com.cchao.insomnia.util;

/**
 * @author cchao
 * @version 8/12/18.
 */
public class ImageHelper {

    public static int getScaleHeight(int containerWidth, int width, int height) {
        return (int) (containerWidth * (float) height / width);
    }

}
