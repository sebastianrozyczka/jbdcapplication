package pl.sebroz.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {
    private int id;
    private String type;
    private String description;
    private double amount;
    private String date;
}
