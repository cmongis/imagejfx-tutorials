/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.knoplab.md5;

import java.io.File;
import javafx.concurrent.Task;
import org.scijava.service.SciJavaService;

/**
 *
 * @author cyril
 */
public interface MD5Service extends SciJavaService{
    
    
    /**
     * Check the current directory for all files
     * then process the md5 of each file
     * @param file directory
     * @return A task representing the whole process
     */
    
    Task getCurrentTask();
    
    File getCurrentFolder();
    
    void setCurrentFolder(File folder);
    
    Task<Boolean> startMD5Processing();

    public void cancelMD5Processing();
    
    
}
