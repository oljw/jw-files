/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author JW
 */
public class Inhouse extends Part {
    private static final String TAG = "Inhouse.java";

    private int machineId;

    public Inhouse(int machineId) {
        this.machineId = machineId;
    }

    public int getMachineID() {
        return machineId;
    }

    public void setMachineID(int machineId) {
        this.machineId = machineId;
    }    
}
