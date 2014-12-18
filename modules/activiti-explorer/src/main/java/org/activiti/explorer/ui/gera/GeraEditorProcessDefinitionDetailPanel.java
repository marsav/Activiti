package org.activiti.explorer.ui.gera;

import org.activiti.editor.ui.EditorProcessDefinitionDetailPanel;
import org.activiti.editor.ui.EditorProcessDefinitionPage;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.ui.custom.DetailPanel;

/**
 * Created by martynas on 08/10/14.
 */
public class GeraEditorProcessDefinitionDetailPanel extends EditorProcessDefinitionDetailPanel {


    public GeraEditorProcessDefinitionDetailPanel(String modelId, GeraEditorProcessDefinitionPage processDefinitionPage) {
        this.i18nManager = ExplorerApp.get().getI18nManager();

        this.processDefinitionPage = processDefinitionPage;
        this.modelData = repositoryService.getModel(modelId);

        initUi();
    }
}
