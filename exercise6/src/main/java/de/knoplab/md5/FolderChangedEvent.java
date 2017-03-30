/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.knoplab.md5;

import java.io.File;
import org.scijava.event.SciJavaEvent;

/**
 *
 * @author cyril
 */
public class FolderChangedEvent extends SciJavaEvent {
    
    final private File folder;

    public FolderChangedEvent(File folder) {
        this.folder = folder;
    }

    public File getFolder() {
        return folder;
    }
    
    
    
    
    
}
