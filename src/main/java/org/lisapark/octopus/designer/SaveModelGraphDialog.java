/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.octopus.designer;

import com.google.common.io.Files;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;
import org.openide.util.Exceptions;

/**
 *
 * @author alexmy
 */
public class SaveModelGraphDialog {

    public static void saveModelGraph(DesignerFrame parent, String content, JTextArea outputTxt) {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose a directory and provide file name to save your file: ");
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int returnValue = jfc.showSaveDialog(parent);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                if (jfc.getSelectedFile().createNewFile()) {
                    Files.write(content.getBytes(), jfc.getSelectedFile());
                    outputTxt.append("\n New File: " + jfc.getSelectedFile() + " -saved.\n");
                } else if(jfc.getSelectedFile().isFile()){
                    Files.write(content.getBytes(), jfc.getSelectedFile());
                    outputTxt.append("\n File: " + jfc.getSelectedFile() + " -updated.\n");
                } else {
                    outputTxt.append("\n Please provide File name.\n");
                }
            } catch (IOException ex) {
                outputTxt.append(ex.getMessage());
            }
        }
    }
}
