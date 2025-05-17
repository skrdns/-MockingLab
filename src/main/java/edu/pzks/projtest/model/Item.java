package edu.pzks.projtest.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private UUID id;
    private String name;
    private String code;
    private String description;
    private LocalDateTime createDate;
    private Optional<LocalDateTime> updateDate;
}
