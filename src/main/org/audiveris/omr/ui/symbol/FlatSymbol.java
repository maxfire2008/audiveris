//------------------------------------------------------------------------------------------------//
//                                                                                                //
//                                      F l a t S y m b o l                                       //
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
import org.audiveris.omr.sig.inter.AlterInter;
import static org.audiveris.omr.ui.symbol.Alignment.AREA_CENTER;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Class <code>FlatSymbol</code> handles a flat or double-flat symbol with a refPoint.
 *
 * @author Hervé Bitteur
 */
public class FlatSymbol
        extends ShapeSymbol
{
    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Creates a new FlatSymbol object.
     *
     * @param shape the related shape
     * @param codes the codes for MusicFont characters
     */
    public FlatSymbol (Shape shape,
                       int... codes)
    {
        this(false, shape, codes);
    }

    /**
     * Creates a new FlatSymbol object.
     *
     * @param isIcon true for an icon
     * @param shape  the related shape
     * @param codes  the codes for MusicFont characters
     */
    protected FlatSymbol (boolean isIcon,
                          Shape shape,
                          int... codes)
    {
        super(isIcon, shape, false, codes);
    }

    //~ Methods ------------------------------------------------------------------------------------
    //----------//
    // getModel //
    //----------//
    @Override
    public AlterInter.Model getModel (MusicFont font,
                                      Point location)
    {
        final Params p = getParams(font);
        final AlterInter.Model model = new AlterInter.Model();
        model.box = p.rect;
        model.translate(p.vectorTo(location));

        return model;
    }

    //------------//
    // createIcon //
    //------------//
    @Override
    protected ShapeSymbol createIcon ()
    {
        return new FlatSymbol(true, shape, codes);
    }

    //-----------//
    // getParams //
    //-----------//
    @Override
    protected Params getParams (MusicFont font)
    {
        final Params p = new Params();
        p.layout = font.layout(getString());
        p.rect = p.layout.getBounds();
        p.offset = new Point2D.Double(0, -p.rect.getY() - p.rect.getHeight() / 2);

        return p;
    }

    //-------//
    // paint //
    //-------//
    @Override
    protected void paint (Graphics2D g,
                          Params p,
                          Point2D location,
                          Alignment alignment)
    {
        final Point2D loc = alignment.translatedPoint(AREA_CENTER, p.rect, location);
        PointUtil.subtraction(loc, p.offset);
        OmrFont.paint(g, p.layout, loc, AREA_CENTER);
    }
}
