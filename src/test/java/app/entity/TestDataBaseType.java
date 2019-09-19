package app.entity;

import java.io.*;
import javax.persistence.*;
import java.util.*;
import javax.xml.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonFilter;
import cronapi.CronapiByteHeaderSignature;
import cronapi.rest.security.CronappSecurity;

@Entity
@Table(name = "\"TESTDATABASETYPE\"")
@XmlRootElement
@CronappSecurity
@JsonFilter("app.entity.TestDataBaseType")
public class TestDataBaseType implements Serializable {

    /**
     * UID da classe, necessário na serialização
     * @generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * @generated
     */
    @Id
    @Column(name = "id", nullable = false, insertable=true, updatable=true)
    private java.lang.String id = UUID.randomUUID().toString().toUpperCase();

    /**
     * @generated
     */
    @Column(name = "typeString", nullable = true, unique = false, insertable=true, updatable=true)

    private java.lang.String typeString;

    /**
     * @generated
     */
    @Column(name = "typeLogic", nullable = true, unique = false, insertable=true, updatable=true)

    private java.lang.Boolean typeLogic;

    /**
     * @generated
     */
    @Column(name = "typeCaract", nullable = true, unique = false, insertable=true, updatable=true)

    private java.lang.Character typeCaract;

    /**
     * @generated
     */
    @Column(name = "typeNumerico", nullable = true, unique = false, insertable=true, updatable=true)

    private java.lang.Double typeNumerico;

    /**
     * @generated
     */
    @Column(name = "typeIntero", nullable = true, unique = false, insertable=true, updatable=true)

    private java.lang.Integer typeIntero;

    /**
     * @generated
     */
    @Column(name = "typeLong", nullable = true, unique = false, insertable=true, updatable=true)

    private java.lang.Long typeLong;

    /**
     * @generated
     */
    @Column(name = "typeBigDecinal", nullable = true, unique = false, insertable=true, updatable=true)

    private java.math.BigDecimal typeBigDecinal;

    /**
     * @generated
     */
    @Column(name = "typeBigInteger", nullable = true, unique = false, insertable=true, updatable=true)

    private java.math.BigInteger typeBigInteger;

    /**
     * @generated
     */
    @Column(name = "typeByte", nullable = true, unique = false, insertable=true, updatable=true)

    private java.lang.Byte typeByte;

    /**
     * @generated
     */
    @Temporal(TemporalType.TIME)
    @Column(name = "typeTime", nullable = true, unique = false, insertable=true, updatable=true)

    private java.util.Date typeTime;

    /**
     * @generated
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "typeDateTime", nullable = true, unique = false, insertable=true, updatable=true, columnDefinition = "TIMESTAMP")

    private java.util.Date typeDateTime;

    /**
     * @generated
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "typeDate", nullable = true, unique = false, insertable=true, updatable=true)

    private java.util.Date typeDate;

    /**
     * @generated
     */
    @Column(name = "typeByteArray", nullable = true, unique = false, insertable=true, updatable=true)

    private byte[] typeByteArray;

    /**
     * @generated
     */
    @Column(name = "typeByteArrayBanco", nullable = true, unique = false, insertable=true, updatable=true)
    @CronapiByteHeaderSignature

    private byte[] typeByteArrayBanco;

    /**
     * @generated
     */
    @Column(name = "typeShort", nullable = true, unique = false, insertable=true, updatable=true)

    private java.lang.Short typeShort;

    /**
     * Construtor
     * @generated
     */
    public TestDataBaseType(){
    }


    /**
     * Obtém id
     * return id
     * @generated
     */

    public java.lang.String getId(){
        return this.id;
    }

    /**
     * Define id
     * @param id id
     * @generated
     */
    public TestDataBaseType setId(java.lang.String id){
        this.id = id;
        return this;
    }

    /**
     * Obtém typeString
     * return typeString
     * @generated
     */

    public java.lang.String getTypeString(){
        return this.typeString;
    }

    /**
     * Define typeString
     * @param typeString typeString
     * @generated
     */
    public TestDataBaseType setTypeString(java.lang.String typeString){
        this.typeString = typeString;
        return this;
    }

    /**
     * Obtém typeLogic
     * return typeLogic
     * @generated
     */

    public java.lang.Boolean getTypeLogic(){
        return this.typeLogic;
    }

    /**
     * Define typeLogic
     * @param typeLogic typeLogic
     * @generated
     */
    public TestDataBaseType setTypeLogic(java.lang.Boolean typeLogic){
        this.typeLogic = typeLogic;
        return this;
    }

    /**
     * Obtém typeCaract
     * return typeCaract
     * @generated
     */

    public java.lang.Character getTypeCaract(){
        return this.typeCaract;
    }

    /**
     * Define typeCaract
     * @param typeCaract typeCaract
     * @generated
     */
    public TestDataBaseType setTypeCaract(java.lang.Character typeCaract){
        this.typeCaract = typeCaract;
        return this;
    }

    /**
     * Obtém typeNumerico
     * return typeNumerico
     * @generated
     */

    public java.lang.Double getTypeNumerico(){
        return this.typeNumerico;
    }

    /**
     * Define typeNumerico
     * @param typeNumerico typeNumerico
     * @generated
     */
    public TestDataBaseType setTypeNumerico(java.lang.Double typeNumerico){
        this.typeNumerico = typeNumerico;
        return this;
    }

    /**
     * Obtém typeIntero
     * return typeIntero
     * @generated
     */

    public java.lang.Integer getTypeIntero(){
        return this.typeIntero;
    }

    /**
     * Define typeIntero
     * @param typeIntero typeIntero
     * @generated
     */
    public TestDataBaseType setTypeIntero(java.lang.Integer typeIntero){
        this.typeIntero = typeIntero;
        return this;
    }

    /**
     * Obtém typeLong
     * return typeLong
     * @generated
     */

    public java.lang.Long getTypeLong(){
        return this.typeLong;
    }

    /**
     * Define typeLong
     * @param typeLong typeLong
     * @generated
     */
    public TestDataBaseType setTypeLong(java.lang.Long typeLong){
        this.typeLong = typeLong;
        return this;
    }

    /**
     * Obtém typeBigDecinal
     * return typeBigDecinal
     * @generated
     */

    public java.math.BigDecimal getTypeBigDecinal(){
        return this.typeBigDecinal;
    }

    /**
     * Define typeBigDecinal
     * @param typeBigDecinal typeBigDecinal
     * @generated
     */
    public TestDataBaseType setTypeBigDecinal(java.math.BigDecimal typeBigDecinal){
        this.typeBigDecinal = typeBigDecinal;
        return this;
    }

    /**
     * Obtém typeBigInteger
     * return typeBigInteger
     * @generated
     */

    public java.math.BigInteger getTypeBigInteger(){
        return this.typeBigInteger;
    }

    /**
     * Define typeBigInteger
     * @param typeBigInteger typeBigInteger
     * @generated
     */
    public TestDataBaseType setTypeBigInteger(java.math.BigInteger typeBigInteger){
        this.typeBigInteger = typeBigInteger;
        return this;
    }

    /**
     * Obtém typeByte
     * return typeByte
     * @generated
     */

    public java.lang.Byte getTypeByte(){
        return this.typeByte;
    }

    /**
     * Define typeByte
     * @param typeByte typeByte
     * @generated
     */
    public TestDataBaseType setTypeByte(java.lang.Byte typeByte){
        this.typeByte = typeByte;
        return this;
    }

    /**
     * Obtém typeTime
     * return typeTime
     * @generated
     */

    public java.util.Date getTypeTime(){
        return this.typeTime;
    }

    /**
     * Define typeTime
     * @param typeTime typeTime
     * @generated
     */
    public TestDataBaseType setTypeTime(java.util.Date typeTime){
        this.typeTime = typeTime;
        return this;
    }

    /**
     * Obtém typeDateTime
     * return typeDateTime
     * @generated
     */

    public java.util.Date getTypeDateTime(){
        return this.typeDateTime;
    }

    /**
     * Define typeDateTime
     * @param typeDateTime typeDateTime
     * @generated
     */
    public TestDataBaseType setTypeDateTime(java.util.Date typeDateTime){
        this.typeDateTime = typeDateTime;
        return this;
    }

    /**
     * Obtém typeDate
     * return typeDate
     * @generated
     */

    public java.util.Date getTypeDate(){
        return this.typeDate;
    }

    /**
     * Define typeDate
     * @param typeDate typeDate
     * @generated
     */
    public TestDataBaseType setTypeDate(java.util.Date typeDate){
        this.typeDate = typeDate;
        return this;
    }

    /**
     * Obtém typeByteArray
     * return typeByteArray
     * @generated
     */

    public byte[] getTypeByteArray(){
        return this.typeByteArray;
    }

    /**
     * Define typeByteArray
     * @param typeByteArray typeByteArray
     * @generated
     */
    public TestDataBaseType setTypeByteArray(byte[] typeByteArray){
        this.typeByteArray = typeByteArray;
        return this;
    }

    /**
     * Obtém typeByteArrayBanco
     * return typeByteArrayBanco
     * @generated
     */

    public byte[] getTypeByteArrayBanco(){
        return this.typeByteArrayBanco;
    }

    /**
     * Define typeByteArrayBanco
     * @param typeByteArrayBanco typeByteArrayBanco
     * @generated
     */
    public TestDataBaseType setTypeByteArrayBanco(byte[] typeByteArrayBanco){
        this.typeByteArrayBanco = typeByteArrayBanco;
        return this;
    }

    /**
     * Obtém typeShort
     * return typeShort
     * @generated
     */

    public java.lang.Short getTypeShort(){
        return this.typeShort;
    }

    /**
     * Define typeShort
     * @param typeShort typeShort
     * @generated
     */
    public TestDataBaseType setTypeShort(java.lang.Short typeShort){
        this.typeShort = typeShort;
        return this;
    }

    /**
     * @generated
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TestDataBaseType object = (TestDataBaseType)obj;
        if (!Objects.equals(id, object.id)) return false;
        return true;
    }

    /**
     * @generated
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
}
