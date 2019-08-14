/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.octopus.designer;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;
import org.lisapark.koctopus.core.graph.Graph;
import org.lisapark.koctopus.util.Pair;

/**
 *
 * @author alexmy
 */
public class OpenModelGraphDialog {

    public static Pair<String,Graph> openModelGraphAsString(DesignerFrame parent, JTextArea outputTxt) {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(parent);
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = jfc.getSelectedFile();
                String modelName = selectedFile.getName();
                String json = new String(Files.readAllBytes(selectedFile.toPath()));
                Graph modelGraph = new Gson().fromJson(json, Graph.class);
                return Pair.newInstance(modelName, modelGraph);
            } catch (IOException ex) {
                outputTxt.append(ex.getMessage());
            }
        }
        return null;
    }
}
