/*
 * Copyright 2010-2019 Australian Signals Directorate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.gov.asd.tac.constellation.views.dataaccess.panes;

import au.gov.asd.tac.constellation.preferences.utilities.PreferenceUtilites;
import au.gov.asd.tac.constellation.views.JavaFxTopComponent;
import au.gov.asd.tac.constellation.graph.Graph;
import au.gov.asd.tac.constellation.preferences.ApplicationPreferenceKeys;
import au.gov.asd.tac.constellation.security.proxy.ProxyUtilities;
import au.gov.asd.tac.constellation.views.dataaccess.io.ParameterIOUtilities;
import javafx.application.Platform;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays the Data Access View.
 * <p>
 */
@TopComponent.Description(
        preferredID = "DataAccessViewTopComponent",
        iconBase = "au/gov/asd/tac/constellation/views/dataaccess/panes/resources/data-access-view.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(
        mode = "explorer",
        openAtStartup = false
)
@ActionID(
        category = "Window",
        id = "au.gov.asd.tac.constellation.views.dataaccess.panes.DataAccessViewTopComponent"
)
@ActionReferences({
    @ActionReference(path = "Menu/Views", position = 400),
    @ActionReference(path = "Shortcuts", name = "CS-D")
})
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_DataAccessViewAction",
        preferredID = "DataAccessViewTopComponent"
)
@Messages({
    "CTL_DataAccessViewAction=Data Access View",
    "CTL_DataAccessViewTopComponent=Data Access View",
    "HINT_DataAccessViewTopComponent=Data Access View"
})
public final class DataAccessViewTopComponent extends JavaFxTopComponent<DataAccessPane> {

    private DataAccessPane dataAccessViewPane;

    public DataAccessViewTopComponent() {
        setName(Bundle.CTL_DataAccessViewTopComponent());
        setToolTipText(Bundle.HINT_DataAccessViewTopComponent());

        initComponents();

        dataAccessViewPane = new DataAccessPane(DataAccessViewTopComponent.this);
        initContent();

        addAttributeCountChangeHandler(graph -> {
            if (dataAccessViewPane != null) {
                dataAccessViewPane.update(graph);
            }
        });

        ProxyUtilities.setProxySelector(null);
    }

    /**
     * The pane used for the data access view.
     * <p>
     * This gives us a parent for dialog boxes, for example.
     *
     * @return The pane used for the data access view.
     */
    public DataAccessPane getDataAccessPane() {
        return dataAccessViewPane;
    }

    @Override
    public DataAccessPane createContent() {
        return dataAccessViewPane;
    }

    @Override
    public String createStyle() {
        return "resources/data-access-view.css";
    }

    @Override
    public void handleComponentOpened() {
        PreferenceUtilites.addPreferenceChangeListener(ApplicationPreferenceKeys.OUTPUT2_PREFERENCE, this);
    }

    @Override
    public void handleComponentClosed() {
        PreferenceUtilites.removePreferenceChangeListener(ApplicationPreferenceKeys.OUTPUT2_PREFERENCE, this);
    }

    @Override
    protected void handleNewGraph(final Graph graph) {
        if (dataAccessViewPane != null) {
            dataAccessViewPane.update(graph);
            Platform.runLater(() -> {
                ParameterIOUtilities.loadDataAccessState(dataAccessViewPane, graph);
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
