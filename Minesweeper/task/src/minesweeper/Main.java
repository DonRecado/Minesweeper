package minesweeper;

import java.util.Scanner;

public class Main {
    static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        // write your code here

        System.out.print("How many mines do you want on the field? ");
        int mines = scanner.nextInt();
        scanner.nextLine();

        MineField mineField = new MineField(9,9 , mines,scanner);
//
        System.out.println(mineField);

        mineField.play();
    }
}
