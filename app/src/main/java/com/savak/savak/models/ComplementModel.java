package com.savak.savak.models;

import java.io.Serializable;

/**
 * @author Aditya Kulkarni
 */
public class ComplementModel implements Serializable {
    private String AboutThePerson;
    private int type;

    public String getAboutThePerson() {
        return AboutThePerson;
    }

    public void setAboutThePerson(String AboutThePerson) {
        this.AboutThePerson = AboutThePerson;
    }

    private String ScannedCopy;

    public String getScannedCopy() {
        return ScannedCopy;
    }

    public void setScannedCopy(String ScannedCopy) {
        this.ScannedCopy = ScannedCopy;
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

    private String ComplementDate;

    public String getComplementDate() {
        return ComplementDate;
    }

    public void setComplementDate(String ComplementDate) {
        this.ComplementDate = ComplementDate;
    }

    private String Description;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    private String CreatedBy;

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
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

    private String ComplementsBy;

    public String getComplementsBy() {
        return ComplementsBy;
    }

    public void setComplementsBy(String ComplementsBy) {
        this.ComplementsBy = ComplementsBy;
    }

    private Integer LibraryId;

    public Integer getLibraryId() {
        return LibraryId;
    }

    public void setLibraryId(Integer LibraryId) {
        this.LibraryId = LibraryId;
    }

    private String Details;

    public String getDetails() {
        return Details;
    }

    public void setDetails(String Details) {
        this.Details = Details;
    }

    private String CreatedDate;

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }


    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}