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

package org.apache.directory.studio.valueeditors.administrativerole;


import org.apache.directory.studio.ldapbrowser.common.dialogs.TextDialog;
import org.apache.directory.studio.valueeditors.AbstractDialogStringValueEditor;
import org.eclipse.swt.widgets.Shell;


/**
 * Implementation of IValueEditor for attribute administrativeRole.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AdministrativeRoleValueEditor extends AbstractDialogStringValueEditor
{

    /**
     * {@inheritDoc}
     * 
     * This implementation opens the AdministrativeRoleDialog.
     */
    public boolean openDialog( Shell shell )
    {
        Object value = getValue();
        if ( value != null && value instanceof String )
        {
            AdministrativeRoleDialog dialog = new AdministrativeRoleDialog( shell, ( String ) value );
            if ( dialog.open() == TextDialog.OK && !"".equals( dialog.getAdministrativeRole() ) ) //$NON-NLS-1$
            {
                setValue( dialog.getAdministrativeRole() );
                return true;
            }
        }
        return false;
    }

}