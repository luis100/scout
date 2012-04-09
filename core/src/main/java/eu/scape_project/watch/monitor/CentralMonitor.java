package eu.scape_project.watch.monitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import thewebsemantic.binding.RdfBean;
import eu.scape_project.watch.dao.AsyncRequestDAO;
import eu.scape_project.watch.dao.DOListener;
import eu.scape_project.watch.dao.PropertyValueDAO;
import eu.scape_project.watch.domain.AsyncRequest;
import eu.scape_project.watch.domain.Notification;
import eu.scape_project.watch.domain.PropertyValue;
import eu.scape_project.watch.domain.Question;
import eu.scape_project.watch.domain.Trigger;
import eu.scape_project.watch.interfaces.MonitorInterface;
import eu.scape_project.watch.notification.NotificationService;

public class CentralMonitor implements DOListener {

  private static final Logger LOG = LoggerFactory.getLogger(CentralMonitor.class);

  private List<MonitorInterface> monitors;

  private List<AsyncRequest> aRequests;

  private AsyncRequestDAO asyncRequestDAO;
  
  private NotificationService nService; 

  public CentralMonitor() {
    monitors = new ArrayList<MonitorInterface>();
    aRequests = new ArrayList<AsyncRequest>();
    LOG.info("CentralMonitor initialized");
  }

  public void addMonitor(MonitorInterface monitor) {
    monitors.add(monitor);
    monitor.registerCentralMonitor(this);
  }

  public void registerToAsyncRequest(AsyncRequestDAO ar) {
    asyncRequestDAO = ar;
    asyncRequestDAO.addDOListener(this);
    LOG.debug("CentralMonitor listening AsyncRequestDAO");
  }

  public void setNotificationService(NotificationService ns){
    nService = ns;
  }
  
  public NotificationService getNotificationService() {
    return nService;
  }
  
  public void notifyAsyncRequests(List<String> ids) {

    for (String uuid : ids) {
      AsyncRequest tmp = findAsyncRequest(uuid);
      assessRequest(tmp);
    }
    
  }

  public Collection<AsyncRequest> getAllRequests() {
    return Collections.unmodifiableCollection(aRequests);
  }
  
  public Collection<MonitorInterface> getAllMonitors() {
    return Collections.unmodifiableCollection(monitors);
  }
  
  
  @Override
  public void onUpdated(RdfBean object) {
    AsyncRequest req = (AsyncRequest) object;
    LOG.debug("adding Request to monitors " + req.getId());
    if (!aRequests.contains(req)) {
      aRequests.add(req);
      for (MonitorInterface monitor : monitors) {
        monitor.addWatchRequest(req);
      }
    }
  }

  @Override
  public void onRemoved(RdfBean object) {
    aRequests.remove(object);
    //TODO notify monitors about removal
  }

  
  
  
  
  private AsyncRequest findAsyncRequest(String uuid) {

    for (AsyncRequest i : aRequests) {
      if (i.getId().equals(uuid))
        return i;
    }

    return null;

  }

  private void assessRequest(AsyncRequest aRequest) {

    LOG.info("Assessing AsyncRequest " + aRequest.getId());
    
    for (Trigger trigger : aRequest.getTriggers()) {
      Question question = trigger.getQuestion();
      List<PropertyValue> result = PropertyValueDAO.getInstance().query(question.getSparql(), 0, 10);
      if (result.size()>0){
        notify(trigger);
      }else {
        LOG.info("Condition is not satisfied");
      }
    }
      
  }

  private void notify(Trigger trigger) {
    if (nService!=null) {
      for (Notification notification: trigger.getNotifications()) {
        nService.send(notification);
      }
    }else {
      LOG.warn("No NotificationService specified");
    }
  }
}