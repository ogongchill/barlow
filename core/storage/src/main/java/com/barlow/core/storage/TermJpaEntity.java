package com.barlow.core.storage;

import com.barlow.core.domain.registration.Term;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;

import java.time.LocalDateTime;

@Entity
@Table(name = "term")
public class TermJpaEntity extends BaseTimeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "term_no")
    private Long no;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Term.Type type;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "required", nullable = false)
    private boolean required;

    @Column(name = "link_url", nullable = false)
    private String linkUrl;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "effective_at", nullable = false)
    private LocalDateTime effectiveAt;

    public Term toTerm() {
        return new Term(
                no,
                title,
                version,
                linkUrl,
                type,
                required,
                effectiveAt
        );
    }
}
