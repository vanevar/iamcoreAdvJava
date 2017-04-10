package fr.epita.iam.iamcore.test;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Test;

import fr.epita.iam.datamodel.IdentityH;
import fr.epita.iam.services.DateFormatManager;

public class TestHibernateDAO {

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
 
  @Test 
  public void readAll(){
    SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    Session session = sessionFactory.openSession();
    List<IdentityH> identities = session.createQuery("from Identities").list();
    for(IdentityH id : identities)
    {
      System.out.println(id.toString());
     }
    Assert.assertTrue(!identities.isEmpty());
  }
}
