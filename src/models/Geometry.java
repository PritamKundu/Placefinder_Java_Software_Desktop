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
public class Geometry {
    public Coords location;
}
