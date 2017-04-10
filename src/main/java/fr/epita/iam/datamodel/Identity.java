package fr.epita.iam.datamodel;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;

import fr.epita.iam.services.DateFormatManager;

@Entity
@Table(name="Identities")
public class Identity {
  // Properties
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="UId")
  private long uid;
  
  @Column(name="DisplayName")
  private String displayName;

  @Column(name="Email")
  private String email;
  
  @Column(name="Birthday")
  private Date birthdate;
  
  //Constructors
  public Identity (){
    //Mandatory constructor
  }
  
  public Identity( String displayName, long uid, String email, Date birthdate){
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
  public long getUid() {
    return uid;
  }
  public void setUid(long uid) {
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
