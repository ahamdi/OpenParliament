package tn.openparliament.managedeputy.service;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.component.ComponentRequestLifecycle;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.SpaceUtils;
import org.exoplatform.social.core.space.impl.DefaultSpaceApplicationHandler;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.picocontainer.Startable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sawssan Selmi
 * Date: 19/07/12
 * Time: 20:42
 */
public class ManageDeputyService implements Startable {

    private SpaceService spaceService;
    private OrganizationService organizationService;;
    boolean requestStarted = false;
    private static final Log LOG = ExoLogger.getLogger(ManageDeputyService.class);

    public void createDeputy(User deputy){
        organizationService = (OrganizationService) getService(OrganizationService.class);
        spaceService= (SpaceService) getService(SpaceService.class);
        IdentityManager idm = (IdentityManager)  getService(IdentityManager.class);
        startRequest();

        try {
            organizationService.getUserHandler().createUser(deputy,true);
            endRequest();
            //startRequest();
            Space deputySpace = new Space();
            //deputySpace.setCreator(deputy.getUserName());
            deputySpace.setDisplayName("Espace de deputé " + deputy.getFullName());
            deputySpace.setPrettyName(deputy.getUserName());
            deputySpace.setType(DefaultSpaceApplicationHandler.NAME);
            String currentUser = ConversationState.getCurrent().getIdentity().getUserId();
            deputySpace.setVisibility(Space.PUBLIC);
            Identity identity = idm.getOrCreateIdentity(SpaceIdentityProvider.NAME, deputySpace.getPrettyName(), true);
            if (identity != null) {
                deputySpace.setPrettyName(SpaceUtils.buildPrettyName(deputySpace));
            }
            startRequest();
            deputySpace = spaceService.createSpace(deputySpace,currentUser);
            ArrayList<String> managers = new ArrayList<String>();
            managers.add(currentUser);
            managers.add(deputy.getUserName());
            String[] manager= new String[]{};
            manager = managers.toArray(manager);
            deputySpace.setManagers(manager);
            spaceService.updateSpace(deputySpace);
            SpaceUtils.endRequest();

        } catch (Exception e) {
            LOG.error("Error creating Deputy with his space ",e);
        }


    }

    @Override
    public void start() {
        LOG.info("#################################################  Deputy Manager Service Started  #######################################################");
    }

    @Override
    public void stop() {
        LOG.info("#################################################  Deputy Manager Service Stopped  #######################################################");
    }
    private void endRequest() {
        ExoContainer container = ExoContainerContext.getCurrentContainer();
        if (requestStarted && organizationService instanceof ComponentRequestLifecycle) {
            try {
                ((ComponentRequestLifecycle) organizationService).endRequest(container);
            } catch (Exception e) {
                LOG.warn(e.getMessage(),e);
            }
            requestStarted = false;
        }
    }

    private void startRequest() {
        ExoContainer container = ExoContainerContext.getCurrentContainer();
        if (organizationService instanceof ComponentRequestLifecycle) {
            ((ComponentRequestLifecycle) organizationService).startRequest(container);
            requestStarted = true;
        }
    }
    
    private Object getService(Class clazz){
        ExoContainer container = ExoContainerContext.getCurrentContainer();
        return clazz.cast(container.getComponentInstanceOfType(clazz));
    }
}
