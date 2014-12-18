package org.activiti.explorer.ui.gera;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import com.vaadin.ui.TreeTable;
import org.activiti.editor.language.json.converter.util.JsonConverterUtil;
import org.activiti.editor.ui.EditorProcessDefinitionPage;
import org.activiti.engine.GeraCategoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Model;
import org.activiti.explorer.ui.custom.ToolBar;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * Page specifically to be used within the Gera Activiti admin UI.
 *
 * Shows process definitions currently deployed on the system.
 *
 * Created by martynas on 08/10/14.
 */
public class GeraEditorProcessDefinitionPage extends EditorProcessDefinitionPage {

    private static final long serialVersionUID = 1L;

    // services
    protected transient GeraCategoryService geraCategoryService = ProcessEngines.getDefaultProcessEngine().getGeraCategoryService();


    public GeraEditorProcessDefinitionPage() {
        super();
    }

    public GeraEditorProcessDefinitionPage(String modelId) {
        super(modelId);
    }

    @Override
    protected ToolBar createMenuBar() {
        return new GeraManagementMenuBar(); // Editor Process Definition page lives in mgmt section for Alfresco UI
    }

    @Override
    protected void showProcessDefinitionDetail(String selectedModelId) {
        if (!geraCategoryService.getMap().containsKey(selectedModelId)) {
            detailPanel = new GeraEditorProcessDefinitionDetailPanel(selectedModelId, this);
            setDetailComponent(detailPanel);
            changeUrl("" + selectedModelId);
        }
        else {
            detailPanel = new GeraEditorProcessDefinitionNodeDetailPanel(selectedModelId, this);
            setDetailComponent(detailPanel);
            changeUrl("" + selectedModelId);
        }
    }



    @Override
    protected TreeTable createList() {
        final TreeTable processDefinitionTable = new TreeTable();
        processDefinitionTable.addStyleName(ExplorerLayout.STYLE_PROCESS_DEFINITION_LIST);

        // Set non-editable, selectable and full-size
        processDefinitionTable.setEditable(false);
        processDefinitionTable.setImmediate(true);
        processDefinitionTable.setSelectable(true);
        processDefinitionTable.setNullSelectionAllowed(false);
        processDefinitionTable.setSortDisabled(true);
        processDefinitionTable.setSizeFull();

        // Listener to change right panel when clicked on a model
        processDefinitionTable.addListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            public void valueChange(Property.ValueChangeEvent event) {
                if (event.getProperty().getValue()!=null) {
                    showProcessDefinitionDetail((String) event.getProperty().getValue());
                }
            }
        });

        // Create columns
//        processDefinitionTable.addGeneratedColumn("space", new ThemeImageColumnGenerator(Images.SPACER));
//        processDefinitionTable.setColumnWidth("space", 4);
//        processDefinitionTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.PROCESS_22));
//        processDefinitionTable.setColumnWidth("icon", 22);
        processDefinitionTable.addContainerProperty("name", String.class, null);
        processDefinitionTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);

        // category folders
        for (String key: geraCategoryService.getMap().keySet()) {
            Item item=processDefinitionTable.addItem(key);
            item.getItemProperty("name").setValue(geraCategoryService.getTitle(key));
            if (!geraCategoryService.get(key).isTopRoot()) {
                processDefinitionTable.setParent(key, geraCategoryService.get(key).parentKey);
            }
        }

        // models
        List<Model> modelList = repositoryService.createModelQuery().list();
        for (Model modelData : modelList) {
            Item item = processDefinitionTable.addItem(modelData.getId());
            item.getItemProperty("name").setValue(modelData.getName());
            String category = modelData.getCategory();

            // Martynas' hack to get correct category for Model:
            try {
                JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
//                System.out.println("json node targetNamespace: "+editorNode.get("targetNamespace"));
                JsonNode processTargetNamespace = JsonConverterUtil.getProperty("process_namespace", editorNode);
                if (processTargetNamespace != null && StringUtils.isNotEmpty(processTargetNamespace.asText())) {
                    category = processTargetNamespace.asText();
                }
//                BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
//                BpmnModel model = jsonConverter.convertToBpmnModel(editorNode);
            } catch (IOException e) {
                e.printStackTrace();
            }


            // set correct parent
            if (geraCategoryService.getMap().containsKey(category)) {
                processDefinitionTable.setParent(modelData.getId(), category);
            }
            processDefinitionTable.setChildrenAllowed(modelData.getId(), false);
        }

        // expand
        if (modelId!=null && geraCategoryService.get(modelId)!=null) {
            String parentKey = modelId;
            processDefinitionTable.setCollapsed(modelId, false);

            while (geraCategoryService.get(parentKey)!=null && !geraCategoryService.get(parentKey).isTopRoot()) {
                parentKey = geraCategoryService.get(parentKey).parentKey;
                processDefinitionTable.setCollapsed(parentKey, false);
            }

            processDefinitionTable.select(modelId);
            processDefinitionTable.setCurrentPageFirstItemId(modelId);
            processDefinitionTable.focus();
        }

        return processDefinitionTable;
    }
}
