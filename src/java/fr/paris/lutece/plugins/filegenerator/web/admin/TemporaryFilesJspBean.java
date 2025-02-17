/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.filegenerator.web.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rometools.utils.Strings;

import fr.paris.lutece.plugins.filegenerator.business.TemporaryFile;
import fr.paris.lutece.plugins.filegenerator.business.TemporaryFileHome;
import fr.paris.lutece.plugins.filegenerator.service.TemporaryFileService;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class provides the user interface to list temporary files
 */
@Controller( controllerJsp = "ManageMyFiles.jsp", controllerPath = "jsp/admin/plugins/filegenerator/", right = "VIEW_TEMP_FILES" )
public class TemporaryFilesJspBean extends MVCAdminJspBean
{
    private static final long serialVersionUID = 2823024035296798126L;

    // Rights
    public static final String VIEW_TEMP_FILES = "VIEW_TEMP_FILES";

    // Parameter
    public static final String PARAMETER_FILE_ID = "file_id";

    // View
    private static final String VIEW_MY_FILES = "view_myFiles";

    // Templates
    private static final String TEMPLATE_TEMPORARY_FILES = "admin/plugins/filegenerator/manage_temporary_files.html";
    private static final String PROPERTY_TITLE_MANAGE_FILES_SYSTEM = "filegenerator.manage_temporary_files.pageTitle";
    private static final String PROPERTY_MSG_DAYS_DELETE = "filegenerator.manage_temporary_files.help";

    // Marks
    private static final String MARK_FILES = "files_list";
    private static final String MARK_DAYS_DELETE = "msg_days_before_delete";

    // Messages
    private static final String MESSAGE_FILE_ACCESS_DENIED = "Access Denied to this file";

    @View( value = VIEW_MY_FILES, defaultView = true )
    public String getTemporaryFiles( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_TITLE_MANAGE_FILES_SYSTEM );
        List<TemporaryFile> listFiles = TemporaryFileHome.findByUser( getUser( ) );
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_FILES, listFiles );

        String daysBeforeDelete = AppPropertiesService.getProperty( "daemon.temporaryfilesDaemon.days.defore.delete", "30" );
        String message = I18nService.getLocalizedString( PROPERTY_MSG_DAYS_DELETE, new String [ ] {
                daysBeforeDelete
        }, getLocale( ) );
        model.put( MARK_DAYS_DELETE, message );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TEMPORARY_FILES, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    public void doDownloadFile( HttpServletRequest request, HttpServletResponse response ) throws AccessDeniedException, IOException
    {
        String strId = request.getParameter( PARAMETER_FILE_ID );
        if ( Strings.isNotEmpty( strId ) )
        {
            TemporaryFile file = TemporaryFileHome.findByPrimaryKey( Integer.valueOf( strId ) );

            if ( file.getUser( ).getUserId( ) != getUser( ).getUserId( ) )
            {
                throw new AccessDeniedException( MESSAGE_FILE_ACCESS_DENIED );
            }
            if ( file.getIdPhysicalFile( ) == null )
            {
                throw new AccessDeniedException( "File not yet generated" );
            }
            PhysicalFile physicalFile = TemporaryFileService.getInstance( ).loadPhysicalFile( file.getIdPhysicalFile( ) );
            if ( physicalFile != null )
            {
                response.setContentType( file.getTitle( ) );
                response.setHeader( "Content-Disposition", "attachment; filename=\"" + file.getTitle( ) + "\";" );
                OutputStream out = response.getOutputStream( );
                out.write( physicalFile.getValue( ) );
                out.flush( );
                out.close( );
            }
        }
    }

    public void doDeleteFile( HttpServletRequest request, HttpServletResponse response ) throws AccessDeniedException, IOException
    {
        String strId = request.getParameter( PARAMETER_FILE_ID );
        if ( Strings.isNotEmpty( strId ) )
        {
            TemporaryFile file = TemporaryFileHome.findByPrimaryKey( Integer.valueOf( strId ) );
            if ( file.getUser( ).getUserId( ) != getUser( ).getUserId( ) )
            {
                throw new AccessDeniedException( MESSAGE_FILE_ACCESS_DENIED );
            }
            TemporaryFileService.getInstance( ).removeTemporaryFile( file );
        }
        redirectView( request, VIEW_MY_FILES );
    }
}
