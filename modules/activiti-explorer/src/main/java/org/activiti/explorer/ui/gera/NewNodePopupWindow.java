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

import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.ui.SelectEditorComponent;
import org.activiti.engine.GeraCategoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.Messages;
import org.activiti.explorer.NotificationManager;
import org.activiti.explorer.ui.custom.PopupWindow;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;
import org.apache.commons.lang3.StringUtils;


/**
 */
public class NewNodePopupWindow extends PopupWindow implements ModelDataJsonConstants {

  private static final long serialVersionUID = 1L;

  protected I18nManager i18nManager;
  protected NotificationManager notificationManager;
  protected VerticalLayout windowLayout;
  protected GridLayout formLayout;
  protected TextField nameTextField;
  protected TextField keyTextField;

    protected TextArea descriptionTextArea;
  protected SelectEditorComponent selectEditorComponent;
  protected String parentKey = GeraCategoryService.ROOT_KEY;

  protected transient RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
  protected transient GeraCategoryService geraCategoryService = ProcessEngines.getDefaultProcessEngine().getGeraCategoryService();

  public NewNodePopupWindow(String parentKey) {
    this.windowLayout = (VerticalLayout) getContent();
    this.i18nManager = ExplorerApp.get().getI18nManager();
    this.notificationManager = ExplorerApp.get().getNotificationManager();
    this.parentKey=parentKey;
    
    initWindow();
    addFields();
    addButtons();
  }
  
  protected void initWindow() {
    windowLayout.setSpacing(true);
    addStyleName(Reindeer.WINDOW_LIGHT);
    setModal(true);
    setWidth("460px");
    setHeight("470px");
    center();
    setCaption(i18nManager.getMessage(Messages.PROCESS_NODE_NEW_POPUP_CAPTION));
  }
  
  protected void addFields() {
    formLayout = new GridLayout(2, 3);
    formLayout.setSpacing(true);

      formLayout.addComponent(new Label(i18nManager.getMessage(Messages.NODE_PARENT)));
      nameTextField = new TextField();
      nameTextField.setWidth(25, Sizeable.UNITS_EM);
      nameTextField.focus();
      formLayout.addComponent(new Label(geraCategoryService.getTitle(parentKey)));


      formLayout.addComponent(new Label(i18nManager.getMessage(Messages.NODE_KEY)));
      keyTextField = new TextField();
      keyTextField.setWidth(25, Sizeable.UNITS_EM);
      keyTextField.focus();
      formLayout.addComponent(keyTextField);

    formLayout.addComponent(new Label(i18nManager.getMessage(Messages.TASK_NAME)));
    nameTextField = new TextField();
    nameTextField.setWidth(25, Sizeable.UNITS_EM);
    nameTextField.focus();
    formLayout.addComponent(nameTextField);
    
    formLayout.addComponent(new Label(i18nManager.getMessage(Messages.TASK_DESCRIPTION)));
    descriptionTextArea = new TextArea();
    descriptionTextArea.setRows(8);
    descriptionTextArea.setWidth(25, Sizeable.UNITS_EM);
    descriptionTextArea.addStyleName(ExplorerLayout.STYLE_TEXTAREA_NO_RESIZE);
    formLayout.addComponent(descriptionTextArea);
    
//    Label editorLabel = new Label(i18nManager.getMessage(Messages.PROCESS_EDITOR_CHOICE));
//    formLayout.addComponent(editorLabel);
//    formLayout.setComponentAlignment(editorLabel, Alignment.MIDDLE_LEFT);
//
//    selectEditorComponent = new SelectEditorComponent();
//    formLayout.addComponent(selectEditorComponent);

    addComponent(formLayout);
    
    // Some empty space
    Label emptySpace = new Label("&nbsp;", Label.CONTENT_XHTML);
    addComponent(emptySpace);
  }

  protected void addButtons() {
    
    // Create
    Button createButton = new Button(i18nManager.getMessage(Messages.PROCESS_NEW_POPUP_CREATE_BUTTON));
    createButton.setWidth("200px");
    createButton.addListener(new ClickListener() {
      
      private static final long serialVersionUID = 1L;

      public void buttonClick(ClickEvent event) {
        
        if (StringUtils.isEmpty((String) nameTextField.getValue())) {
          nameTextField.setComponentError(new UserError("The name field is required."));
          return;
        }

          if (StringUtils.isEmpty((String) keyTextField.getValue())) {
              keyTextField.setComponentError(new UserError("The key field is required."));
              return;
          }

        // todo: create node
        System.out.println("create node here with key:name: "+(String) keyTextField.getValue() + ":"+(String) nameTextField.getValue() );
        geraCategoryService.put((String) keyTextField.getValue(), new GeraCategoryService.Category((String) keyTextField.getValue(), (String) nameTextField.getValue(), parentKey));

        close();
        ExplorerApp.get().getViewManager().showEditorProcessDefinitionPage(parentKey);
      }
    });
    
    // Alignment
    HorizontalLayout buttonLayout = new HorizontalLayout();
    buttonLayout.setSpacing(true);
    buttonLayout.addComponent(createButton);
    addComponent(buttonLayout);
    windowLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
  }

}
