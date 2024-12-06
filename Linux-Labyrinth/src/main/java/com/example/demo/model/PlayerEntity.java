package com.example.demo.model;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;


@Entity
@Table(name = "player_answers") 
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id is auto generated
    private Long id;

    private String username;

    private String password;

    private LocalDateTime date; 

    private String currentpath;

    private String answer_1;
    private String answer_2; 
    private String answer_3;
    private String answer_4; 
    private String answer_5; 
    private String answer_6; 
    private String answer_7; 



    private PlayerEntity(){
    }

    public PlayerEntity(String username, String password){
        this.username = username;
        this.password = password;
        this.date = LocalDateTime.now(); 
        this.currentpath = "";
        this.answer_1 = "";
        this.answer_2 = "";
        this.answer_3 = "";
        this.answer_4 = "";
        this.answer_5 = "";
        this.answer_6 = "";
        this.answer_7 = "";
    }


    public String getPassword(){
        return password;
    }

    public String getAnswer1(){
        return answer_1;
    }


}