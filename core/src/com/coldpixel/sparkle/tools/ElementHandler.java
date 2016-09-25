/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coldpixel.sparkle.tools;

import com.coldpixel.sparkle.Main;

/**
 *
 * @author mike
 */
public class ElementHandler {
    
    public float getDamage(Main.elementType attacker, Main.elementType victim){
        if((attacker == Main.elementType.AIR && victim == Main.elementType.WATER)
                || (attacker == Main.elementType.WATER && victim == Main.elementType.FIRE)
                || (attacker == Main.elementType.FIRE && victim == Main.elementType.EARTH)
                || (attacker == Main.elementType.EARTH && victim == Main.elementType.AIR)){ 
            return 2;
        }else if((attacker == Main.elementType.WATER && victim == Main.elementType.AIR)
                || (attacker == Main.elementType.FIRE && victim == Main.elementType.WATER)
                || (attacker == Main.elementType.EARTH && victim == Main.elementType.FIRE)
                || (attacker == Main.elementType.AIR && victim == Main.elementType.EARTH)){
            return 0.5f;
        }else{
            return 1;
        }
    }
}
