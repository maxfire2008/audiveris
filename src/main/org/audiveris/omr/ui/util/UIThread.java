//------------------------------------------------------------------------------------------------//
//                                                                                                //
//                                         U I T h r e a d                                        //
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
package org.audiveris.omr.ui.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation <code>UIThread</code> indicates a method which can be called only from the
 * UI thread (EDT: Event Dispatching Thread), or a whole class dedicated to UI.
 * <p>
 * A non-UI thread must use the {@link javax.swing.SwingUtilities#invokeLater(Runnable doRun)} or
 * {@link javax.swing.SwingUtilities#invokeAndWait(Runnable doRun)} to call such method safely.
 *
 * @author Hervé Bitteur
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UIThread
{
}
