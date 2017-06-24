package com.mvit.mihajlo.nekretnine.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by androiddevelopment on 24.6.17..
 */

@DatabaseTable(tableName = Nekretnina.TABLE_NAME_USERS)
public class Nekretnina {
    public static final String FIELD_NAME_ID     = "id";
    public static final String TABLE_NAME_USERS = "actor";
    public static final String TABLE_REALESTATE_NAME = "name";
    public static final String TABLE_REALESTATE_BIOGRAPHY = "biography";
    public static final String TABLE_REALESTATE_IMAGE = "image";
    public static final String TABLE_REALESTATE_ADDRESS = "address";
    public static final String TABLE_REALESTATE_PHONE = "phone";
    public static final String TTABLE_REALESTATE_SIZE = "size";
    public static final String TABLE_REALESTATE_ROOMS = "rooms";
    public static final String TABLE_REALESTATE_PRICE = "price";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = TABLE_REALESTATE_NAME)
    private String mName;

    @DatabaseField(columnName = TABLE_REALESTATE_BIOGRAPHY)
    private String mBiography;

    @DatabaseField(columnName = TABLE_REALESTATE_IMAGE)
    private String mImage;

    @DatabaseField(columnName = TABLE_REALESTATE_ADDRESS)
    private String mAddress;

    @DatabaseField(columnName = TABLE_REALESTATE_PHONE)
    private String mPhone;

    @DatabaseField(columnName = TTABLE_REALESTATE_SIZE)
    private String mSize;

    @DatabaseField(columnName = TABLE_REALESTATE_ROOMS)
    private String mRooms;

    @DatabaseField(columnName = TABLE_REALESTATE_PRICE)
    private String mPrice;

    public Nekretnina(){}

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmBiography() {
        return mBiography;
    }

    public void setmBiography(String mBiography) {
        this.mBiography = mBiography;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmSize() {
        return mSize;
    }

    public void setmSize(String mSize) {
        this.mSize = mSize;
    }

    public String getmRooms() {
        return mRooms;
    }

    public void setmRooms(String mRooms) {
        this.mRooms = mRooms;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    @Override
    public String toString() {
        return mName;
    }



}
