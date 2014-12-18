package org.activiti.explorer.ui.gera;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import org.activiti.editor.ui.EditorProcessDefinitionDetailPanel;
import org.activiti.engine.GeraCategoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.activiti.explorer.ui.process.listener.ImportModelClickListener;
import org.activiti.explorer.ui.process.listener.NewModelClickListener;

/**
 * Created by martynas on 08/10/14.
 */
public class GeraEditorProcessDefinitionNodeDetailPanel extends EditorProcessDefinitionDetailPanel {

    private String nodeId;
    private Button newNodeButton;

    protected transient GeraCategoryService geraCategoryService = ProcessEngines.getDefaultProcessEngine().getGeraCategoryService();


    public GeraEditorProcessDefinitionNodeDetailPanel(String nodeId, GeraEditorProcessDefinitionPage processDefinitionPage) {
        this.i18nManager = ExplorerApp.get().getI18nManager();

        this.processDefinitionPage = processDefinitionPage;
//        this.modelData = repositoryService.getModel(modelId);
        this.nodeId = nodeId;

        initUi();
    }

    @Override
    protected void initActions() {
        newModelButton = new Button(i18nManager.getMessage(Messages.PROCESS_NEW));
        newModelButton.addListener(new NewModelClickListener());

        importModelButton = new Button(i18nManager.getMessage(Messages.PROCESS_IMPORT));
        importModelButton.addListener(new ImportModelClickListener());

        newNodeButton = new Button(i18nManager.getMessage(Messages.PROCESS_NODE_NEW));
        newNodeButton.addListener(new NewNodeClickListener(nodeId));


        // Clear toolbar and add 'new node' button
        processDefinitionPage.getToolBar().removeAllButtons();
        processDefinitionPage.getToolBar().removeAllAdditionalComponents();
        processDefinitionPage.getToolBar().addButton(newNodeButton);
        processDefinitionPage.getToolBar().addButton(newModelButton);
        processDefinitionPage.getToolBar().addButton(importModelButton);

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

