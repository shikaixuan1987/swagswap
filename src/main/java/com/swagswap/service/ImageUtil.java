package com.swagswap.service;

/*  
 * Corendal NetApps Framework - Java framework for web applications  
 * Copyright (C) Thierry Danard  
 *  
 * This program is free software; you can redistribute it and|or  
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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 * Borrowed from http://sourceforge.net/projects/corendalfm/
 * com.corendal.netapps.framework.core.utils.ImageUtil
 * Not used yet. I have to strip out all ImageIO calls first since
 * it's blacklisted. Eventually can be used to crop images.
 */
public final class ImageUtil {
    /**
     * Private constructor to stop anyone from instantiating this class - the
     * static methods should be used explicitly.
     */
    private ImageUtil() {
        // this class contains only static methods
    }

    /**
     * Returns the dimension of an image that is passed as an input stream.
     */
    public static final Dimension getDimension(InputStream is)
            throws IOException {
        if (is != null) {
            BufferedImage bsrc = ImageIO.read(is);
            if (bsrc != null) {
                int width = bsrc.getWidth();
                int height = bsrc.getHeight();
                return new Dimension(width, height);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Returns the dimension of an image that is passed as a byte array.
     */
    public static final Dimension getDimension(byte[] image) throws IOException {
        /*
         * initialize the result
         */
        Dimension result = null;
        ByteArrayInputStream is = null;

        /*
         * do the conversion
         */
        try {
            is = new ByteArrayInputStream(image);
            result = ImageUtil.getDimension(is);
        } finally {
            close(is);
        }

        /*
         * return
         */
        return result;
    }

    /**
     * Returns true if the specified image has transparent pixels.
     */
    public static final boolean getHasAlpha(Image image) {
        /*
         * if buffered image, the color model is readily available
         */
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }

        /*
         * use a pixel grabber to retrieve the image's color model; grabbing a
         * single pixel is usually sufficient
         */
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            // nothing to do about it
        }

        /*
         * get the image's color model
         */
        ColorModel cm = pg.getColorModel();

        /*
         * return
         */
        return cm.hasAlpha();
    }

    /**
     * Converts an image to the JPEG format.
     */
    public static final byte[] getConvertedBytesToJPEG(byte[] photo)
            throws IOException {
        /*
         * initialize the result
         */
        byte[] result = null;

        /*
         * initialize the input streams
         */
        ByteArrayInputStream is = null;
        ByteArrayOutputStream os = null;

        try {
            /*
             * get the streams
             */
            is = new ByteArrayInputStream(photo);
            os = new ByteArrayOutputStream();

            /*
             * get the content of the image
             */
            BufferedImage image = ImageIO.read(is);

            /*
             * write the image
             */
            ImageIO.write(image, "jpeg", os);

            /*
             * recreate the array of bytes
             */
            result = os.toByteArray();
        } finally {
            /*
             * close the streams
             */
            close(is);
            close(os);
        }

        /*
         * return
         */
        return result;
    }

    /**
     * Resizes a photo to a standard width that is passed as a byte array.
     */
    public static final byte[] getNormalizedPhotoByWidth(byte[] photo,
            int currentWidth, int currentHeight, int maxWidth)
            throws IOException {
        if (currentWidth > maxWidth) {
            /*
             * initialize the result
             */
            byte[] result = null;

            /*
             * initialize the input streams
             */
            ByteArrayInputStream is = null;
            ByteArrayOutputStream os = null;

            try {
                /*
                 * get the streams
                 */
                is = new ByteArrayInputStream(photo);
                os = new ByteArrayOutputStream();

                /*
                 * find the new height
                 */
                int maxHeight = ((currentHeight * maxWidth) / currentWidth);

                /*
                 * get the content of the image
                 */
                BufferedImage in = ImageIO.read(is);

                /*
                 * create a new image of the smaller size
                 */
                BufferedImage out = null;
                if (getHasAlpha(in)) {
                    out = new BufferedImage(maxWidth, maxHeight,
                            BufferedImage.TYPE_INT_ARGB);
                } else {
                    out = new BufferedImage(maxWidth, maxHeight,
                            BufferedImage.TYPE_INT_RGB);
                }

                /*
                 * get the graphics associated with the new image
                 */
                Graphics g = out.getGraphics();

                /*
                 * draw the original image into the new image, scaling on the
                 * fly
                 */
                g.drawImage(in, 0, 0, maxWidth, maxHeight, null);

                /*
                 * dispose the graphics object
                 */
                g.dispose();

                /*
                 * write out the new image
                 */
                ImageIO.write(out, "jpeg", os);

                /*
                 * recreate the array of bytes
                 */
                result = os.toByteArray();
            } finally {
                /*
                 * close the streams
                 */
                close(is);
                close(os);
            }

            /*
             * return
             */
            return result;
        } else {
            return photo;
        }
    }

    
	private static void close(ByteArrayInputStream is) {
		/*
		 * close the streams
		 */
		if (is != null) {
		    try {
		        is.close();
		    } catch (Throwable e) {
		        // nothing to do about it
		    }

		    try {
		        is = null;
		    } catch (Throwable e) {
		        // nothing to do about it
		    }
		}
	}

	
    private static final void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (Throwable e) {
                // nothing to do about it
            }

            try {
                os = null;
            } catch (Throwable e) {
                // nothing to do about it
            }
        }
    }
}
