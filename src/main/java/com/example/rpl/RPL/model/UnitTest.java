package com.example.rpl.RPL.model;

import java.time.ZonedDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 *     id           BIGINT NOT NULL AUTO_INCREMENT,
 *     test_id      BIGINT,
 *     files_id     BIGINT,
 *     date_created DATETIME,
 *     last_updated DATETIME,
 *
 *     PRIMARY KEY (id),
 *     FOREIGN KEY (test_id) REFERENCES tests(id)
 */
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "unit_tests")
public class UnitTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;


    @JoinColumn(name = "test_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Test test;

    @JoinColumn(name = "test_file_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RPLFile testFile;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    /**
     * @deprecated Only used by hibernate
     */
    @Deprecated
    private UnitTest() {
    }

}
