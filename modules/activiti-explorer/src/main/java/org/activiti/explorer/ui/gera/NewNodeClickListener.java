package org.activiti.explorer.ui.gera;

import com.vaadin.ui.Button;
import org.activiti.explorer.ExplorerApp;

/**
 */
public class NewNodeClickListener implements Button.ClickListener {

    private static final long serialVersionUID = 1L;
    private String parentKey;

    public NewNodeClickListener(String parentKey) {
        this.parentKey = parentKey;
    }
    public void buttonClick(Button.ClickEvent event) {
        ExplorerApp.get().getViewManager().showPopupWindow(new NewNodePopupWindow(parentKey));
    }
}