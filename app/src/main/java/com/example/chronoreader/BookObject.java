package com.example.chronoreader;

import java.io.Serializable;
import java.util.UUID;

public class BookObject implements Serializable {
    // Instance Variables
    UUID uuid;
    String title;
    String author;
    String genre;
    int pageCount;
    int wordsPerPage;

    //Constructor
    public BookObject(){}
    public BookObject(UUID uuid, String title, String author, String genre, int pageCount, int wordsPerPage){
        this.uuid = uuid;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.pageCount = pageCount;
        this.wordsPerPage = wordsPerPage;
    }
    public UUID getuuid() {
        return uuid;
        }
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public String getGenre(){
        return genre;
    }
    public int getPageCount(){
        return pageCount;
    }
    public int getWordsPerPage(){
        return wordsPerPage;
    }
}
