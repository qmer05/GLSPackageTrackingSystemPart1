package app.entities;

import app.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "packages")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "receiver_name")
    private String receiverName;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Builder
    public Package(String trackingNumber, String SenderName, String receiverName, DeliveryStatus deliveryStatus) {
        this.trackingNumber = trackingNumber;
        this.senderName = SenderName;
        this.receiverName = receiverName;
        this.deliveryStatus = deliveryStatus;
    }

    @PrePersist
    protected void prePersist() {
        this.creationDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Package pkg = (Package) o;
        return Objects.equals(id, pkg.id) &&
                Objects.equals(trackingNumber, pkg.trackingNumber) &&
                Objects.equals(senderName, pkg.senderName) &&
                Objects.equals(receiverName, pkg.receiverName) &&
                deliveryStatus == pkg.deliveryStatus &&
                Objects.equals(creationDate.withNano(0), pkg.creationDate.withNano(0)) &&
                Objects.equals(lastUpdated, pkg.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trackingNumber, senderName, receiverName, deliveryStatus,
                creationDate.withNano(0), lastUpdated);
    }

}
