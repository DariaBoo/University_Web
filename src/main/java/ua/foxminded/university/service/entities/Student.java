package ua.foxminded.university.service.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString(callSuper = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, exclude = { "group" })
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(name = "unique_name_surname_idCard", columnNames = { "first_name", "last_name", "id_card" }),
        @UniqueConstraint(name = "unique_group", columnNames = { "first_name", "last_name", "id_card", "group_id" }) })
@DynamicUpdate
public class Student extends User {

    @Generated(GenerationTime.INSERT)
    @ColumnDefault(value = "'1234'")
    @Column(name = "password", length = 10)
    private String password;

    @NotBlank(message = "Id card may not be blank")
    @Size(min = 2, max = 5, message = "Id card must be between 2 and 5 characters long")
    @Column(name = "id_card", unique = true)
    private String idCard;

    @ManyToOne(targetEntity = Group.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
}
