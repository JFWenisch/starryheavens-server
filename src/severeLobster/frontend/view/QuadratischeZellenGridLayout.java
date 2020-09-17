/*
 * %W% %E%
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package severeLobster.frontend.view;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Insets;

/**
 * Gridlayout, bei dem alle Zellen nicht nur gleichgross, sondern auch
 * quadratisch sind. Dabei ist das gesamte Feld immer so gross wie moeglich.
 * 
 * @author Lutz Kleiber
 * 
 */
public class QuadratischeZellenGridLayout extends GridLayout {

    public QuadratischeZellenGridLayout(int rows, int cols) {
        super(rows, cols);
    }

    /**
     * Methode ist aus original Java Source kopiert, da ich nur so das Verhalten
     * der Groessenfestlegung ueberschreiben konnte. Bis auf "NEU" kommentierten
     * Teil ist alles original Java Source.
     */
    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = getRows();
            int ncols = getColumns();
            boolean ltr = parent.getComponentOrientation().isLeftToRight();

            if (ncomponents == 0) {
                return;
            }
            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            } else {
                nrows = (ncomponents + ncols - 1) / ncols;
            }
            int w = parent.getWidth() - (insets.left + insets.right);
            int h = parent.getHeight() - (insets.top + 100);
            w = (w - (ncols - 1) * getHgap()) / ncols;
            h = (h - (nrows - 1) * getVgap()) / nrows;

            /**
             * NEU: Hoehe und Breite sollen immer identisch sein, damit alle
             * Felder quadratisch sind.
             * 
             */
            if (w != h) {
                /**
                 * Je einen der Werte auf das jeweils kleinere setzen. So nehmen
                 * die Komponenten soviel Platz ein, wie sie können, während sie
                 * noch quadratisch sind.
                 */
                if (w > h)
                    w = h;
                else
                    h = w;
            }
            /**
             * Ende NEU
             */
            if (ltr) {
                for (int c = 0, x = insets.left; c < ncols; c++, x += w
                        + getHgap()) {
                    for (int r = 0, y = insets.top; r < nrows; r++, y += h
                            + getVgap()) {
                        int i = r * ncols + c;
                        if (i < ncomponents) {
                            parent.getComponent(i).setBounds(x, y, w, h);
                        }
                    }
                }
            } else {
                for (int c = 0, x = parent.getHeight() - insets.right - w; c < ncols; c++, x -= w
                        + getHgap()) {
                    for (int r = 0, y = insets.top; r < nrows; r++, y += h
                            + getVgap()) {
                        int i = r * ncols + c;
                        if (i < ncomponents) {
                            parent.getComponent(i).setBounds(x, y, w, h);
                        }
                    }
                }
            }
        }
    }

}