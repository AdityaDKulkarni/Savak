package com.savak.savak.models;

import java.io.Serializable;

/**
 * @author Aditya Kulkarni
 */
public class MembershipModel implements Serializable {
    private Integer MaxMagazines;
    private int type;
    private int meberCount;

    public Integer getMaxMagazines() {
        return MaxMagazines;
    }

    public void setMaxMagazines(Integer MaxMagazines) {
        this.MaxMagazines = MaxMagazines;
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

    private String MembershipPlan;

    public String getMembershipPlan() {
        return MembershipPlan;
    }

    public void setMembershipPlan(String MembershipPlan) {
        this.MembershipPlan = MembershipPlan;
    }

    private Integer Duration;

    public Integer getDuration() {
        return Duration;
    }

    public void setDuration(Integer Duration) {
        this.Duration = Duration;
    }

    private Boolean IsVoter;

    public Boolean getIsVoter() {
        return IsVoter;
    }

    public void setIsVoter(Boolean IsVoter) {
        this.IsVoter = IsVoter;
    }

    private Integer LedgerId;

    public Integer getLedgerId() {
        return LedgerId;
    }

    public void setLedgerId(Integer LedgerId) {
        this.LedgerId = LedgerId;
    }

    private Integer LibraryId;

    public Integer getLibraryId() {
        return LibraryId;
    }

    public void setLibraryId(Integer LibraryId) {
        this.LibraryId = LibraryId;
    }

    private Integer MaxBooks;

    public Integer getMaxBooks() {
        return MaxBooks;
    }

    public void setMaxBooks(Integer MaxBooks) {
        this.MaxBooks = MaxBooks;
    }

    private Integer MaxAnualMags;

    public Integer getMaxAnualMags() {
        return MaxAnualMags;
    }

    public void setMaxAnualMags(Integer MaxAnualMags) {
        this.MaxAnualMags = MaxAnualMags;
    }

    private String CreatedDate;

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    private Boolean IsForMagazine;

    public Boolean getIsForMagazine() {
        return IsForMagazine;
    }

    public void setIsForMagazine(Boolean IsForMagazine) {
        this.IsForMagazine = IsForMagazine;
    }

    private String AccountName;

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String AccountName) {
        this.AccountName = AccountName;
    }


    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setMeberCount(int meberCount) {
        this.meberCount = meberCount;
    }

    public int getMeberCount() {
        return meberCount;
    }
}