package com.savak.savak.models;

import java.io.Serializable;

/**
 * @author Aditya Kulkarni
 */

public class ManagementBodyModel implements Serializable {
    private String DateOfBirth;
    private String website;

    public int getType() {
        return type;
    }

    private int type;

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String DateOfBirth) {
        this.DateOfBirth = DateOfBirth;
    }

    private String LastModifiedBy;

    public String getLastModifiedBy() {
        return LastModifiedBy;
    }

    public void setLastModifiedBy(String LastModifiedBy) {
        this.LastModifiedBy = LastModifiedBy;
    }

    private String LastModifiedDate;

    public String getLastModifiedDate() {
        return LastModifiedDate;
    }

    public void setLastModifiedDate(String LastModifiedDate) {
        this.LastModifiedDate = LastModifiedDate;
    }

    private String CreatedBy;

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    private Boolean IsContactPerson;

    public Boolean getIsContactPerson() {
        return IsContactPerson;
    }

    public void setIsContactPerson(Boolean IsContactPerson) {
        this.IsContactPerson = IsContactPerson;
    }

    private String FirstName;

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    private String MobileNo1;

    public String getMobileNo1() {
        return MobileNo1;
    }

    public void setMobileNo1(String MobileNo1) {
        this.MobileNo1 = MobileNo1;
    }

    private String LogoImage;

    public String getLogoImage() {
        return LogoImage;
    }

    public void setLogoImage(String LogoImage) {
        this.LogoImage = LogoImage;
    }

    private Boolean IsActive;

    public Boolean getIsActive() {
        return IsActive;
    }

    public void setIsActive(Boolean IsActive) {
        this.IsActive = IsActive;
    }

    private Integer SrNo;

    public Integer getSrNo() {
        return SrNo;
    }

    public void setSrNo(Integer SrNo) {
        this.SrNo = SrNo;
    }

    private String MobileNo2;

    public String getMobileNo2() {
        return MobileNo2;
    }

    public void setMobileNo2(String MobileNo2) {
        this.MobileNo2 = MobileNo2;
    }

    private String Title;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    private Integer Gender;

    public Integer getGender() {
        return Gender;
    }

    public void setGender(Integer Gender) {
        this.Gender = Gender;
    }

    private String MiddleName;

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String MiddleName) {
        this.MiddleName = MiddleName;
    }

    private String ManagementBodyName;

    public String getManagementBodyName() {
        return ManagementBodyName;
    }

    public void setManagementBodyName(String ManagementBodyName) {
        this.ManagementBodyName = ManagementBodyName;
    }

    private Integer LibraryId;

    public Integer getLibraryId() {
        return LibraryId;
    }

    public void setLibraryId(Integer LibraryId) {
        this.LibraryId = LibraryId;
    }

    private String CreatedDate;

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    private String FromDate;

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String FromDate) {
        this.FromDate = FromDate;
    }

    private String ToDate;

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String ToDate) {
        this.ToDate = ToDate;
    }

    private String LastName;

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    private String ProfileIdentity;

    public String getProfileIdentity() {
        return ProfileIdentity;
    }

    public void setProfileIdentity(String ProfileIdentity) {
        this.ProfileIdentity = ProfileIdentity;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}