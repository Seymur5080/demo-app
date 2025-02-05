package az.bankrespublika.demoapp.dao.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "currency_types")
public class CurrencyType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JacksonXmlProperty(isAttribute = true, localName = "Type")
    @Column(nullable = false)
    String type;

    @ManyToOne
    @JoinColumn(name = "currency_report_id", nullable = false)
    CurrencyReport currencyReport;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Valute")
    @OneToMany(mappedBy = "currencyType", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Currency> currencies = new ArrayList<>();

    /*
    public void addCurrency(Currency currency) {
        currencies.add(currency);
        currency.setCurrencyType(this);
    }
     */
}