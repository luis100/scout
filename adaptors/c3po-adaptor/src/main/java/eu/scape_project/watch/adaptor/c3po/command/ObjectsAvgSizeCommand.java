package eu.scape_project.watch.adaptor.c3po.command;

import eu.scape_project.watch.domain.PropertyValue;
import eu.scape_project.watch.utils.exceptions.InvalidJavaClassForDataTypeException;
import eu.scape_project.watch.utils.exceptions.UnsupportedDataTypeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static eu.scape_project.watch.adaptor.c3po.common.C3POConstants.CP_OBJECTS_AVG_SIZE;

/**
 * Fetches the average size of an object within the collection profile.
 * 
 * @author Petar Petrov <me@petarpetrov.org>
 * 
 * @author lfaria@keep.pt
 * 
 *         Changed at 2012-04-18: Catching new
 *         {@link PropertyValue#setValue(Object)} exceptions.
 * 
 */
public class ObjectsAvgSizeCommand extends Command {

  private final Logger LOG = LoggerFactory.getLogger(ObjectsAvgSizeCommand.class);

  /**
   * Fetches the average size (in bytes).
   * 
   * @return the value of the average size.
   */
  @Override
  public PropertyValue execute() {
    final PropertyValue pv = new PropertyValue();
    try {
      pv.setProperty(this.getProperty(CP_OBJECTS_AVG_SIZE, "The average size of objects in the collection (in bytes)"));
      pv.setValue(this.getReader().getObjectsAvgSize(), String.class);
    } catch (UnsupportedDataTypeException e) {
      LOG.error("Could not set property value", e);
    } catch (InvalidJavaClassForDataTypeException e) {
      LOG.error("Could not set property value", e);
    }
    
    return pv;
  }

}
