package fr.epita.iam.iamcore.test;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import fr.epita.iam.services.IdentityJDBCDAO;

public class TestPropertiesFile {

  private static final Logger LOGGER = LogManager.getLogger(IdentityJDBCDAO.class);
  
  @Test
  public void test() throws IOException{
    Properties p = new Properties();
//    p.load(new FileInputStream(new File("src/test/resources/test.properties"));
    p.load(this.getClass().getResourceAsStream("/test.properties"));
    LOGGER.info(p.get("jdbc.connection.string"));
  }
}
