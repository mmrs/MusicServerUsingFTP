/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notused.newpackage;

import java.io.Serializable;

/**
 *
 * @author siyam
 */
public class Data implements Serializable{
    private byte[] buff;
    
    public Data(byte[] data) {
        this.buff = data;
    }

    public byte[] getData() {
        return buff;
    }
    
}
