package com.hisham.scribesByHIsham.model.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

@Getter
@Setter
@MappedSuperclass
@JsonIgnoreProperties(
        value = {"createdBy", "lastModifiedBy"},
        allowGetters = true
)
public abstract class UserDateAudit extends DateAudit{
    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;

    @LastModifiedBy
    private Long lastModifiedBy;
}
