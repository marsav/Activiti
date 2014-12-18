package org.activiti.explorer.ui.gera;

import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.Reindeer;
import org.activiti.engine.GeraCategoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.ui.AbstractPage;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.activiti.explorer.ui.process.ProcessDefinitionDetailPanel;

/**
 * Created by martynas on 15/10/14.
 */
public class GeraProcessDefinitionNodeDetailPanel extends ProcessDefinitionDetailPanel {
    private String nodeId;

    protected transient GeraCategoryService geraCategoryService;


    public GeraProcessDefinitionNodeDetailPanel() {
        super();
    }
    public GeraProcessDefinitionNodeDetailPanel(String nodeId, GeraProcessDefinitionPage parentPage) {
        this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
        this.managementService = ProcessEngines.getDefaultProcessEngine().getManagementService();
        this.formService = ProcessEngines.getDefaultProcessEngine().getFormService();
        this.geraCategoryService = ProcessEngines.getDefaultProcessEngine().getGeraCategoryService();


        this.i18nManager = ExplorerApp.get().getI18nManager();

        this.parentPage = parentPage;
        this.processDefinition = null;
        this.nodeId = nodeId;

        initUi();
    }

    @Override
    protected void initActions(AbstractPage parentPage){
        // none
    }

    @Override
    protected void initHeader() {
        GridLayout details = new GridLayout(2, 2);
        details.setWidth(100, UNITS_PERCENTAGE);
        details.addStyleName(ExplorerLayout.STYLE_TITLE_BLOCK);
        details.setSpacing(true);
        details.setMargin(false, false, true, false);
        details.setColumnExpandRatio(1, 1.0f);
        detailPanelLayout.addComponent(details);

        // Image
        Embedded image = new Embedded(null, Images.REPOSITORY_50);
        details.addComponent(image, 0, 0, 0, 1);

        // Name
        Label nameLabel = new Label(geraCategoryService.getTitle(nodeId));
        nameLabel.addStyleName(Reindeer.LABEL_H2);
        details.addComponent(nameLabel, 1, 0);

        // Properties
        HorizontalLayout propertiesLayout = new HorizontalLayout();
        propertiesLayout.setSpacing(true);
        details.addComponent(propertiesLayout);

//        // Version
//        String versionString = i18nManager.getMessage(Messages.PROCESS_VERSION, modelData.getVersion());
//        Label versionLabel = new Label(versionString);
//        versionLabel.addStyleName(ExplorerLayout.STYLE_PROCESS_HEADER_VERSION);
//        propertiesLayout.addComponent(versionLabel);
    }

    @Override
    public void initProcessDefinitionInfo() {
        // no process def info
    }
}
