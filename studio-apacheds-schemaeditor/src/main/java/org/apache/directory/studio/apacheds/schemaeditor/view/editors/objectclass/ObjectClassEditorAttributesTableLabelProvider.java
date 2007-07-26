/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */

package org.apache.directory.studio.apacheds.schemaeditor.view.editors.objectclass;


import org.apache.directory.studio.apacheds.schemaeditor.Activator;
import org.apache.directory.studio.apacheds.schemaeditor.PluginConstants;
import org.apache.directory.studio.apacheds.schemaeditor.model.AttributeTypeImpl;
import org.apache.directory.studio.apacheds.schemaeditor.view.ViewUtils;
import org.apache.directory.studio.apacheds.schemaeditor.view.editors.NonExistingAttributeType;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * This class is the Label Provider for the Attributes Table of the Object Class Editor.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class ObjectClassEditorAttributesTableLabelProvider extends LabelProvider implements ITableLabelProvider
{
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    public Image getColumnImage( Object element, int columnIndex )
    {
        if ( ( element instanceof AttributeTypeImpl ) || ( element instanceof NonExistingAttributeType ) )
        {
            return AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, PluginConstants.IMG_ATTRIBUTE_TYPE )
                .createImage();
        }

        // Default
        return null;
    }


    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    public String getColumnText( Object element, int columnIndex )
    {
        if ( element instanceof AttributeTypeImpl )
        {
            AttributeTypeImpl at = ( AttributeTypeImpl ) element;

            String[] names = at.getNames();
            if ( ( names != null ) && ( names.length > 0 ) )
            {
                return ViewUtils.concateAliases( names ) + "  -  (" + at.getOid() + ")";
            }
            else
            {
                return "(None)  -  (" + at.getOid() + ")";
            }
        }
        else if ( element instanceof NonExistingAttributeType )
        {
            return ( ( NonExistingAttributeType ) element ).getDisplayName();
        }

        // Default
        return null;
    }
}
