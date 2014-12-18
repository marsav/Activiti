package org.activiti.explorer.ui.gera;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ui.AbstractPage;
import org.activiti.explorer.ui.management.processdefinition.ChangeProcessSuspensionStatePopupWindow;

import java.util.Date;

/**
 * Created by martynas on 10/10/14.
 */
public class GeraChangeProcessSuspensionStatePopupWindow extends ChangeProcessSuspensionStatePopupWindow {
    public GeraChangeProcessSuspensionStatePopupWindow(String processDefinitionId, AbstractPage parentPage, boolean suspend) {
        super(processDefinitionId, parentPage, suspend);
    }

    protected void addOkButton(final boolean suspend) {
        verticalLayout.addComponent(new Label("&nbsp", Label.CONTENT_XHTML));
        verticalLayout.addComponent(new Label("&nbsp", Label.CONTENT_XHTML));

        Button okButton = new Button(i18nManager.getMessage(Messages.BUTTON_OK));
        verticalLayout.addComponent(okButton);
        verticalLayout.setComponentAlignment(okButton, Alignment.BOTTOM_CENTER);

        okButton.addListener(new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(Button.ClickEvent event) {
                RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
                boolean includeProcessInstances = (Boolean) includeProcessInstancesCheckBox.getValue();

                if (suspend) {
                    repositoryService.suspendProcessDefinitionById(processDefinitionId,
                            includeProcessInstances, (Date) dateField.getValue());
                } else {
                    repositoryService.activateProcessDefinitionById(processDefinitionId,
                            includeProcessInstances, (Date) dateField.getValue());
                }
                ((GeraProcessDefinitionPage)parentPage).refreshSelected(); // try to refresh page

                close();
            }

        });
    }
}
