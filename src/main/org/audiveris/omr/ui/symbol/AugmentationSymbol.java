//------------------------------------------------------------------------------------------------//
//                                                                                                //
//                               A u g m e n t a t i o n S y m b o l                              //
//                                                                                                //
//------------------------------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//
//  Copyright © Audiveris 2022. All rights reserved.
//
//  This program is free software: you can redistribute it and/or modify it under the terms of the
//  GNU Affero General Public License as published by the Free Software Foundation, either version
//  3 of the License, or (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
//  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//  See the GNU Affero General Public License for more details.
//
//  You should have received a copy of the GNU Affero General Public License along with this
//  program.  If not, see <http://www.gnu.org/licenses/>.
//------------------------------------------------------------------------------------------------//
// </editor-fold>
package org.audiveris.omr.ui.symbol;

import org.audiveris.omr.glyph.Shape;
import org.audiveris.omr.math.PointUtil;
import static org.audiveris.omr.ui.symbol.Alignment.*;
import static org.audiveris.omr.ui.symbol.ShapeSymbol.decoComposite;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Class <code>AugmentationSymbol</code> implements a decorated augmentation symbol.
 *
 * @author Hervé Bitteur
 */
public class AugmentationSymbol
        extends ShapeSymbol
{
    //~ Static fields/initializers -----------------------------------------------------------------

    /** The head part. */
    private static final BasicSymbol head = Symbols.getSymbol(Shape.NOTEHEAD_BLACK);

    /** Offset ratio of dot center WRT decorated rectangle width. */
    private static final double dxRatio = +0.25;

    //~ Constructors -------------------------------------------------------------------------------
    /**
     * Create a <code>AugmentationSymbol</code> (with decoration?) standard size
     *
     * @param decorated true for a decorated image
     */
    public AugmentationSymbol (boolean decorated)
    {
        this(false, decorated);
    }

    /**
     * Create a <code>AugmentationSymbol</code> (with decoration?)
     *
     * @param isIcon    true for an icon
     * @param decorated true for a decorated image
     */
    protected AugmentationSymbol (boolean isIcon,
                                  boolean decorated)
    {
        super(isIcon, Shape.AUGMENTATION_DOT, decorated, 46);
    }

    //~ Methods ------------------------------------------------------------------------------------
    //-----------------------//
    // createDecoratedSymbol //
    //-----------------------//
    @Override
    protected ShapeSymbol createDecoratedSymbol ()
    {
        return new AugmentationSymbol(isIcon, true);
    }

    //------------//
    // createIcon //
    //------------//
    @Override
    protected ShapeSymbol createIcon ()
    {
        return new AugmentationSymbol(true, decorated);
    }

    //-----------//
    // getParams //
    //-----------//
    @Override
    protected MyParams getParams (MusicFont font)
    {
        MyParams p = new MyParams();

        // Augmentation symbol layout
        p.layout = font.layout(getString());

        if (decorated) {
            // Head layout
            p.headLayout = font.layout(head.getString());

            // Use a rectangle twice as wide as note head
            Rectangle2D hRect = p.headLayout.getBounds();
            p.rect = new Rectangle2D.Double(0, 0, 2 * hRect.getWidth(), hRect.getHeight());

            // Define specific offset
            p.offset = new Point2D.Double(dxRatio * p.rect.getWidth(), 0);
        } else {
            p.rect = p.layout.getBounds();
        }

        return p;
    }

    //-------//
    // paint //
    //-------//
    @Override
    protected void paint (Graphics2D g,
                          BasicSymbol.Params params,
                          Point2D location,
                          Alignment alignment)
    {
        MyParams p = (MyParams) params;

        if (decorated) {
            // Draw a note head (using composite) on the left side
            Point2D loc = alignment.translatedPoint(MIDDLE_LEFT, p.rect, location);
            Composite oldComposite = g.getComposite();
            g.setComposite(decoComposite);
            MusicFont.paint(g, p.headLayout, loc, MIDDLE_LEFT);
            g.setComposite(oldComposite);

            // Augmentation on right side
            PointUtil.add(loc, (3 * p.rect.getWidth()) / 4, 0);
            MusicFont.paint(g, p.layout, loc, AREA_CENTER);
        } else {
            // Augmentation alone
            Point2D loc = alignment.translatedPoint(AREA_CENTER, p.rect, location);
            MusicFont.paint(g, p.layout, loc, AREA_CENTER);
        }
    }

    //~ Inner Classes ------------------------------------------------------------------------------
    //--------//
    // Params //
    //--------//
    protected static class MyParams
            extends BasicSymbol.Params
    {

        // offset: if decorated, offset of symbol center vs decorated image center
        // layout: dot layout
        // rect:   global image (= head + dot if decorated, dot if not)
        //
        // Layout for head
        TextLayout headLayout;
    }
}
