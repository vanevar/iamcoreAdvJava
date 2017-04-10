package fr.epita.iam.iamcore.test;

import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.services.DateFormatManager;
import fr.epita.iam.services.IdentityJDBCDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class TestHibernateDAO {

  private static final Logger LOGGER = LogManager.getLogger(TestHibernateDAO.class);
  
  @Inject
  SessionFactory sFactory;
  
  @Inject
  IdentityJDBCDAO dao;
  
  @Test
  public void testHibernate(){
    List<Identity> identities = dao.readAllIdentities();
    int originalSize = identities.size();
    
    LOGGER.info("Before : {} ", identities);
    
    Session session = sFactory.openSession();
    DateFormatManager dfm = new DateFormatManager();
    Identity id = new Identity("Carlos Diez", 0, "cdm@gmailcom", dfm.dateFromString("1980-12-24"));
    session.saveOrUpdate(id);
    
    identities = dao.readAllIdentities();
    LOGGER.info("After : {}", identities);
    
    Assert.assertEquals(identities.size(), originalSize + 1);
  }
  
  
  
  /*
   * 
   * 
  @Test 
  public void readAll(){
    Session session = sFactory.openSession();
    List<Identity> identities = session.createQuery("from Identities").list();
    for(Identity id : identities)
    {
      System.out.println(id.toString());
     }
    Assert.assertTrue(!identities.isEmpty());
  }
  @Test
  public void HibernateSession(){
    SessionFactory session = new Configuration().configure().buildSessionFactory();
    Assert.assertNotNull(session);
  }
  
  @Test
  public void insertIdentity(){
    DateFormatManager dfm = new DateFormatManager();
    IdentityH id = new IdentityH("Carlos Diez", 0, "cdm@gmailcom", dfm.dateFromString("1980-12-24"));
    
    SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    Session session = sessionFactory.openSession();
    
    session.beginTransaction();
    session.save(id);
    session.getTransaction().commit();
    
    session.close();
  }
 
  
  */
}
