package eu.scape_project.watch.merging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import eu.scape_project.watch.dao.DAO;
import eu.scape_project.watch.domain.Entity;
import eu.scape_project.watch.domain.PropertyValue;
import eu.scape_project.watch.domain.SourceAdaptor;

/**
 * Default rule to merge entities and property values into the knowledge base.
 * 
 * @author Luis Faria <lfaria@keep.pt>
 * 
 */
public class DefaultMergeRule implements MergeRule {

  
  @Override
  public void mergeEntity(final Entity entity) {
	String name = entity.getName();
	if (name.contains("_")){
		String[] splits = name.split("_");
		Entity newEntity = new Entity(entity.getType(),splits[0]); 
		DAO.ENTITY.save(newEntity);
	}else {
		DAO.ENTITY.save(entity);
	}
  }

  @Override
  public void mergePropertyValue(final SourceAdaptor adaptor, final PropertyValue propertyValue) {
	Entity entity = propertyValue.getEntity();
	String name = entity.getName();
	if (name.contains("_")){
		String[] splits = name.split("_");
		Entity newEntity = new Entity(entity.getType(),splits[0]);
		propertyValue.setEntity(newEntity);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(splits[1]);
		} catch (ParseException e) {
			System.out.println("ERRROROR");
			e.printStackTrace();
		}
		DAO.PROPERTY_VALUE.save(adaptor, date, propertyValue);
	}else {
		DAO.PROPERTY_VALUE.save(adaptor, propertyValue);
	}
  }

}
