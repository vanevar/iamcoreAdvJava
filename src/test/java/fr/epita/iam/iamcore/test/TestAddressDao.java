package fr.epita.iam.iamcore.test;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.epita.iam.datamodel.Address;
import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.services.AddressHibernateDAO;
import fr.epita.iam.services.Dao;
import fr.epita.iam.services.DateFormatManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class TestAddressDao {

  private static final Logger LOGGER = LogManager.getLogger(TestHibernateDAO.class);
  
  @Inject 
  AddressHibernateDAO addrDao;
  
  @Inject
  Dao<Identity> idDao;
  
  @Test
  public void testAddressHibernate(){
    LOGGER.info("Creating Identity for Address");
    DateFormatManager dfm = new DateFormatManager();
    Identity id = new Identity("Carlos Diez", "cdm@gmailcom"
        , dfm.dateFromString("1980-12-24"), null);
  
    LOGGER.info("Test Writing Address");
    Address address = new Address(id, "65 Avenue Kennedy", null, "Paris", "75002", "France");
    addrDao.write(address);
    
    LOGGER.info("Test Identity was persited by Address DAO");
    List<Identity >identities = idDao.search(new Identity(null, null, null, null));
    for(Identity id2 : identities)
    {
      System.out.println(id2.toString());
    }
  
    LOGGER.info("Test Address Search by first line");
    List<Address> addresses = addrDao.search(new Address (null, "kEN", null, null, null, null ));
    for(Address addr : addresses)
    {
      System.out.println(addr.toString());
    }
    
    LOGGER.info("Test Update Address");
    Address modifiedAddr = addresses.get(0);
    modifiedAddr.setSecondLine("Depto. 101");
    addrDao.update(modifiedAddr);
    System.out.println(addrDao.search(new Address(null, null, "101", null,null, null)));
      
    LOGGER.info("Search Identity by Address");
    identities = addrDao.searchIdentityByAddr(new Address(null, null, "101", null, null, null));
    for(Identity id2 : identities)
    {
      System.out.println(id2.toString());
    }
    
    LOGGER.info("Delete address");
    addrDao.delete(modifiedAddr);
    Assert.assertTrue(addrDao.search(new Address(null, null, null, null, null, null)).isEmpty());
    
  }
}
