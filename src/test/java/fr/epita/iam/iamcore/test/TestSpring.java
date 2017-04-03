package fr.epita.iam.iamcore.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.services.IdentityJDBCDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class TestSpring {

  @Autowired
  IdentityJDBCDAO dao;
  
  @Test
  public void testSpringContext(){
    dao.writeIdentity(new Identity("TB", "1", "mail@.com", new Date()));
  }
}
