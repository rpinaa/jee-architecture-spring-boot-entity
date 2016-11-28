package org.example.seed.domain;

import lombok.Data;
import org.example.seed.group.MomentumGroup;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ricardo Pina Arellano on 24/11/2016.
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Momentum implements Serializable {

    @Column(name = "REGISTER_DATE", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @NotNull(groups = {MomentumGroup.class})
    private Date registerDate;

    @Column(name = "CHANGE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @NotNull(groups = {MomentumGroup.class})
    private Date changeDate;
}
