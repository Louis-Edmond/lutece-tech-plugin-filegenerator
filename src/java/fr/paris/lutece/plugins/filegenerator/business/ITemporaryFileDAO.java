/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.plugins.filegenerator.business;

import java.util.List;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 *
 * ITemporaryFileDAO
 *
 */
public interface ITemporaryFileDAO
{

    /**
     * Insert a new record in the table.
     *
     * @param file
     *            instance of the File object to insert
     * @return the id of the new file
     */
    int insert( TemporaryFile file, Plugin plugin );

    /**
     * Load the data of the File from the table
     *
     * @param nId
     *            The identifier of the file
     * @return the instance of the File
     */
    TemporaryFile load( int nId, Plugin plugin );

    /**
     * Delete a record from the table
     *
     * @param nIdFile
     *            The identifier of the file
     */
    void delete( int nIdFile, Plugin plugin );

    /**
     * Update the file in the table
     *
     * @param file
     *            instance of the File object to update
     */
    void store( TemporaryFile file, Plugin plugin );

    /**
     * Load all the files of an user.
     *
     * @param user
     *            the user
     * @return the list of File
     */
    List<TemporaryFile> findByUser( AdminUser user, Plugin plugin );

    /**
     * Select files older than the given number of days
     * 
     * @param days
     */
    List<TemporaryFile> selectFilesOlderThan( int days, Plugin plugin );
}
