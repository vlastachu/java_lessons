package ru.progexcenter.lesson2.calc;

/**
 * Application calculate expression written in postfix notation
 */
class PostfixCalculator{
    public static void main(String[] args){
        String str = args[0];
        char c;
        int[] operands = new int[100];
        //initialize your state variables
        
        for(int i = 0; i < str.length(); ++i){
            c = str.charAt(i);
            //TODO parse and calculate
        }
        System.out.println(operands[0]);
    }
} 
