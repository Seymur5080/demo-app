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
@Table(name = "currency_reports")
public class CurrencyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JacksonXmlProperty(isAttribute = true, localName = "Date")
    @Column(nullable = false)
    String date;

    @JacksonXmlProperty(isAttribute = true, localName = "Name")
    @Column(nullable = false)
    String name;

    @JacksonXmlProperty(isAttribute = true, localName = "Description")
    @Column(nullable = false, length = 1000)
    String description;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "ValType")
    @OneToMany(mappedBy = "currencyReport", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CurrencyType> currencyTypes = new ArrayList<>();

    public void addCurrencyType(CurrencyType currencyType) {
        currencyTypes.add(currencyType);
        currencyType.setCurrencyReport(this);
    }
}