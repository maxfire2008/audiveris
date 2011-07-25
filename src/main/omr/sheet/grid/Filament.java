//----------------------------------------------------------------------------//
//                                                                            //
//                              F i l a m e n t                               //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright (C) Herve Bitteur 2000-2010. All rights reserved.               //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package omr.sheet.grid;

import omr.glyph.GlyphSection;
import omr.glyph.facets.BasicStick;
import omr.glyph.facets.Stick;

import omr.log.Logger;

import omr.sheet.Scale;

import java.util.*;

/**
 * Class {@code Filament} represents a long glyph that can be far from being a
 * straight line.
 * It is used to handle candidate staff lines and bar lines.
 */
public class Filament
    extends BasicStick
{
    //~ Static fields/initializers ---------------------------------------------

    /** Usual logger utility */
    private static final Logger logger = Logger.getLogger(Filament.class);

    /**
     * For comparing Filament instances on their starting point
     */
    public static final Comparator<Filament> startComparator = new Comparator<Filament>() {
        public int compare (Filament f1,
                            Filament f2)
        {
            // Sort on start
            return Integer.signum(f1.getStartPoint().x - f2.getStartPoint().x);
        }
    };

    /**
     * For comparing Filament instances on their stopping point
     */
    public static final Comparator<Filament> stopComparator = new Comparator<Filament>() {
        public int compare (Filament f1,
                            Filament f2)
        {
            // Sort on stop
            return Integer.signum(f1.getStopPoint().x - f2.getStopPoint().x);
        }
    };

    /**
     * For comparing Filament instances on distance from reference axis
     */
    public static final Comparator<Filament> distanceComparator = new Comparator<Filament>() {
        public int compare (Filament f1,
                            Filament f2)
        {
            // Sort on distance from top edge
            return Integer.signum(f1.refDist - f2.refDist);
        }
    };


    //~ Instance fields --------------------------------------------------------

    /** Related scale */
    private final Scale scale;

    /** Distance from reference axis */
    private Integer refDist;

    //~ Constructors -----------------------------------------------------------

    //----------//
    // Filament //
    //----------//
    /**
     * Creates a new Filament object.
     *
     * @param scale scaling data
     */
    public Filament (Scale scale)
    {
        this(scale, FilamentAlignment.class);
    }

    //----------//
    // Filament //
    //----------//
    /**
     * Creates a new Filament object.
     *
     * @param scale scaling data
     */
    public Filament (Scale                             scale,
                     Class<?extends FilamentAlignment> alignmentClass)
    {
        super(scale.interline(), alignmentClass);
        this.scale = scale;
    }

    //~ Methods ----------------------------------------------------------------

    //-------------//
    // getAncestor //
    //-------------//
    @Override
    public Filament getAncestor ()
    {
        return (Filament) super.getAncestor();
    }

    //-----------//
    // getParent //
    //-----------//
    @Override
    public Filament getPartOf ()
    {
        return (Filament) super.getPartOf();
    }

    //----------------//
    // setRefDistance //
    //----------------//
    /**
     * Remember the filament distance to reference axis
     * @param refDist the orthogonal distance to reference axis
     */
    public void setRefDistance (int refDist)
    {
        this.refDist = refDist;
    }

    //----------------//
    // getRefDistance //
    //----------------//
    /**
     * Report the orthogonal distance from the filament to the reference axis
     * @return distance from axis that takes global slope into acount
     */
    public Integer getRefDistance ()
    {
        return refDist;
    }

    //-------------------------//
    // getResultingThicknessAt //
    //-------------------------//
    /**
     * Compute the thickness at provided coordinate of the potential merge
     * between this and that filaments
     * @param that the other filament
     * @param coord the provided coordinate
     * @return the resulting thickness
     */
    public double getResultingThicknessAt (Filament that,
                                           int      coord)
    {
        double thisPos = this.getPositionAt(coord);
        double thatPos = that.getPositionAt(coord);
        double thisThickness = this.getThicknessAt(coord);
        double thatThickness = that.getThicknessAt(coord);

        return Math.abs(thisPos - thatPos) +
               ((thisThickness + thatThickness) / 2);
    }

    //------------//
    // addSection //
    //------------//
    public void addSection (GlyphSection section)
    {
        addSection(section, Linking.LINK_BACK);
    }

//    //------//
//    // dump //
//    //------//
//    @Override
//    public void dump ()
//    {
//        super.dump();
//        System.out.println("   refDist=" + getRefDistance());
//    }
//
    //---------//
    // include //
    //---------//
    /**
     * Include a whole other filament into this one
     * @param that the filament to swallow
     */
    public void include (Stick that)
    {
        for (GlyphSection section : that.getMembers()) {
            addSection(section);
        }

        that.setPartOf(this);
    }

    //-----------------//
    // invalidateCache //
    //-----------------//
    @Override
    public void invalidateCache ()
    {
        super.invalidateCache();

        refDist = null;
    }

    //---------------//
    // getPositionAt //
    //---------------//
    /**
     * Report the precise filament position for the provided coordinate .
     * @param coord the coord value (x for horizontal fil, y for vertical fil)
     * @return the pos value (y for horizontal fil, x for vertical fil)
     */
    public double positionAt (double coord)
    {
        return getAlignment()
                   .getPositionAt(coord);
    }

    //---------//
    // slopeAt //
    //---------//
    public double slopeAt (double coord)
    {
        return getAlignment()
                   .slopeAt(coord);
    }

    //------------//
    // trueLength //
    //------------//
    /**
     * Report an evaluation of how this filament is filled by sections
     * @return how solid this filament is
     */
    public int trueLength ()
    {
        return (int) Math.rint((double) getWeight() / scale.mainFore());
    }

    //--------------//
    // getAlignment //
    //--------------//
    @Override
    protected FilamentAlignment getAlignment ()
    {
        return (FilamentAlignment) super.getAlignment();
    }

    //-----------------//
    // internalsString //
    //-----------------//
    @Override
    protected String internalsString ()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(" start[x=")
          .append(getStartPoint().x)
          .append(",y=")
          .append(getStartPoint().y)
          .append("]");

        sb.append(" stop[x=")
          .append(getStopPoint().x)
          .append(",y=")
          .append(getStopPoint().y)
          .append("]");

        sb.append("meanDist:")
          .append(getMeanDistance());

        if (getPartOf() != null) {
            sb.append(" anc:")
              .append(getAncestor());
        }

        if (refDist != null) {
            sb.append(" refDist:")
              .append(refDist);
        }

        return sb.toString();
    }
}
