package org.apache.usergrid.chop.webapp.view.main;

import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.Reindeer;
import org.apache.usergrid.chop.api.Module;
import org.apache.usergrid.chop.webapp.service.InjectorFactory;
import org.apache.usergrid.chop.webapp.dao.ModuleDao;
import org.apache.usergrid.chop.webapp.view.util.UIUtil;
import org.apache.usergrid.chop.webapp.view.user.UserSubwindow;

public class Header extends AbsoluteLayout {

    private ModuleDao moduleDao = InjectorFactory.getInstance(ModuleDao.class);

    private Label moduleLabel = UIUtil.addLabel(this, "", "left: 10px; top: 10px;", "500px");

    public Header() {
        addManageButton();
    }

    private void addManageButton() {

        Button button = UIUtil.addButton(this, "Manage", "left: 940px; top: 10px;", "80px");
        button.setStyleName(Reindeer.BUTTON_LINK);

        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                manageButtonClicked();
            }
        });
    }

    private void manageButtonClicked() {
        UI.getCurrent().addWindow( new UserSubwindow() );
    }

    void showModule(String moduleId) {

        Module module = moduleDao.get(moduleId);
        String caption = String.format(
                "<b>%s / %s / %s</b>",
                module.getGroupId(),
                module.getArtifactId(),
                module.getVersion()
        );

        moduleLabel.setValue(caption);
    }
}
