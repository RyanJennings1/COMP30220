package service.core;

import java.io.Serializable;

/**
 * Interface to define the state to be stored in ClientInfo objects
 * 
 * @author Rem
 *
 */
public class ClientInfo implements Serializable {
  public static final char MALE = 'M';
  public static final char FEMALE = 'F';

  public ClientInfo(String name, char sex, int age, int points, int noClaims, String licenseNumber) {
    this.name = name;
    this.gender = sex;
    this.age = age;
    this.points = points;
    this.noClaims = noClaims;
    this.licenseNumber = licenseNumber;
  }
	
  public ClientInfo() {}

  public String getName() {
    return this.name;
  }

  public void setName(String nme) {
    this.name = nme;
  }

  public char getGender() {
    return this.gender;
  }

  public void setGender(char sex) {
    this.gender = sex;
  }

  public int getAge() {
    return this.age;
  }

  public void setAge(int newAge) {
    this.age = newAge;
  }

  public int getPoints() {
    return this.points;
  }

  public void setPoints(int pnts) {
    this.points = pnts;
  }

  public int noClaims() {
    return this.noClaims;
  }

  public void setNoClaims(int nc) {
    this.noClaims = nc;
  }

  public String getLicenseNumber() {
    return this.licenseNumber;
  }

  public void setLicenseNumber(String lino) {
    this.licenseNumber = lino;
  }

  /**
   * Public fields are used as modern best practice argues that use of set/get
   * methods is unnecessary as (1) set/get makes the field mutable anyway, and
   * (2) set/get introduces additional method calls, which reduces performance.
   */
  private String name;
  private char gender;
  private int age;
  private int points;
  private int noClaims;
  private String licenseNumber;
}
