/*
 * Copyright 2010-2020 Australian Signals Directorate
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
package au.gov.asd.tac.constellation.testing;

import au.gov.asd.tac.constellation.functionality.welcome.WelcomePageProvider;
import au.gov.asd.tac.constellation.graph.Graph;
import au.gov.asd.tac.constellation.graph.StoreGraph;
import au.gov.asd.tac.constellation.graph.file.opener.GraphOpener;
import au.gov.asd.tac.constellation.graph.interaction.InteractiveGraphPluginRegistry;
import au.gov.asd.tac.constellation.graph.locking.DualGraph;
import au.gov.asd.tac.constellation.graph.schema.Schema;
import au.gov.asd.tac.constellation.graph.schema.SchemaFactoryUtilities;
import au.gov.asd.tac.constellation.graph.schema.analytic.AnalyticSchemaFactory;
import au.gov.asd.tac.constellation.plugins.PluginExecutor;
import au.gov.asd.tac.constellation.plugins.PluginInfo;
import au.gov.asd.tac.constellation.testing.construction.SphereGraphBuilderPlugin;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 * The New Graph with a Sphere plugin for the Welcome Page.
 *
 * @author canis_majoris
 */

@ServiceProvider(service = WelcomePageProvider.class)
@PluginInfo(tags = {"WELCOME"})
@NbBundle.Messages("SphereGraphWelcomePlugin=Sphere Graph Welcome Plugin")
public class SphereGraphWelcomePlugin extends WelcomePageProvider {

    /**
     * Get a unique reference that is used to identify the plugin 
     *
     * @return a unique reference
     */
    @Override
    public String getName() {
        return SphereGraphWelcomePlugin.class.getName();
    }
    
    /**
     * Get a description for the link that will appear on the Welcome Page 
     *
     * @return a unique reference
     */
    @Override
    public String getLinkDescription() {
        return "Open up a new graph with a Sphere Network";
    }
    
    /**
     * Get an optional textual description that appears on the Welcome Page.
     *
     * @return a unique reference
     */
    @Override
    public String getDescription() {
        StringBuilder buf = new StringBuilder();
        buf.append("<br>");
        buf.append("This will open a new graph with nodes arranged in a sphere.");
        return buf.toString();
    }
    
    /**
     * Returns a link to a resource that can be used instead of text.
     *
     * @return a unique reference
     */
    @Override
    public String getImage() {
        return null;
    }
    /**
     * This method describes what action should be taken when the 
     * link is clicked on the Welcome Page
     *
     */
    @Override
    public void run() {
        final Schema schema = SchemaFactoryUtilities.getSchemaFactory(AnalyticSchemaFactory.ANALYTIC_SCHEMA_ID).createSchema();
        final StoreGraph sg = new StoreGraph(schema);
        schema.newGraph(sg);
        
        final Graph dualGraph = new DualGraph(sg, false);
        PluginExecutor.startWith(CoreTestingPluginRegistry.SPHERE_GRAPH_BUILDER)
                .set(SphereGraphBuilderPlugin.ADD_CHARS_PARAMETER_ID, true)
                .set(SphereGraphBuilderPlugin.DRAW_MANY_DECORATORS_PARAMETER_ID, true)
                .set(SphereGraphBuilderPlugin.DRAW_MANY_TX_PARAMETER_ID, true)
                .set(SphereGraphBuilderPlugin.N_PARAMETER_ID, 100)
                .set(SphereGraphBuilderPlugin.OPTION_PARAMETER_ID, "Random vertices")
                .set(SphereGraphBuilderPlugin.T_PARAMETER_ID, 50)
                .set(SphereGraphBuilderPlugin.USE_ALL_DISPLAYABLE_CHARS_PARAMETER_ID, true)
                .set(SphereGraphBuilderPlugin.USE_LABELS_PARAMETER_ID, true)
                .set(SphereGraphBuilderPlugin.USE_RANDOM_ICONS_PARAMETER_ID, true)
                .followedBy(InteractiveGraphPluginRegistry.RESET_VIEW)
                .executeWriteLater(dualGraph);
        final String graphName = SchemaFactoryUtilities.getSchemaFactory(AnalyticSchemaFactory.ANALYTIC_SCHEMA_ID).getLabel().replace(" ", "").toLowerCase();
        GraphOpener.getDefault().openGraph(dualGraph, graphName);
    }

    /**
     * Determines whether this analytic appear on the Welcome Page 
     *
     * @return true is this analytic should be visible, false otherwise.
     */
    @Override
    public boolean isVisible() {
        return true;
    }
}