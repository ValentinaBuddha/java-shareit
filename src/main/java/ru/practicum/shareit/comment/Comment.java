//package ru.practicum.shareit.comment;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "comments")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class Comment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    @Column
//    private String text;
//
//    @Column(name = "item_id")
//    private long itemId;
//
//    @Column(name = "author_id")
//    private long authorId;
//}
