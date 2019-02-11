package com.savak.savak.models;

import java.io.Serializable;

/**
 * @author Aditya Kulkarni
 */

public class SocialProjectModel implements Serializable{
    private int type;
    private String LongDesc;
    private String website;

    public String getLongDesc() {
        return LongDesc;
    }

    public void setLongDesc(String LongDesc) {
        this.LongDesc = LongDesc;
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

    private String ShortDesc;

    public String getShortDesc() {
        return ShortDesc;
    }

    public void setShortDesc(String ShortDesc) {
        this.ShortDesc = ShortDesc;
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

    private Integer LibraryId;

    public Integer getLibraryId() {
        return LibraryId;
    }

    public void setLibraryId(Integer LibraryId) {
        this.LibraryId = LibraryId;
    }

    private String ProjectTitle;

    public String getProjectTitle() {
        return ProjectTitle;
    }

    public void setProjectTitle(String ProjectTitle) {
        this.ProjectTitle = ProjectTitle;
    }

    private String CreatedDate;

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    private Boolean ShowPromo;

    public Boolean getShowPromo() {
        return ShowPromo;
    }

    public void setShowPromo(Boolean ShowPromo) {
        this.ShowPromo = ShowPromo;
    }

    private String ProjectDate;

    public String getProjectDate() {
        return ProjectDate;
    }

    public void setProjectDate(String ProjectDate) {
        this.ProjectDate = ProjectDate;
    }

    private String PhotoImage;

    public String getPhotoImage() {
        return PhotoImage;
    }

    public void setPhotoImage(String PhotoImage) {
        this.PhotoImage = PhotoImage;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}