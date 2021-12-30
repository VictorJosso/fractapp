package fr.josso.fractales.Core;

/*
 * This class is part of MCFS (Mission Control - Flight Software) a development
 * of Team Puli Space, official Google Lunar XPRIZE contestant.
 * This class is released under Creative Commons CC0.
 * @author Zsolt Pocze, Dimitry Polivaev
 * Please like us on facebook, and/or join our Small Step Club.
 * http://www.pulispace.com
 * https://www.facebook.com/pulispace
 * http://nyomdmegteis.hu/en/
 */

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BandedSampleModel;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * A class to store big image.
 */
public class BigBufferedImage extends BufferedImage {

    private static final String TMP_DIR = System.getProperty("java.io.tmpdir");
    /**
     * The constant MAX_PIXELS_IN_MEMORY.
     */
    public static final int MAX_PIXELS_IN_MEMORY =  1024 * 1024;

    /**
     * a Constructor.
     *
     * @param width     wanted width of the image.
     * @param height    wanted height if the image.
     * @param imageType wanted type of the image.
     * @return a BufferedImage or BigBufferedImage depending on the wanted size.
     */
    public static BufferedImage create(int width, int height, int imageType) {
        if (width * height > MAX_PIXELS_IN_MEMORY) {
            try {
                final File tempDir = new File(TMP_DIR);
                return createBigBufferedImage(tempDir, width, height, imageType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new BufferedImage(width, height, imageType);
        }
    }

    /**
     * a Constructor.
     * @param tempDir the buffer directory.
     * @param width wanted width of the image.
     * @param height wanted height if the image.
     * @param imageType wanted type of the image.
     * @return a BigBufferedImage of the wanted size.
     * @throws IOException in case of unsupported image type.
     */
    private static BufferedImage createBigBufferedImage(File tempDir, int width, int height, int imageType)
            throws IOException {
        FileDataBuffer buffer = new FileDataBuffer(tempDir, width * height, 4);
        ColorModel colorModel;
        BandedSampleModel sampleModel;
        switch (imageType) {
            case TYPE_INT_RGB -> {
                colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                        new int[]{8, 8, 8, 0},
                        false,
                        false,
                        ComponentColorModel.TRANSLUCENT,
                        DataBuffer.TYPE_BYTE);
                sampleModel = new BandedSampleModel(DataBuffer.TYPE_BYTE, width, height, 3);
            }
            case TYPE_INT_ARGB -> {
                colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                        new int[]{8, 8, 8, 8},
                        true,
                        false,
                        ComponentColorModel.TRANSLUCENT,
                        DataBuffer.TYPE_BYTE);
                sampleModel = new BandedSampleModel(DataBuffer.TYPE_BYTE, width, height, 4);
            }
            default -> throw new IllegalArgumentException("Unsupported image type: " + imageType);
        }
        SimpleRaster raster = new SimpleRaster(sampleModel, buffer, new Point(0, 0));
        return new BigBufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);
    }

    /**
     * a Constructor.
     * @param cm ColorModel for the new image.
     * @param raster Raster for the image data.
     * @param isRasterPremultiplied if true, the data in the raster has been premultiplied with alpha.
     * @param properties Hashtable of String/Object pairs.
     */
    private BigBufferedImage(ColorModel cm, SimpleRaster raster, boolean isRasterPremultiplied, Hashtable<?, ?> properties) {
        super(cm, raster, isRasterPremultiplied, properties);
    }

    private static class SimpleRaster extends WritableRaster {

        /**
         * Instantiates a new Simple raster.
         *
         * @param sampleModel the sampleModel of the new SimpleRaster.
         * @param dataBuffer  the dataBuffer of the new SimpleRaster.
         * @param origin      the origin of the new SimpleRaster.
         */
        public SimpleRaster(SampleModel sampleModel, FileDataBuffer dataBuffer, Point origin) {
            super(sampleModel, dataBuffer, origin);
        }

    }

    /**
     * An intern class to dispose of the buffers.
     */
    private static final class FileDataBufferDeleterHook extends Thread {

        static {
            Runtime.getRuntime().addShutdownHook(new FileDataBufferDeleterHook());
        }

        /**
         * Set the structure that contains the buffers to dispose.
         */
        private static final HashSet<FileDataBuffer> undisposedBuffers = new HashSet<>();

        /**
         * Dispose of the buffers.
         */
        @Override
        public void run() {
            final FileDataBuffer[] buffers = undisposedBuffers.toArray(new FileDataBuffer[0]);
            for (FileDataBuffer b : buffers) {
                b.disposeNow();
            }
        }
    }

    /**
     * An intern class to buff file data.
     */
    private static class FileDataBuffer extends DataBuffer {

        private final String id = "buffer-" + System.currentTimeMillis() + "-" + ((int) (Math.random() * 1000));
        private File dir;
        private String path;
        private File[] files;
        private RandomAccessFile[] accessFiles;
        private MappedByteBuffer[] buffer;

        /**
         * Instantiates a new File data buffer.
         *
         * @param dir      the dir
         * @param size     the size
         * @param numBanks the num banks
         * @throws IOException the io exception
         */
        public FileDataBuffer(File dir, int size, int numBanks) throws IOException {
            super(TYPE_BYTE, size, numBanks);
            this.dir = dir;
            init();
        }

        /**
         * Initialise the buffer.
         * @throws IOException in case of unreachable directory.
         */
        private void init() throws IOException {
            FileDataBufferDeleterHook.undisposedBuffers.add(this);
            if (dir == null) {
                dir = new File(".");
            }
            if (!dir.exists()) {
                throw new RuntimeException("FileDataBuffer constructor parameter dir does not exist: " + dir);
            }
            if (!dir.isDirectory()) {
                throw new RuntimeException("FileDataBuffer constructor parameter dir is not a directory: " + dir);
            }
            path = dir.getPath() + "/" + id;
            File subDir = new File(path);
            subDir.mkdir();
            buffer = new MappedByteBuffer[banks];
            accessFiles = new RandomAccessFile[banks];
            files = new File[banks];
            for (int i = 0; i < banks; i++) {
                File file = files[i] = new File(path + "/bank" + i + ".dat");
                final RandomAccessFile randomAccessFile = accessFiles[i] = new RandomAccessFile(file, "rw");
                buffer[i] = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, getSize());
            }
        }

        /**
         * Getter for an element.
         * @param bank index of bank of the element.
         * @param i index of the element in bank.
         * @return the wanted element.
         */
        @Override
        public int getElem(int bank, int i) {
            return buffer[bank].get(i) & 0xff;
        }


        /**
         * Setter for an element.
         * @param bank index of bank of the element.
         * @param i index of the element in bank.
         * @param val value to store.
         */
        @Override
        public void setElem(int bank, int i, int val) {
            buffer[bank].put(i, (byte) val);
        }

        /**
         * Dispose of the buffer.
         */
        private void disposeNow() {
            this.buffer = null;
            if (accessFiles != null) {
                for (RandomAccessFile file : accessFiles) {
                    try {
                        file.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                accessFiles = null;
            }
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
                files = null;
            }
            if (path != null) {
                new File(path).delete();
                path = null;
            }
        }

    }
}