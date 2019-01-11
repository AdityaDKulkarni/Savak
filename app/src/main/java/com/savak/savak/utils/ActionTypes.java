package com.savak.savak.utils;

/**
 * @author Aditya Kulkarni
 */

public interface ActionTypes {
    int TYPE_SMART_LIBRARY = 0;
    int TYPE_SMART_SEARCH = 1;
    int TYPE_REGIONS = 2;
    int TYPE_REGION_LIBRARIES = 3;
    int TYPE_MANAGEMENT_BODY = 4;
    int TYPE_SOCIAL_PROJECTS = 5;
    int TYPE_COMPLEMENTS = 6;
    int TYPE_CONTROL_PANEL = 7;
    int TYPE_NEW_BOOKS = 8;
    int TYPE_GRANTHSAMPADA = 9;
    int TYPE_READERS_CHOICE = 10;
    int TOP_10_READERS = 11;
    int MEMBERSHIP_PLANS = 12;
    int TYPE_WISE_COUNT = 13;
    int MEMBER_STATISTICS = 14;
    int TYPE_WISE_PURCHASE = 15;

    int getType();
}
