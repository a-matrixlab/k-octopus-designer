/* 
 * Copyright (c) 2013 Lisa Park, Inc. (www.lisa-park.net).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Lisa Park, Inc. (www.lisa-park.net) - initial API and implementation and/or initial documentation
 */
package org.lisapark.octopus.designer;

import com.jidesoft.plaf.LookAndFeelFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import org.apache.commons.lang.SystemUtils;
import org.lisapark.koctopus.core.RepositoryException;
import org.openide.util.Exceptions;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public class DesignerApplication {
    
    
    private static final String[] REPO_PATH = {"file:///Users/alexmylnikov1/.m2/repository/k-octopus/k-octopus-processors/0.7.3/k-octopus-processors-0.7.3-jar-with-dependencies.jar"};
    static final String TOP_PACKAGE = "org.lisapark.koctopus.processors";

    public static void main(String[] args) throws IOException {
        try {
            // Reference to the license
            com.jidesoft.utils.Lm.verifyLicense("Lisa Park", "Octopus Designer", "zS6180HbbJpdVY1yArGP4blHYyvg6mK2");

            List<String> repoPathList = new ArrayList<>(Arrays.asList(REPO_PATH));
            String os = SystemUtils.OS_NAME;
            if (os.toLowerCase().startsWith("windows")) {
                LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
            } else {
                LookAndFeelFactory.installJideExtension(5);
            }

            final DesignerFrame designerFrame = new DesignerFrame(repoPathList);
            designerFrame.loadInitialDataFromRepository(REPO_PATH, TOP_PACKAGE);
            SwingUtilities.invokeLater(() -> {
                designerFrame.setVisible(true);
            });
        } catch (RepositoryException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
