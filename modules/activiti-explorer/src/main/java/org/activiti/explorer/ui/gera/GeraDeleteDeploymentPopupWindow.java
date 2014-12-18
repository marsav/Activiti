package org.activiti.explorer.ui.gera;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ui.AbstractPage;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.management.deployment.DeleteDeploymentPopupWindow;
import org.activiti.explorer.ui.management.deployment.DeploymentPage;

import java.util.List;

/**
 * Created by martynas on 17/10/14.
 */
public class GeraDeleteDeploymentPopupWindow extends DeleteDeploymentPopupWindow {
    private GeraProcessDefinitionPage parentPage;
    private String processDefinitionId = null;

    public GeraDeleteDeploymentPopupWindow(Deployment deployment, final AbstractPage parentPage) {
        super(deployment, null);
        this.parentPage = (GeraProcessDefinitionPage)parentPage;
    }

//    protected void initWindow() {
//        windowLayout.setSpacing(true);
//        addStyleName(Reindeer.WINDOW_LIGHT);
//        setModal(true);
//        center();
//        setCaption(i18nManager.getMessage(Messages.DEPLOYMENT_DELETE_POPUP_CAPTION, deployment.getName()));
//    }

    @Override
    protected void addButtons() {
        // Cancel
        Button cancelButton = new Button(i18nManager.getMessage(Messages.BUTTON_CANCEL));
        cancelButton.addStyleName(Reindeer.BUTTON_SMALL);
        cancelButton.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });

        // Delete
        Button deleteButton = new Button(i18nManager.getMessage(Messages.DEPLOYMENT_DELETE_POPUP_DELETE_BUTTON));
        deleteButton.addStyleName(Reindeer.BUTTON_SMALL);
        deleteButton.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                // Delete deployment, close popup window and refresh deployment list
                repositoryService.deleteDeployment(deployment.getId(), true);
                close();
                // todo: msa
                parentPage.refreshSelectNext();
            }
        });

        // Alignment
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.addComponent(cancelButton);
        buttonLayout.addComponent(deleteButton);
        addComponent(buttonLayout);
        windowLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
    }
}
