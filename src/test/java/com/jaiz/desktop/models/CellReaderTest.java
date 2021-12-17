package com.jaiz.desktop.models;

import org.junit.Test;

public class CellReaderTest {

    @Test
    public void parseTest(){
        CellReader cr=new CellReader();

        cr.parse("AZ59");
    }

    @Test
    public void charTest(){
        char a='A';
        char z='Z';

        System.out.println((int)a);
        System.out.println((int)z);
    }

    @Test
    public void colPos2IndexTest(){
        CellReader cr=new CellReader();

        int i = cr.colPos2Index("ZZ");
        System.out.println(i);
    }
}
