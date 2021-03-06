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
package org.apache.directory.studio.schemaeditor.view.wizards;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.directory.studio.schemaeditor.PluginUtils;
import org.apache.directory.studio.schemaeditor.model.Schema;
import org.apache.directory.studio.schemaeditor.model.io.XMLSchemaFileExporter;
import org.apache.directory.studio.schemaeditor.view.ViewUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;


/**
 * This class represents the wizard to export schemas as XML.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class ExportSchemasAsXmlWizard extends Wizard implements IExportWizard
{
    /** The selected schemas */
    private Schema[] selectedSchemas = new Schema[0];

    // The pages of the wizard
    private ExportSchemasAsXmlWizardPage page;


    /**
     * {@inheritDoc}
     */
    public void addPages()
    {
        // Creating pages
        page = new ExportSchemasAsXmlWizardPage();
        page.setSelectedSchemas( selectedSchemas );

        // Adding pages
        addPage( page );
    }


    /**
     * {@inheritDoc}
     */
    public boolean performFinish()
    {
        // Saving the dialog settings
        page.saveDialogSettings();

        // Getting the schemas to be exported and where to export them
        final Schema[] selectedSchemas = page.getSelectedSchemas();
        int exportType = page.getExportType();
        if ( exportType == ExportSchemasAsXmlWizardPage.EXPORT_MULTIPLE_FILES )
        {
            final String exportDirectory = page.getExportDirectory();
            try
            {
                getContainer().run( false, false, new IRunnableWithProgress()
                {
                    public void run( IProgressMonitor monitor )
                    {
                        monitor.beginTask(
                            Messages.getString( "ExportSchemasAsXmlWizard.ExportingSchemas" ), selectedSchemas.length ); //$NON-NLS-1$
                        for ( Schema schema : selectedSchemas )
                        {
                            monitor.subTask( schema.getSchemaName() );

                            try
                            {
                                BufferedWriter buffWriter = new BufferedWriter( new FileWriter( exportDirectory + "/" //$NON-NLS-1$
                                    + schema.getSchemaName() + ".xml" ) ); //$NON-NLS-1$
                                buffWriter.write( XMLSchemaFileExporter.toXml( schema ) );
                                buffWriter.close();
                            }
                            catch ( IOException e )
                            {
                                PluginUtils
                                    .logError(
                                        NLS
                                            .bind(
                                                Messages.getString( "ExportSchemasAsXmlWizard.ErrorWhenSavingSchema" ), new String[] { schema.getSchemaName() } ), e ); //$NON-NLS-1$
                                ViewUtils
                                    .displayErrorMessageDialog(
                                        Messages.getString( "ExportSchemasAsXmlWizard.Error" ), NLS.bind( Messages.getString( "ExportSchemasAsXmlWizard.ErrorWhenSavingSchema" ), new String[] { schema.getSchemaName() } ) ); //$NON-NLS-1$ //$NON-NLS-2$
                            }
                            monitor.worked( 1 );
                        }
                        monitor.done();
                    }
                } );
            }
            catch ( InvocationTargetException e )
            {
                // Nothing to do (it will never occur)
            }
            catch ( InterruptedException e )
            {
                // Nothing to do.
            }
        }
        else if ( exportType == ExportSchemasAsXmlWizardPage.EXPORT_SINGLE_FILE )
        {
            final String exportFile = page.getExportFile();
            try
            {
                getContainer().run( false, false, new IRunnableWithProgress()
                {
                    public void run( IProgressMonitor monitor )
                    {
                        monitor.beginTask( Messages.getString( "ExportSchemasAsXmlWizard.ExportingSchemas" ), 1 ); //$NON-NLS-1$
                        try
                        {
                            BufferedWriter buffWriter = new BufferedWriter( new FileWriter( exportFile ) );
                            buffWriter.write( XMLSchemaFileExporter.toXml( selectedSchemas ) );
                            buffWriter.close();
                        }
                        catch ( IOException e )
                        {
                            PluginUtils.logError( Messages
                                .getString( "ExportSchemasAsXmlWizard.ErrorWhenSavingSchemas" ), e ); //$NON-NLS-1$
                            ViewUtils
                                .displayErrorMessageDialog(
                                    Messages.getString( "ExportSchemasAsXmlWizard.Error" ), Messages.getString( "ExportSchemasAsXmlWizard.ErrorWhenSavingSchemas" ) ); //$NON-NLS-1$ //$NON-NLS-2$
                        }
                        monitor.worked( 1 );
                        monitor.done();
                    }
                } );
            }
            catch ( InvocationTargetException e )
            {
                // Nothing to do (it will never occur)
            }
            catch ( InterruptedException e )
            {
                // Nothing to do.
            }
        }

        return true;
    }


    /**
     * {@inheritDoc}
     */
    public void init( IWorkbench workbench, IStructuredSelection selection )
    {
        setNeedsProgressMonitor( true );
    }


    /**
     * Sets the selected projects.
     *
     * @param schemas
     *      the schemas
     */
    public void setSelectedSchemas( Schema[] schemas )
    {
        selectedSchemas = schemas;
    }
}
