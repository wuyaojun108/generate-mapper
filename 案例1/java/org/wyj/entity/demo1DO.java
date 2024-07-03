package org.wyj.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.sql.Blob;

/**
 * @author wuyaojun
 * @date 2024/7/3
 */
public class demo1DO {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 注释1
     */
    private Byte aTinyint;
    /**
     * 注释2
     */
    private Short aSmallint;
    private Integer aMediumint;
    private Integer aInt;
    private Integer aInteger;
    private Long aBigint;
    private Boolean aBoolean;
    private Boolean aBit;
    private Float aFloat;
    private Double aDouble;
    private BigDecimal aDecimal;
    private String aChar;
    private String aVarchar;
    private String aTinytext;
    private String aText;
    private String aMediumtext;
    private String aLongText;
    private Date aDate;
    private Date aTime;
    private Date aDatetime;
    private Timestamp aTimestamp;
    private Date aYear;
    private Blob aBlob;

    public Integer getId() {
         return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getATinyint() {
         return aTinyint;
    }

    public void setATinyint(Byte aTinyint) {
        this.aTinyint = aTinyint;
    }

    public Short getASmallint() {
         return aSmallint;
    }

    public void setASmallint(Short aSmallint) {
        this.aSmallint = aSmallint;
    }

    public Integer getAMediumint() {
         return aMediumint;
    }

    public void setAMediumint(Integer aMediumint) {
        this.aMediumint = aMediumint;
    }

    public Integer getAInt() {
         return aInt;
    }

    public void setAInt(Integer aInt) {
        this.aInt = aInt;
    }

    public Integer getAInteger() {
         return aInteger;
    }

    public void setAInteger(Integer aInteger) {
        this.aInteger = aInteger;
    }

    public Long getABigint() {
         return aBigint;
    }

    public void setABigint(Long aBigint) {
        this.aBigint = aBigint;
    }

    public Boolean getABoolean() {
         return aBoolean;
    }

    public void setABoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public Boolean getABit() {
         return aBit;
    }

    public void setABit(Boolean aBit) {
        this.aBit = aBit;
    }

    public Float getAFloat() {
         return aFloat;
    }

    public void setAFloat(Float aFloat) {
        this.aFloat = aFloat;
    }

    public Double getADouble() {
         return aDouble;
    }

    public void setADouble(Double aDouble) {
        this.aDouble = aDouble;
    }

    public BigDecimal getADecimal() {
         return aDecimal;
    }

    public void setADecimal(BigDecimal aDecimal) {
        this.aDecimal = aDecimal;
    }

    public String getAChar() {
         return aChar;
    }

    public void setAChar(String aChar) {
        this.aChar = aChar;
    }

    public String getAVarchar() {
         return aVarchar;
    }

    public void setAVarchar(String aVarchar) {
        this.aVarchar = aVarchar;
    }

    public String getATinytext() {
         return aTinytext;
    }

    public void setATinytext(String aTinytext) {
        this.aTinytext = aTinytext;
    }

    public String getAText() {
         return aText;
    }

    public void setAText(String aText) {
        this.aText = aText;
    }

    public String getAMediumtext() {
         return aMediumtext;
    }

    public void setAMediumtext(String aMediumtext) {
        this.aMediumtext = aMediumtext;
    }

    public String getALongText() {
         return aLongText;
    }

    public void setALongText(String aLongText) {
        this.aLongText = aLongText;
    }

    public Date getADate() {
         return aDate;
    }

    public void setADate(Date aDate) {
        this.aDate = aDate;
    }

    public Date getATime() {
         return aTime;
    }

    public void setATime(Date aTime) {
        this.aTime = aTime;
    }

    public Date getADatetime() {
         return aDatetime;
    }

    public void setADatetime(Date aDatetime) {
        this.aDatetime = aDatetime;
    }

    public Timestamp getATimestamp() {
         return aTimestamp;
    }

    public void setATimestamp(Timestamp aTimestamp) {
        this.aTimestamp = aTimestamp;
    }

    public Date getAYear() {
         return aYear;
    }

    public void setAYear(Date aYear) {
        this.aYear = aYear;
    }

    public Blob getABlob() {
         return aBlob;
    }

    public void setABlob(Blob aBlob) {
        this.aBlob = aBlob;
    }
}