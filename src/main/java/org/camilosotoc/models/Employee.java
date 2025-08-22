package org.camilosotoc.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@AllArgsConstructor
@Getter
public class Employee {

    private String name;
    private String lastname;
    private String rut;
    private String position;
    private Double baseSalary;
    private Double bonus;
    private Double discounts;
    private String inDate;
    @Setter
    private String error;

    public int getSeniority() {
        if (this.inDate == null)
            return 0;
        try {
            LocalDate date = LocalDate.parse(this.inDate);
            int years = Period.between(date, LocalDate.now()).getYears();
            return Math.max(0, years);
        } catch (Exception e) {
            return 0;
        }
    }

    public double getSeniorityBonus() {
        int seniority = this.getSeniority();
        if (seniority > 5) {
            return (this.baseSalary != null ? this.baseSalary : 0) * 0.10;
        } else if (seniority >= 3) {
            return (this.baseSalary != null ? this.baseSalary : 0) * 0.05;
        }
        return 0;
    }

    public double getFinalSalary() {
        return (this.baseSalary != null ? this.baseSalary : 0)
                + (this.bonus != null ? this.bonus : 0)
                + getSeniorityBonus()
                - (this.discounts != null ? this.discounts : 0);
    }
}
