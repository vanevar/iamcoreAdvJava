package fr.epita.iam.iamcore.test;

import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.services.DateFormatManager;
import fr.epita.iam.services.IdentityHibernateDAO;
import fr.epita.iam.services.IdentityJDBCDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class TestHibernateDAO {

  private static final Logger LOGGER = LogManager.getLogger(TestHibernateDAO.class);
  
  @Inject
  IdentityHibernateDAO hibernateDao;
  
  @Inject
  SessionFactory sFactory;
  
  @Inject
  IdentityJDBCDAO dao;
  
  @Test
  public void testHibernate(){
    List<Identity> identities = hibernateDao.readAllIdentities();
    
    LOGGER.info("Before : {} ", identities);
    
    DateFormatManager dfm = new DateFormatManager();
    Identity id = new Identity("Carlos Diez", 0, "cdm@gmailcom", dfm.dateFromString("1980-12-24"));
    hibernateDao.writeIdentity(id);
    
    identities = hibernateDao.readAllIdentities();
    for(Identity id2 : identities)
    {
      System.out.println(id2.toString());
     }
    LOGGER.info("Search by Id");
    System.out.println(hibernateDao.readIdentity(1).toString());
    LOGGER.info("Search by Display Name");    
    System.out.println(hibernateDao.findByDisplayName("c").toString());
    
    LOGGER.info("Update Identity");    
    id = hibernateDao.findByDisplayName("c");
    id.setDisplayName("Pepe");
    hibernateDao.updateIdentity(id);
    System.out.println(hibernateDao.findByDisplayName("p").toString());
    
    LOGGER.info("Delete Identity");    
    hibernateDao.deleteIdentity(id);
    Assert.assertTrue(hibernateDao.readAllIdentities().isEmpty());
  
  }
}
