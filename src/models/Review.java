/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author fayshaluddin
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {
    public String author_name;
    public int rating;
    public String text;
    public String relative_time_description;
    public String profile_photo_url;
}
