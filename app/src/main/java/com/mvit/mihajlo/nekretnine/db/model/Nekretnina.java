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
    public static final String TABLE_NAME_USERS = "actor";
    public static final String FIELD_NAME_ID     = "id";
    public static final String TABLE_MOVIE_NAME = "name";
    public static final String TABLE_MOVIE_BIOGRAPHY = "biography";
    public static final String TABLE_MOVIE_SCORE = "score";
    public static final String TABLE_MOVIE_BIRTH = "birth";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = TABLE_MOVIE_NAME)
    private String mName;

    @DatabaseField(columnName = TABLE_MOVIE_BIOGRAPHY)
    private String mBiography;

    @DatabaseField(columnName = TABLE_MOVIE_SCORE)
    private Float mScore;

    @DatabaseField(columnName = TABLE_MOVIE_BIRTH)
    private String mBirth;

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

    public Float getmScore() {
        return mScore;
    }

    public void setmScore(Float mScore) {
        this.mScore = mScore;
    }

    public String getmBirth() {
        return mBirth;
    }

    public void setmBirth(String mBirth) {
        this.mBirth = mBirth;
    }

    @Override
    public String toString() {
        return mName;
    }



}
