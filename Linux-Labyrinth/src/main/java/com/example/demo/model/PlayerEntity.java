package com.example.demo.model;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;
import java.nio.file.Paths;


@Entity
@Table(name = "player_answers") 
public class PlayerEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id is auto generated
    private Long id;
	
    @NotNull
    @Column(unique = true, nullable = false)
    private String username;
	
    @Column(nullable = false)
    @NotNull
    private String password;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime date; 
    
    @Column(nullable = false)
    @NotNull
    private String currentpath;

    @Column(nullable = false)
    @NotNull
    private String answer_1;
  
    @Column(nullable = false)
    @NotNull
    private String answer_2; 

    @Column(nullable = false)
    @NotNull 
    private String answer_3;

    @Column(nullable = false)
    @NotNull
    private String answer_4;

    @Column(nullable = false)
    @NotNull 
    private String answer_5;

    @Column(nullable = false)
    @NotNull 
    private String answer_6;

    @Column(nullable = false)
    @NotNull 
    private String answer_7; 



    private PlayerEntity(){
    }

    public PlayerEntity(String username, String password){
        this.username = username;
        this.password = password;
        this.date = LocalDateTime.now(); 
        this.currentpath = Paths.get("").toAbsolutePath().getParent().resolve("labyrinth").normalize().toString();
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
    
    public String getName(){
        return username;
    }

    public String getAnswer1(){
        return answer_1;
    }

    public String getAnswer2(){
        return answer_2;
    }

    public String getAnswer3(){
        return answer_3;
    }

    public String getAnswer4(){
        return answer_4;
    }

    public String getAnswer5(){
        return answer_5;
    }

    public String getAnswer6(){
        return answer_6;
    }

    public String getAnswer7(){
        return answer_7;
    }

    public String getPath(){
        return currentpath;
    }
}
