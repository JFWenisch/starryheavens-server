package infrastructure.graphics;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Graphic Utils
 *
 * @author Lars Schlegelmilch
 */
public class GraphicUtils {

    /**
     * Creates a "screenshot" of the given component.
     */
    public static BufferedImage createComponentShot(final Component component) {
        if (component == null) {
            return null;
        }

        if (!component.isDisplayable()) {
            Dimension d = component.getSize();

            if (d.width == 0 || d.height == 0) {
                d = component.getPreferredSize();
                component.setSize(d);
            }

            layoutComponent(component);
        }

        final BufferedImage bufferedImage = new BufferedImage(
                component.getWidth(), component.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        component.paint(graphics2D);
        graphics2D.dispose();

        return bufferedImage;
    }

    private static void layoutComponent(Component component) {
        synchronized (component.getTreeLock()) {
            component.doLayout();

            if (component instanceof Container) {
                for (Component child : ((Container) component).getComponents()) {
                    layoutComponent(child);
                }
            }
        }
    }

    /**
     * Get a scaled image for the given size
     */
    public static BufferedImage getScaledIconImage(Image image, int width,
                                                   int height) {
        return getScaledIconImage(Collections.singletonList(image), width,
                height);
    }

    /**
     * Get best match image from list for the given size
     */
    public static BufferedImage getScaledIconImage(List<Image> imageList,
                                                   int width, int height) {
        if (imageList == null || imageList.size() == 0 || width == 0
                || height == 0) {
            return null;
        }

        Image bestImage = null;
        int bestWidth = 0;
        int bestHeight = 0;
        double bestSimilarity = 3; // Impossibly high value
        // double bestScaleFactor = 0;

        for (Image im : imageList) {
            if (im == null) {
                continue;
            }

            int iw;
            int ih;

            try {
                iw = im.getWidth(null);
                ih = im.getHeight(null);
            } catch (Exception e) {
                System.err
                        .println("getScaledIconImage: Perhaps the image passed into Java is broken. Skipping this icon.");
                continue;
            }

            if (iw > 0 && ih > 0) {
                // Calc scale factor
                double scaleFactor = Math.min((double) width / (double) iw,
                        (double) height / (double) ih);
                // Calculate scaled image dimensions
                // adjusting scale factor to nearest "good" value
                int adjw = 0;
                int adjh = 0;
                double scaleMeasure = 1; // 0 - best (no) scale, 1 - impossibly
                // bad
                if (scaleFactor >= 2) {
                    // Need to enlarge image more than twice
                    // Round down scale factor to multiply by integer value
                    scaleFactor = Math.floor(scaleFactor);
                    adjw = iw * (int) scaleFactor;
                    adjh = ih * (int) scaleFactor;
                    scaleMeasure = 1.0 - 0.5 / scaleFactor;
                } else if (scaleFactor >= 1) {
                    // Don't scale
                    scaleFactor = 1.0;
                    adjw = iw;
                    adjh = ih;
                    scaleMeasure = 0;
                } else if (scaleFactor >= 0.75) {
                    // Multiply by 3/4
                    scaleFactor = 0.75;
                    adjw = iw * 3 / 4;
                    adjh = ih * 3 / 4;
                    scaleMeasure = 0.3;
                } else if (scaleFactor >= 0.6666) {
                    // Multiply by 2/3
                    scaleFactor = 0.6666;
                    adjw = iw * 2 / 3;
                    adjh = ih * 2 / 3;
                    scaleMeasure = 0.33;
                } else {
                    // Multiply size by 1/scaleDivider
                    // where scaleDivider is minimum possible integer
                    // larger than 1/scaleFactor
                    double scaleDivider = Math.ceil(1.0 / scaleFactor);
                    scaleFactor = 1.0 / scaleDivider;
                    adjw = (int) Math.round((double) iw / scaleDivider);
                    adjh = (int) Math.round((double) ih / scaleDivider);
                    scaleMeasure = 1.0 - 1.0 / scaleDivider;
                }
                double similarity = ((double) width //
                        - (double) adjw) //
                        / (double) width //
                        + ((double) height //
                        - (double) adjh) / //
                        (double) height // Large padding is bad
                        + scaleMeasure; // Large rescale is bad
                if (similarity < bestSimilarity) {
                    bestSimilarity = similarity;
                    // bestScaleFactor = scaleFactor;
                    bestImage = im;
                    bestWidth = adjw;
                    bestHeight = adjh;
                }

                if (similarity == 0) {
                    break;
                }
            }
        }

        if (bestImage == null) {
            // No images were found, possibly all are broken
            return null;
        }

        final BufferedImage bimage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = bimage.createGraphics();
        applyRenderingHints(g);

        try {
            int x = (width - bestWidth) / 2;
            int y = (height - bestHeight) / 2;

            // if (LOG.isTraceEnabled()) {
            // LOG.trace("getScaledIconImage result: " + "w : " + width +
            // " h : " + height + " iW : "
            // + bestImage.getWidth(null) + " iH : " + bestImage.getHeight(null)
            // + " sim : " + bestSimilarity
            // + " sf : " + bestScaleFactor + " adjW : " + bestWidth +
            // " adjH : " + bestHeight + " x : " + x
            // + " y : " + y);
            // }
            g.drawImage(bestImage, x, y, bestWidth, bestHeight, null);
        } finally {
            g.dispose();
        }

        return bimage;
    }

    /**
     * @return a {@link java.awt.RenderingHints} instance with antialiasing
     *         rendering hints.
     */
    public static RenderingHints createRenderingHints() {
        final Map<RenderingHints.Key, Object> hints = new HashMap<RenderingHints.Key, Object>();
        hints.put(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        hints.put(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        hints.put(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        hints.put(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        return new RenderingHints(hints);
    }

    public static RenderingHints applyRenderingHints(Graphics2D g2d) {

        if (g2d == null) {
            throw new IllegalArgumentException("the g2d is set");
        }

        final RenderingHints oldHints = g2d.getRenderingHints();
        g2d.setRenderingHints(createRenderingHints());

        return oldHints;
    }
}
