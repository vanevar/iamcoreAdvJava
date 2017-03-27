package fr.epita.iam.services;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertiesReader {
  private String fileName = null;
  private Properties propFile = new Properties();
  
  private static final Logger LOGGER = LogManager.getLogger(IdentityJDBCDAO.class);
  
  public PropertiesReader(String fileName) {
    super();
    LOGGER.debug("=> Constuctor");
    this.fileName = fileName;
    initPropertyFromFileName();
    LOGGER.debug("<= Constuctor finnished with no issues");
  }
  
  private void initPropertyFromFileName()
  {
    LOGGER.debug("=> initPropertyFromFileName");
    try {
      propFile.load(this.getClass().getResourceAsStream(fileName));
    } catch (IOException e) {
      LOGGER.error("Error trying to read from properties file. ", e);
    }
    LOGGER.debug("<= initPropertyFromFileName finnished with no issues");
    
  }
  
  public String getProperty(String propertyKey)
  {
    LOGGER.debug("=> getProperty : tracing the input : {}", propertyKey);
    String value = propFile.getProperty(propertyKey);
    return LOGGER.traceExit("<= readIdentity : {}", value);
  }

}
