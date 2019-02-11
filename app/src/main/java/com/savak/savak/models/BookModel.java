package com.savak.savak.models;

import java.io.Serializable;

/**
 * @author Aditya Kulkarni
 */

public class BookModel implements Serializable {

    private int LibraryId;
    private int MemberId;
    private int type;
    private int BookCount;
    private double totalAmount;
    private String Author, BookType, PublisherName, BookTitle, BookImage, BookInwardNo, BookNo, UserName, Member, MemberImage;
    private String website;

    public int getMemberId() {
        return MemberId;
    }

    public void setMemberId(int memberId) {
        MemberId = memberId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getMember() {
        return Member;
    }

    public void setMember(String member) {
        Member = member;
    }

    public String getMemberImage() {
        return MemberImage;
    }

    public void setMemberImage(String memberImage) {
        MemberImage = memberImage;
    }

    public int getBookCount() {
        return BookCount;
    }

    public void setBookCount(int bookCount) {
        BookCount = bookCount;
    }

    public String getBookInwardNo() {
        return BookInwardNo;
    }

    public void setBookInwardNo(String bookInwardNo) {
        BookInwardNo = bookInwardNo;
    }

    public String getBookNo() {
        return BookNo;
    }

    public void setBookNo(String bookNo) {
        BookNo = bookNo;
    }

    public String getBookImage() {
        return BookImage;
    }

    public void setBookImage(String bookImage) {
        BookImage = bookImage;
    }

    public int getLibraryId() {
        return LibraryId;
    }

    public void setLibraryId(int libraryId) {
        LibraryId = libraryId;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getBookType() {
        return BookType;
    }

    public void setBookType(String bookType) {
        BookType = bookType;
    }

    public String getPublisherName() {
        return PublisherName;
    }

    public void setPublisherName(String publisherName) {
        PublisherName = publisherName;
    }

    public String getBookTitle() {
        return BookTitle;
    }

    public void setBookTitle(String bookTitle) {
        BookTitle = bookTitle;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
