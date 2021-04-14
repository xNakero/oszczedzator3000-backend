package pl.pz.oszczedzator3000.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pz.oszczedzator3000.model.enums.Category;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "expense")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @Column(name = "expense_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long expenseId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double value;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
