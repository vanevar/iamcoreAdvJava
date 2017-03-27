package fr.epita.iam.datamodel;

import java.util.Date;
import fr.epita.iam.services.DateFormatManager;

public class Identity {
  // Properties
  private String displayName;
  private String uid; 
  private String email;
  private Date birthdate;
  
  //Constructors
  public Identity (){
    //Mandatory constructor
  }
  
  public Identity( String displayName, String uid, String email, Date birthdate){
    this.displayName = displayName;
    this.uid = uid;
    this.email = email;
    this.birthdate = birthdate;
  }
  
  // Getters & Setters
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  public String getUid() {
    return uid;
  }
  public void setUid(String uid) {
    this.uid = uid;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public Date getBirthdate() {
    return birthdate;
  }
  public void setBirthdate(Date birthdate) {
    this.birthdate = birthdate;
  }
  

  
  @Override
  public String toString()
  {
    DateFormatManager dfm = new DateFormatManager();
    return "Identity [ uid = " + this.uid + ", displayName = " + this.displayName +  
        ", email = " + this.email + ", birthdate = " + dfm.stringFromDate(this.birthdate) + " ]";
  }
}
