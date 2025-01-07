package br.com.weblinker.users.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE \"companies\" SET \"deleted_at\" = NOW() WHERE \"id\" = ?")
@Where(clause = "\"deleted_at\" IS NULL")
@Table(name = "companies")
@Getter
@Setter
public class Company extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "company")
    private List<User> users;

    public Company() {
    }

    public Company(String name) {
        this.name = name;
    }
}
