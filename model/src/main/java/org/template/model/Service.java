package org.template.model;

import org.eclipse.persistence.annotations.JoinFetch;
import org.eclipse.persistence.annotations.JoinFetchType;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Entity
@SqlResultSetMapping(name = "ScheduleResult", entities = @EntityResult(entityClass = Service.class))
@NamedNativeQuery(name = "Schedules",
        query = "SELECT s.* FROM service s where (6371 * acos(cos(radians(?1)) * cos(radians(s.Latitude)) * cos(radians(s.Longitude) - radians(?2)) + sin(radians(?1)) * sin(radians(s.Latitude))))< ?3 and s.IdPerformerUser is null and s.ExpirationDate >= current_timestamp",
        resultSetMapping = "ScheduleResult")
public class Service implements Serializable {
    private int idService;
    private String address;
    private String details;
    private RequestUser requestUser;
    private Category category;
    private PerformUser performerUser;
    private Boolean performed;
    private Boolean assistance;
    private Timestamp startSlot;
    private Timestamp endSlot;
    private Timestamp expirationDate;
    private Timestamp insertionDate;
    private Timestamp acceptanceDate;
    private Double latitude;
    private Double longitude;
    private byte[] servicePicture;



    public Service() {

    }

    public Service(int idService, String address, String details, RequestUser requestUser, Category category, PerformUser performerUser, Boolean performed, Boolean assistance, Timestamp startSlot, Timestamp endSlot, Timestamp expirationDate, Timestamp insertionDate, Timestamp acceptanceDate, Double latitude, Double longitude, byte[] servicePicture) {
        this.idService = idService;
        this.address = address;
        this.details = details;
        this.requestUser = requestUser;
        this.category = category;
        this.performerUser = performerUser;
        this.performed = performed;
        this.assistance = assistance;
        this.startSlot = startSlot;
        this.endSlot = endSlot;
        this.expirationDate = expirationDate;
        this.insertionDate = insertionDate;
        this.acceptanceDate = acceptanceDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.servicePicture = servicePicture;
    }

    @Id
    @Column(name = "idService")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    @Basic
    @Column(name = "Address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "Performed")
    public Boolean getPerformed() {
        return performed;
    }

    public void setPerformed(Boolean performed) {
        this.performed = performed;
    }

    @Basic
    @Column(name = "Assistance")
    public Boolean getAssistance() {
        return assistance;
    }

    public void setAssistance(Boolean assistance) {
        this.assistance = assistance;
    }

    @Basic
    @Column(name = "Details")
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Basic
    @Column(name = "StartSlot")
    public Timestamp getStartSlot() {
        return startSlot;
    }

    public void setStartSlot(Timestamp startSlot) {
        this.startSlot = startSlot;
    }

    @Basic
    @Column(name = "EndSlot")
    public Timestamp getEndSlot() {
        return endSlot;
    }

    public void setEndSlot(Timestamp endSlot) {
        this.endSlot = endSlot;
    }

    @Basic
    @Column(name = "ExpirationDate")
    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Basic
    @Column(name = "InsertionDate")
    public Timestamp getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(Timestamp insertionDate) {
        insertionDate = new Timestamp((new Date()).getTime());
        this.insertionDate = insertionDate;
    }

    @Basic
    @Column(name = "AcceptanceDate")
    public Timestamp getAcceptanceDate() {
        return acceptanceDate;
    }

    public void setAcceptanceDate(Timestamp acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    @Basic
    @Column(name = "Latitude")
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "Longitude")
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "Picture")
    public byte[] getServicePicture() {
        return servicePicture;
    }

    public void setServicePicture(byte[] servicePicture) {
        this.servicePicture = servicePicture;
    }




    @ManyToOne
    @JoinColumn(name = "IdPerformerUser", referencedColumnName = "idUser")
    public PerformUser getPerformerUser() {
        return performerUser;
    }

    public void setPerformerUser(PerformUser performerUser) {
        this.performerUser = performerUser;
    }

    @ManyToOne
    @JoinColumn(name = "IdRequestUser", referencedColumnName = "idUser", nullable = false)
    public RequestUser getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(RequestUser requestUser) {
        this.requestUser = requestUser;
    }

    @ManyToOne
    @JoinColumn(name = "Category_Name", referencedColumnName = "Name", nullable = false)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Service)) return false;
        Service service = (Service) o;
        return idService == service.idService &&
                Objects.equals(address, service.address) &&
                Objects.equals(details, service.details) &&
                Objects.equals(requestUser, service.requestUser) &&
                Objects.equals(category, service.category) &&
                Objects.equals(performerUser, service.performerUser) &&
                Objects.equals(performed, service.performed) &&
                Objects.equals(assistance, service.assistance) &&
                Objects.equals(startSlot, service.startSlot) &&
                Objects.equals(endSlot, service.endSlot) &&
                Objects.equals(expirationDate, service.expirationDate) &&
                Objects.equals(insertionDate, service.insertionDate) &&
                Objects.equals(acceptanceDate, service.acceptanceDate) &&
                Objects.equals(latitude, service.latitude) &&
                Objects.equals(longitude, service.longitude) &&
                Arrays.equals(servicePicture, service.servicePicture);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idService, address, details, requestUser, category, performerUser, performed, assistance, startSlot, endSlot, expirationDate, insertionDate, acceptanceDate, latitude, longitude);
        result = 31 * result + Arrays.hashCode(servicePicture);
        return result;
    }
}
