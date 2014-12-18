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

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import com.vaadin.ui.TreeTable;
import org.activiti.engine.GeraCategoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.navigation.ProcessNavigator;
import org.activiti.explorer.navigation.UriFragment;
import org.activiti.explorer.ui.ComponentFactory;
import org.activiti.explorer.ui.custom.ToolBar;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.activiti.explorer.ui.process.ProcessDefinitionFilter;
import org.activiti.explorer.ui.process.ProcessDefinitionFilterFactory;
import org.activiti.explorer.ui.process.ProcessDefinitionPage;

import java.util.*;


/**
 * Page specifically to be used within the Alfresco Activiti admin UI.
 * 
 * Shows process definitions currently deployed on the system.
 * 
 * @author Joram Barrez
 */
public class GeraProcessDefinitionPage extends ProcessDefinitionPage {

  private static final long serialVersionUID = 1L;

    // services
    protected transient GeraCategoryService geraCategoryService = ProcessEngines.getDefaultProcessEngine().getGeraCategoryService();

    protected static final String PROPERTY_ID = "id";
    protected static final String PROPERTY_NAME = "name";
    protected static final String PROPERTY_KEY = "key";


    private ProcessDefinitionFilter definitionFilter;

    private Map<String, List<String>> versionMap = new HashMap();

    public GeraProcessDefinitionPage() {
        ExplorerApp.get().setCurrentUriFragment(
                new UriFragment(ProcessNavigator.process_URI_PART));

        // Create the filter, responsible for querying and populating process definition items
        ComponentFactory<ProcessDefinitionFilter> factory =
                ExplorerApp.get().getComponentFactory(ProcessDefinitionFilterFactory.class);
        definitionFilter = factory.create();
    }

  public GeraProcessDefinitionPage(String processDefinitionId) {
    super(processDefinitionId);
  }
  
  @Override
  protected ToolBar createMenuBar() {
    return new GeraManagementMenuBar(); // Process Definition page lives in mgmt section for Alfresco UI
  }
  
  protected void showProcessDefinitionDetail(String processDefinitionId) {
      if (!geraCategoryService.getMap().containsKey(processDefinitionId))
      {
          detailPanel = new GeraProcessDefinitionDetailPanel(processDefinitionId, this);
          setDetailComponent(detailPanel);
          changeUrl(processDefinitionId);
      }
      else {
          detailPanel = new GeraProcessDefinitionNodeDetailPanel(processDefinitionId, this);
          setDetailComponent(detailPanel);
          changeUrl("" + processDefinitionId);
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

    // Listener to change right panel when clicked on a process
        processDefinitionTable.addListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            public void valueChange(Property.ValueChangeEvent event) {
                System.out.println("valueChange: "+event.getProperty().getValue());
                if (event.getProperty().getValue()!=null) {
                    showProcessDefinitionDetail((String) event.getProperty().getValue());
                }
            }
        });

    // Create columns
//    processDefinitionTable.addGeneratedColumn("space", new ThemeImageColumnGenerator(Images.SPACER));
//    processDefinitionTable.setColumnWidth("space", 4);
//    processDefinitionTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(Images.PROCESS_22));
//    processDefinitionTable.setColumnWidth("icon", 20);
    processDefinitionTable.addContainerProperty("name", String.class, null);
    processDefinitionTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);

    // category folders
    Map<String, GeraCategoryService.Category> catMap = geraCategoryService.getMap();
    for (String key: catMap.keySet()) {
        Item item=processDefinitionTable.addItem(key);
        item.getItemProperty("name").setValue(geraCategoryService.getTitle(key));
        if (!geraCategoryService.get(key).isTopRoot()) {
            processDefinitionTable.setParent(key, geraCategoryService.get(key).parentKey);
        }
        else {
            processDefinitionTable.setCollapsed(key, false);
        }
    }

    // process defs
    List<ProcessDefinition> pdList = repositoryService.createProcessDefinitionQuery().list();
    versionMap.clear();
    for (ProcessDefinition pdData : pdList) {
        Item item = processDefinitionTable.addItem(pdData.getId());
        item.getItemProperty("name").setValue(pdData.getName());

        if (geraCategoryService.getMap().containsKey(pdData.getCategory())) {
            processDefinitionTable.setParent(pdData.getId(), pdData.getCategory());
        }
        processDefinitionTable.setChildrenAllowed(pdData.getId(), false);

        // different version of the process definition? Prepare version list.
        List<String> versions = versionMap.get(pdData.getKey());
        if (versions==null) versions = new ArrayList<String>();
        versions.add(pdData.getId());
        versionMap.put(pdData.getKey(),versions);
    }

    // Nest versions into branches.
    for (String key: versionMap.keySet()) {
        if (versionMap.get(key).size()>1) {
            List<String> versions = versionMap.get(key);
            Collections.sort(versions);
            // the last version becomes parent, all the rest nested as version-childs
            String parentProcId = versions.get(versions.size()-1);
            System.out.println("parent process: "+parentProcId);
            processDefinitionTable.setChildrenAllowed(parentProcId, true);

            for (int i=versions.size()-2; i>=0; i--) {
                System.out.println("  child process: "+versions.get(i));
                processDefinitionTable.setParent(versions.get(i), parentProcId);
            }
        }
    }

    // expand to the parameter
    if (processDefinitionId!=null && geraCategoryService.get(processDefinitionId)!=null) {
        String parentKey = processDefinitionId;
        processDefinitionTable.setCollapsed(processDefinitionId, false);

        do {
            parentKey = geraCategoryService.get(parentKey).parentKey;
            processDefinitionTable.setCollapsed(parentKey, false);
        } while (!geraCategoryService.get(parentKey).isTopRoot());

        processDefinitionTable.select(processDefinitionId);
        processDefinitionTable.setCurrentPageFirstItemId(processDefinitionId);
        processDefinitionTable.focus();
    }

    return processDefinitionTable;
}

//    public ProcessDefinitionListQuery.ProcessDefinitionListItem createItem(ProcessDefinition processDefinition) {
//        ProcessDefinitionListQuery.ProcessDefinitionListItem item = new ProcessDefinitionListQuery.ProcessDefinitionListItem();
//        item.addItemProperty(PROPERTY_ID, new ObjectProperty<String>(processDefinition.getId()));
//        item.addItemProperty(PROPERTY_NAME, new ObjectProperty<String>(getProcessDisplayName(processDefinition)));
//        item.addItemProperty(PROPERTY_KEY, new ObjectProperty<String>(processDefinition.getKey()));
//        return item;
//    }

//    protected String getProcessDisplayName(ProcessDefinition processDefinition) {
//        if(processDefinition.getName() != null) {
//            return processDefinition.getName();
//        } else {
//            return processDefinition.getKey();
//        }
//    }

//    public void refreshSelected() {
//        String pageIndex = (String) table.getCurrentPageFirstItemId();
//        ProcessDefinitionListQuery.ProcessDefinitionListItem selectedIndex = (ProcessDefinitionListQuery.ProcessDefinitionListItem) table.getValue();
////        table.removeAllItems();
//
//        // Remove all items
////        table.getContainerDataSource().removeAllItems();
//
//        // Select the same one in the list
//
////        table.setCurrentPageFirstItemIndex(pageIndex);
//        table.setCurrentPageFirstItemId(pageIndex);
////        selectElement(selectedIndex);
//        table.select(selectedIndex);
//        table.setCurrentPageFirstItemId(selectedIndex);
//
//        showProcessDefinitionDetail((String)selectedIndex.getItemProperty("id").getValue());
//
////        addMenuBar();
//
//    }

    public void refreshSelected() {
        String pageIndex = (String) table.getCurrentPageFirstItemId();
        String selectedIndex = (String) table.getValue();
System.out.println("selectedIndex: "+selectedIndex);
        table.setCurrentPageFirstItemId(pageIndex);
        table.select(selectedIndex);
        table.setCurrentPageFirstItemId(selectedIndex);

        showProcessDefinitionDetail(selectedIndex);
    }

    /**
     * Refresh the list on the left side and selects the next element in the table.
     * (useful when element of the list is deleted)
     */
    @Override
    public void refreshSelectNext() {
        Integer pageIndex = table.getCurrentPageFirstItemIndex();
        String selectNextValue = (String) table.nextItemId(table.getValue());
        if (selectNextValue==null)
            selectNextValue=(String) table.prevItemId(table.getValue());

System.out.println("selectedIndex: "+table.getValue()+", next: "+selectNextValue);

        table.removeItem(table.getValue());

        // Try to select the next one in the list
        Integer max = table.getContainerDataSource().size();
        if (max != 0) {
            if(pageIndex > max) {
                pageIndex = max -1;
            }
            table.setCurrentPageFirstItemIndex(pageIndex);
            if (selectNextValue!=null) {
                table.setValue(selectNextValue);
            }
        } else {
            table.setCurrentPageFirstItemIndex(0);
        }
    }
}
