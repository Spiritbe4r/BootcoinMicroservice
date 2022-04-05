package com.bootcamp.bootcoinservice.models.entities;

import com.bootcamp.bootcoinservice.models.dto.YankiCommand;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "bootcoin")
public class BootCoin implements Serializable {

    private static final long serialVersionUID= 4765376250981322925L;

    @Id
    private String id;

    private String name;

    private String clientIdType;

    private String clientIdNumber;

    private String phoneNumber;

    private String email;

    private YankiCommand yanki;

    private String typeOfAccount;

    private Double amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss ")
    private LocalDateTime createdAt = LocalDateTime.now();

}
