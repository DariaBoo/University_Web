package ua.foxminded.university.service.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "group" })
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_idCard", columnNames = { "user_id", "id_card" }),
        @UniqueConstraint(name = "unique_group", columnNames = { "user_id", "id_card", "group_id" }) })
@DynamicUpdate
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    
    @NotBlank(message = "Id card may not be blank")
    @Size(min = 2, max = 5, message = "Id card must be between 2 and 5 characters long")
    @Column(name = "id_card", unique = true)
    private String idCard;

    @ManyToOne(targetEntity = Group.class)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
}
