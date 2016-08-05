/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dks_lab_sync;

/**
 *
 * @author how32_000
 */
public class FileData {
    private String type;
    private String name;
    private String modifyDate;
    
    FileData(String type, String name, String date) {
        this.type = type;
        this.name = name;
        this.modifyDate = date;
    }
    
    public String getType() {
        return type;
    }
    public String getName() {
        return name;
    }
    public String getDate() {
        return modifyDate;
    }
}
