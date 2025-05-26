package com.barlow.core.storage;

import com.barlow.core.domain.version.AvailableClientVersion;
import com.barlow.core.domain.version.SemanticVersion;
import com.barlow.core.enumerate.DeviceOs;
import jakarta.persistence.*;

@Entity
@Table(name = "client_version")
public class ClientVersionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10)", name = "device_os", nullable = false)
    private DeviceOs deviceOs;

    @Column(name = "minimum_supported", length = 50)
    private String minimumSupported;

    @Column(name = "latest", length = 50)
    private String latest;

    AvailableClientVersion toAvailableClientVersion() {
        return new AvailableClientVersion(
            SemanticVersion.of(minimumSupported),
            SemanticVersion.of(latest)
        );
    }
}
