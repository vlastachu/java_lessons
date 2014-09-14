package ru.progexcenter.lesson3.squareroot;

public class Main {

	static double sqrt(double square, double epsilon, double at){
		at = (at + square/at)/2;
		boolean isGoodEnough = Math.abs(at*at - square) < epsilon;
		if(isGoodEnough) return at;
		else return sqrt(square, epsilon, at);
	}

	static double sqrt(double square, double epsilon){
		return sqrt(square, epsilon, square/2);
	}

	static double sqrt(double square){
		return sqrt(square, 0.00001, square/2);
	}

    public static void main(String[] args) {
		System.out.println(sqrt(3));
    }
}
