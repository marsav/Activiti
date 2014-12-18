/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.explorer.ui.gera;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.jobexecutor.TimerActivateProcessDefinitionHandler;
import org.activiti.engine.impl.jobexecutor.TimerSuspendProcessDefinitionHandler;
import org.activiti.engine.impl.persistence.entity.JobEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.Job;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.Messages;
import org.activiti.explorer.data.LazyLoadingContainer;
import org.activiti.explorer.ui.AbstractPage;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.custom.PrettyTimeLabel;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.activiti.explorer.ui.management.deployment.DeleteDeploymentPopupWindow;
import org.activiti.explorer.ui.process.ProcessDefinitionDetailPanel;
import org.activiti.explorer.ui.process.ProcessDefinitionPage;
import org.activiti.explorer.ui.process.listener.ConvertProcessDefinitionToModelClickListener;
import org.activiti.explorer.ui.process.listener.StartProcessInstanceClickListener;

import java.util.List;


/**
 * @author Joram Barrez
 */
public class GeraProcessDefinitionDetailPanel extends ProcessDefinitionDetailPanel {

  private static final long serialVersionUID = 1L;
  private Button stateChangeButton;

    public GeraProcessDefinitionDetailPanel(String processDefinitionId, ProcessDefinitionPage processDefinitionPage) {
    super(processDefinitionId, processDefinitionPage);
  }

    // Alfresco is supposed to not use actions here? but Gera does!
    protected void initActions(final AbstractPage parentPage) {
        System.out.println("GeraProcessDefinitionDetailPanel:initActions.processDefinition: "+processDefinition);
        ProcessDefinitionPage processDefinitionPage = (ProcessDefinitionPage) parentPage;

        startProcessInstanceButton = new Button(i18nManager.getMessage(Messages.PROCESS_START));
        startProcessInstanceButton.addListener(new StartProcessInstanceClickListener(processDefinition, processDefinitionPage));

        Button stateChangeButton = new Button(i18nManager.getMessage(processDefinition.isSuspended()?Messages.PROCESS_ACTIVATE:Messages.PROCESS_SUSPEND));

        stateChangeButton.addListener(new Button.ClickListener() {

                private static final long serialVersionUID = 1L;

                public void buttonClick(Button.ClickEvent event) {
                    GeraChangeProcessSuspensionStatePopupWindow popupWindow =
                            new GeraChangeProcessSuspensionStatePopupWindow(processDefinition.getId(), parentPage, !processDefinition.isSuspended());
                    ExplorerApp.get().getViewManager().showPopupWindow(popupWindow);
                }

            });

        if (!processDefinition.isSuspended()) {
            // Check if button must be disabled
            boolean suspendJobPending = false;
            List<Job> jobs = ProcessEngines.getDefaultProcessEngine().getManagementService()
                    .createJobQuery().processDefinitionId(processDefinition.getId()).list();
            for (Job job : jobs) {
                // TODO: this is a hack. Needs to be cleaner in engine!
                if (((JobEntity) job).getJobHandlerType().equals(TimerSuspendProcessDefinitionHandler.TYPE)) {
                    suspendJobPending = true;
                    break;
                }
            }
            stateChangeButton.setEnabled(!suspendJobPending);
        }
        else {

            // Check if already activation job pending
            boolean activateJobPending = false;
            List<Job> jobs = ProcessEngines.getDefaultProcessEngine().getManagementService()
                    .createJobQuery().processDefinitionId(processDefinition.getId()).list();
            for (Job job : jobs) {
                // TODO: this is a hack. Needs to be cleaner in engine!
                if ( ((JobEntity) job).getJobHandlerType().equals(TimerActivateProcessDefinitionHandler.TYPE) ) {
                    activateJobPending = true;
                    break;
                }
            }
            stateChangeButton.setEnabled(!activateJobPending);
        }

        editProcessDefinitionButton = new Button(i18nManager.getMessage(Messages.PROCESS_CONVERT));
        editProcessDefinitionButton.addListener(new ConvertProcessDefinitionToModelClickListener(processDefinition));

        if(((ProcessDefinitionEntity) processDefinition).isGraphicalNotationDefined() == false) {
            editProcessDefinitionButton.setEnabled(false);
        }

        // Delete button
        Button deleteButton = new Button(i18nManager.getMessage(Messages.DEPLOYMENT_DELETE));
        deleteButton.setIcon(Images.DELETE);
        deleteButton.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                ExplorerApp.get().getViewManager().showPopupWindow(new GeraDeleteDeploymentPopupWindow(deployment, parentPage));
            }
        });

        // Clear toolbar and add buttons
        processDefinitionPage.getToolBar().removeAllButtons();
        processDefinitionPage.getToolBar().addButton(startProcessInstanceButton);
        processDefinitionPage.getToolBar().addButton(stateChangeButton);
        processDefinitionPage.getToolBar().addButton(editProcessDefinitionButton);
        processDefinitionPage.getToolBar().addButton(deleteButton);
        // todo: add other func
    }
  
  protected void initUi() {
    super.initUi(); // diagram etc.
    initProcessInstancesTable();
  }
  
  protected void initProcessInstancesTable() {
    ProcessInstanceTableLazyQuery query = new ProcessInstanceTableLazyQuery(processDefinition.getId());
    
    // Header
    Label instancesTitle = new Label(i18nManager.getMessage(Messages.PROCESS_INSTANCES) + " (" + query.size() + ")");
    instancesTitle.addStyleName(ExplorerLayout.STYLE_H3);
    instancesTitle.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
    instancesTitle.addStyleName(ExplorerLayout.STYLE_NO_LINE);
    detailPanelLayout.addComponent(instancesTitle);

    if (query.size() > 0) {
      
      Label emptySpace = new Label("&nbsp;", Label.CONTENT_XHTML);
      detailPanelLayout.addComponent(emptySpace);
      
      Table instancesTable = new Table();
      instancesTable.setWidth(400, UNITS_PIXELS);
      if (query.size() > 6) {
        instancesTable.setPageLength(6);
      } else {
        instancesTable.setPageLength(query.size());
      }
      
      LazyLoadingContainer container = new LazyLoadingContainer(query);
      instancesTable.setContainerDataSource(container);
      
      // container props
      instancesTable.addContainerProperty(GeraProcessInstanceTableItem.PROPERTY_ID, String.class, null);
      instancesTable.addContainerProperty(GeraProcessInstanceTableItem.PROPERTY_BUSINESSKEY, String.class, null);
      instancesTable.addContainerProperty(GeraProcessInstanceTableItem.PROPERTY_ACTIONS, Component.class, null);
      
      // column alignment
      instancesTable.setColumnAlignment(GeraProcessInstanceTableItem.PROPERTY_ACTIONS, Table.ALIGN_CENTER);
      
      // column header
      instancesTable.setColumnHeader(GeraProcessInstanceTableItem.PROPERTY_ID, i18nManager.getMessage(Messages.PROCESS_INSTANCE_ID));
      instancesTable.setColumnHeader(GeraProcessInstanceTableItem.PROPERTY_BUSINESSKEY, i18nManager.getMessage(Messages.PROCESS_INSTANCE_BUSINESSKEY));
      instancesTable.setColumnHeader(GeraProcessInstanceTableItem.PROPERTY_ACTIONS, i18nManager.getMessage(Messages.PROCESS_INSTANCE_ACTIONS));
      
      instancesTable.setEditable(false);
      instancesTable.setSelectable(true);
      instancesTable.setNullSelectionAllowed(false);
      instancesTable.setSortDisabled(true);
      detailPanelLayout.addComponent(instancesTable);
      
    } else {
      Label noInstances = new Label(i18nManager.getMessage(Messages.PROCESS_NO_INSTANCES));
      detailPanelLayout.addComponent(noInstances);
    }
  }

    private static final String STYLE_PROCESS_HEADER_SUSPENDED = "process-state-suspended";
    private static final String STYLE_PROCESS_HEADER_ACTIVE = "process-state-active";
    // msa: copied from abstract and modified
    protected void initHeader() {
        GridLayout details = new GridLayout(2, 2);
        details.setWidth(100, UNITS_PERCENTAGE);
        details.addStyleName(ExplorerLayout.STYLE_TITLE_BLOCK);
        details.setSpacing(true);
        details.setMargin(false, false, true, false);
        details.setColumnExpandRatio(1, 1.0f);
        detailPanelLayout.addComponent(details);

        // Image
        Embedded image = new Embedded(null, Images.PROCESS_50);
        details.addComponent(image, 0, 0, 0, 1);

        // Name
        Label nameLabel = new Label(getProcessDisplayName(processDefinition));
        nameLabel.addStyleName(Reindeer.LABEL_H2);
        details.addComponent(nameLabel, 1, 0);

        // Properties
        HorizontalLayout propertiesLayout = new HorizontalLayout();
        propertiesLayout.setSpacing(true);
        details.addComponent(propertiesLayout);

        // Version
        String versionString = i18nManager.getMessage(Messages.PROCESS_VERSION, processDefinition.getVersion());
        Label versionLabel = new Label(versionString);
        versionLabel.addStyleName(ExplorerLayout.STYLE_PROCESS_HEADER_VERSION);
        propertiesLayout.addComponent(versionLabel);

        // Add deploy time
        PrettyTimeLabel deployTimeLabel = new PrettyTimeLabel(i18nManager.getMessage(Messages.PROCESS_DEPLOY_TIME),
                deployment.getDeploymentTime(), null, true);
        deployTimeLabel.addStyleName(ExplorerLayout.STYLE_PROCESS_HEADER_DEPLOY_TIME);
        propertiesLayout.addComponent(deployTimeLabel);

        // Add state
        // todo: rep real state

        String stateString = i18nManager.getMessage(processDefinition.isSuspended()?Messages.PROCESS_SUSPENDED:Messages.PROCESS_ACTIVE);
        Label stateLabel = new Label(stateString);
        if (processDefinition.isSuspended())
            stateLabel.addStyleName(STYLE_PROCESS_HEADER_SUSPENDED);
        else
            stateLabel.addStyleName(STYLE_PROCESS_HEADER_ACTIVE);
        propertiesLayout.addComponent(stateLabel);
    }
}
