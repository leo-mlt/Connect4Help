package org.template.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

@Entity
public class Category implements Serializable {
    private String name;
    private byte[] photo;
    private Collection<Service> relatedServices;

    @Id
    @Column(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "Photo")
    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (name != null ? !name.equals(category.name) : category.name != null) return false;
        if (!Arrays.equals(photo, category.photo)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }

    @OneToMany(mappedBy = "category")
    public Collection<Service> getRelatedServices() {
        return relatedServices;
    }

    public void setRelatedServices(Collection<Service> relatedServices) {
        this.relatedServices = relatedServices;
    }
}
