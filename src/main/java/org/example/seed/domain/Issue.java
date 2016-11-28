package org.example.seed.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.seed.catalog.IssuePriority;
import org.example.seed.catalog.IssueStatus;
import org.example.seed.catalog.IssueType;
import org.example.seed.group.issue.IssueCreateGroup;
import org.example.seed.group.issue.IssueUpdateGroup;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity(name = "TIC001_ISSUE")
public class Issue extends Momentum {

    private static final long serialVersionUID = -3820079360849433869L;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "ID_ISSUE", length = 32, nullable = false, updatable = false)
    @NotEmpty(groups = {IssueUpdateGroup.class})
    private String id;

    @Column(name = "TITLE", length = 80)
    @NotEmpty(groups = {IssueCreateGroup.class, IssueUpdateGroup.class})
    private String title;

    @Column(name = "DESCRIPTION", length = 150)
    @NotEmpty(groups = {IssueCreateGroup.class, IssueUpdateGroup.class})
    private String description;

    @Column(name = "TYPE", length = 11)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = {IssueCreateGroup.class, IssueUpdateGroup.class})
    private IssueType type;

    @Column(name = "PRIORITY", length = 6)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = {IssueUpdateGroup.class})
    private IssuePriority priority;

    @Column(name = "STATUS", length = 11)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = {IssueUpdateGroup.class})
    private IssueStatus status;
}
