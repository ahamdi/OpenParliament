package tn.openparliament.managedBeans;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.impl.UserImpl;
import tn.openparliament.managedeputy.service.ManageDeputyService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sawssan Selmi
 * Date: 19/07/12
 * Time: 20:32
 */
@ViewScoped
@ManagedBean(name = "manageDeputyBean")
public class ManageDeputyBean implements Serializable {

    private static final long serialVersionUID = -6239437588285327644L;

    ManageDeputyService manageDeputyervice;
    OrganizationService organizationService;
    private String deputyname;
    private String deputyPrenom;
    private String deputyUsername;
    private String deputyEmail;
    private String deputyPassword;

    public ManageDeputyBean() {
        manageDeputyervice = (ManageDeputyService) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(ManageDeputyService.class);
        organizationService = (OrganizationService) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(OrganizationService.class);
    }

    public String createDeputy(){


        User deputy = new UserImpl();
        deputy.setFirstName(deputyPrenom);
        deputy.setLastName(deputyname);
        deputy.setUserName(deputyUsername);
        deputy.setEmail(deputyEmail);
        deputy.setPassword(deputyPassword);




        try {
            manageDeputyervice.createDeputy(deputy);
            return "deputyCreated";
        } catch (Exception e) {
            e.printStackTrace();
            return "errorDeputyCreated";
        }
    }

    public String getDeputyname() {
        return deputyname;
    }

    public void setDeputyname(String deputyname) {
        this.deputyname = deputyname;
    }

    public String getDeputyPrenom() {
        return deputyPrenom;
    }

    public void setDeputyPrenom(String deputyPrenom) {
        this.deputyPrenom = deputyPrenom;
    }

    public String getDeputyEmail() {
        return deputyEmail;
    }

    public void setDeputyEmail(String deputyEmail) {
        this.deputyEmail = deputyEmail;
    }

    public String getDeputyPassword() {
        return deputyPassword;
    }

    public void setDeputyPassword(String deputyPassword) {
        this.deputyPassword = deputyPassword;
    }

    public void setDeputyUsername(String deputyUsername) {
        this.deputyUsername = deputyUsername;
    }

    public String getDeputyUsername() {

        return deputyUsername;
    }
}
