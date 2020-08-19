/*
 * Vizz3D - A Framework for Software Visualization
 * Copyright (C) 2006 Thomas Panas
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */


package com.obj;

import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import java.io.IOException;

import java.net.URL;

import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public class TextureReader {
    public static Texture readTexture(String filename)
        throws IOException {
        return readPixels(readImage(filename));
    }

    private static URL getResource(final String filename) {
        // Try to load resource from jar
        URL url = ClassLoader.getSystemResource(filename);

        // If not found in jar, then load from disk
        if (url == null) {
            try {
                url = new URL("file", "localhost", filename);
            } catch (Exception urlException) {
            } // ignore
        }
        return url;
    }

    private static BufferedImage readImage(String resourceName)
        throws IOException {
        URL url = getResource(resourceName);
        if (url == null) {
            throw new RuntimeException("Error reading resource " + resourceName);
        }
        return ImageIO.read(url);
    }

    private static Texture readPixels(BufferedImage img) {
        if (img == null) {
            System.err.println("Cant read image.");
            System.exit(0);
        }
        int[] packedPixels = new int[img.getWidth() * img.getHeight()];

        PixelGrabber pixelgrabber = new PixelGrabber(img, 0, 0, img.getWidth(), img.getHeight(),
                packedPixels, 0, img.getWidth());
        try {
            pixelgrabber.grabPixels();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

        ByteBuffer unpackedPixels = ByteBuffer.allocateDirect(packedPixels.length * 4);

        for (int row = img.getHeight() - 1; row >= 0; row--) {
            for (int col = 0; col < img.getWidth(); col++) {
                int packedPixel = packedPixels[(row * img.getWidth()) + col];

                unpackedPixels.put((byte) ((packedPixel >> 16) & 0xFF));
                unpackedPixels.put((byte) ((packedPixel >> 8) & 0xFF));
                unpackedPixels.put((byte) ((packedPixel >> 0) & 0xFF));
                unpackedPixels.put((byte) ((packedPixel >> 24) & 0xFF));

                //                System.err.println("putting to 1: "+((byte) ((packedPixel >> 16) & 0xFF))+
                //               		"   2: "+((byte) ((packedPixel >> 8) & 0xFF))+"  3: "+
                //              		((byte) ((packedPixel >> 0) & 0xFF)));
            }
        }
        unpackedPixels.position(0);
        return new Texture(unpackedPixels, img.getWidth(), img.getHeight());
    }

    public static class Texture {
        private ByteBuffer pixels;
        private int width;
        private int height;

        public Texture(ByteBuffer pixels, int width, int height) {
            this.height = height;
            this.pixels = pixels;
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public ByteBuffer getPixels() {
            return pixels;
        }

        public int getWidth() {
            return width;
        }
    }
}
