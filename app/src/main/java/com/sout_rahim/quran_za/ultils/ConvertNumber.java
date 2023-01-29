package com.sout_rahim.quran_za.ultils;

public final class ConvertNumber {

    private String input;
    private final String output;

    public ConvertNumber(String input){
        this.input=input;
        this.output=normalize();
    }


    private String normalize(){

    
       input = input.replaceAll("(1)", "١");
       input = input.replaceAll("(2)", "٢");
       input = input.replaceAll("(3)", "٣");
       input = input.replaceAll("(4)", "٤");
       input = input.replaceAll("(5)", "٥");
       input = input.replaceAll("(6)", "٦");
       input = input.replaceAll("(7)", "٧");
       input = input.replaceAll("(8)", "٨");
       input = input.replaceAll("(9)", "٩");
       input = input.replaceAll("(0)", "٠");

        return input;
    }
    public String getOutput() {
        return output;
    }
}
