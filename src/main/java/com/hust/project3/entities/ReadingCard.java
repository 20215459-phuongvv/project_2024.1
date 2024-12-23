package com.hust.project3.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reading_cards")
public class ReadingCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "type")
    private Integer type;

    @Column(name = "violation_count")
    private Integer violationCount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "status")
    private Integer status;

    @OneToMany(mappedBy = "readingCard", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BorrowBook> borrowBookList;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnoreProperties("reading_card")
    private User user;
}
