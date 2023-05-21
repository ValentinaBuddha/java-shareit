package ru.practicum.shareit.item;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Boolean available;
    
    @Column(name = "owner_id")
    private long ownerId;

    @Column(name = "request_id")
    private long requestId;
}