package az.bankrespublika.demoapp.dao.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JacksonXmlProperty(isAttribute = true, localName = "Code")
    @Column(nullable = false)
    String code;

    @JacksonXmlProperty(localName = "Name")
    @Column(nullable = false)
    String name;

    @JacksonXmlProperty(localName = "Nominal")
    @Column(nullable = false)
    String nominal;

    @JacksonXmlProperty(localName = "Value")
    @Column(nullable = false)
    Double value;

    @ManyToOne
    @JoinColumn(name = "currency_type_id", nullable = false)
    CurrencyType currencyType;
}