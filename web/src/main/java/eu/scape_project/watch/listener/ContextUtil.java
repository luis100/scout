package eu.scape_project.watch.listener;

import javax.servlet.ServletContext;

import eu.scape_project.watch.adaptor.AdaptorManager;
import eu.scape_project.watch.interfaces.SchedulerInterface;
import eu.scape_project.watch.linking.DataLinker;
import eu.scape_project.watch.merging.DataMerger;

/**
 * Utility class to manage object instances kept in servlet context.
 * 
 * @author Luis Faria <lfaria@keep.pt>
 * 
 */
public final class ContextUtil {

  /**
   * The adaptor manager identifier within the servlet context.
   */
  private static final String SCOUT_ADAPTORMANAGER = "scout.core.adaptormanager";

  /**
   * The adaptor manager identifier within the servlet context.
   */
  private static final String SCOUT_DATA_MERGER = "scout.core.datamerger";

  /**
   * The scheduler identifier within the servlet context.
   */
  private static final String SCOUT_SCHEDULER = "scout.core.scheduler";

  /**
   * The data linker identifier within the servlet context.
   */
  private static final String SCOUT_DATA_LINKER = "scout.core.datalinker";

  /**
   * No instances of this utility class should exist.
   */
  private ContextUtil() {

  }

  /**
   * Get adaptor manager context instance.
   * 
   * @param context
   *          The servlet context.
   * @return The adaptor manager context instance, or <code>null</code> if none
   *         exists.
   */
  public static AdaptorManager getAdaptorManager(final ServletContext context) {
    AdaptorManager manager = (AdaptorManager) context.getAttribute(SCOUT_ADAPTORMANAGER);
    if (manager == null) {
      manager = new AdaptorManager();
      setAdaptorManager(manager, context);
    }

    return manager;
  }

  /**
   * Set the adaptor manager context instance.
   * 
   * @param manager
   *          The adaptor manager.
   * @param context
   *          The servlet context.
   */
  public static void setAdaptorManager(final AdaptorManager manager, final ServletContext context) {
    context.setAttribute(SCOUT_ADAPTORMANAGER, manager);
  }

  /**
   * Get data merger context instance.
   * 
   * @param context
   *          The servlet context.
   * @return The data merger context instance, or <code>null</code> if none
   *         exists.
   */
  public static DataMerger getDataMerger(final ServletContext context) {
    return (DataMerger) context.getAttribute(SCOUT_DATA_MERGER);
  }

  /**
   * Set the data merger context instance.
   * 
   * @param merger
   *          The data merger.
   * @param context
   *          The servlet context.
   */
  public static void setDataMerger(final DataMerger merger, final ServletContext context) {
    context.setAttribute(SCOUT_DATA_MERGER, merger);
  }

  /**
   * Get data linker context instance.
   * 
   * @param context
   *          The servlet context.
   * @return The data linker context instance, or <code>null</code> if none
   *         exists.
   */
  public static DataLinker getDataLinker(final ServletContext context) {
    return (DataLinker) context.getAttribute(SCOUT_DATA_LINKER);
  }

  /**
   * Set the data linker context instance.
   * 
   * @param linker
   *          The data linker.
   * @param context
   *          The servlet context.
   */
  public static void setDataLinker(final DataLinker linker, final ServletContext context) {
    context.setAttribute(SCOUT_DATA_LINKER, linker);
  }

  /**
   * Get scheduler context instance.
   * 
   * @param context
   *          The servlet context.
   * @return The scheduler context instance, or <code>null</code> if none
   *         exists.
   */
  public static SchedulerInterface getScheduler(final ServletContext context) {
    return (SchedulerInterface) context.getAttribute(SCOUT_SCHEDULER);
  }

  /**
   * Set the scheduler context instance.
   * 
   * @param scheduler
   *          The scheduler.
   * @param context
   *          The servlet context.
   */
  public static void setScheduler(final SchedulerInterface scheduler, final ServletContext context) {
    context.setAttribute(SCOUT_DATA_LINKER, scheduler);
  }

}
