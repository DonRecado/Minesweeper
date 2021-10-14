package minesweeper;

import java.util.*;

enum Marker {
    FREE,
    MINE,
}

public class MineField {
    private int xAxis;
    private int yAxis;
    private int mines;
    private char[][] playArr;
    private char[][] playField;
    private final Scanner scanner;

    public MineField(int xAxis, int yAxis, int mines, Scanner scanner) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.mines = mines;
        this.scanner = scanner;
        this.playField = new char[yAxis][xAxis];
        this.playArr = new char[yAxis][xAxis];
        setPlayArr();

    }

    public void play() {

        boolean gameOver = false;
        boolean lost = false;

        while (!gameOver) {
            System.out.print("Set/unset mines marks or claim a cell as free:");
            String[] input = scanner.nextLine().split(" ");

            try {
                int xCordinate = Integer.parseInt(input[0]) - 1;
                int yCordinate = Integer.parseInt(input[1]) - 1;


                Marker marker = null;
                switch (input[2].toUpperCase()) {
                    case "FREE":
                        marker = Marker.FREE;
                        break;
                    case "MINE":
                        marker = Marker.MINE;
                        break;
                    default:
                        marker = null;
                        break;
                }

                if (marker != null && xCordinate < xAxis && yCordinate < yAxis && xCordinate >= 0 && yCordinate >= 0) {
                    if (marker.equals(Marker.FREE)) {
                        if (playArr[yCordinate][xCordinate] == 'X') {
                            gameOver = true;
                            lost = true;
                        } else {
                            if (playArr[yCordinate][xCordinate] == '.' &&  playField[yCordinate][xCordinate] == '.') {
                                processHints(yCordinate, xCordinate,'.','/');
                            } else if (playField[yCordinate][xCordinate] == '*' || playField[yCordinate][xCordinate] == '/') {
                                playField[yCordinate][xCordinate] = '.';
                            } else {
                                playField[yCordinate][xCordinate] = playArr[yCordinate][xCordinate];
                            }
                        }
                    }
                    if (marker.equals(Marker.MINE)) {
                        if (playField[yCordinate][xCordinate] != '*') {
                            playField[yCordinate][xCordinate] = '*';

                            if (playArr[yCordinate][xCordinate] == 'X') {
                                this.mines--;
                                if (this.mines == 0) {
                                    gameOver = true;
                                    lost = false;
                                    break;
                                }
                            }
                        } else {
                            playField[yCordinate][xCordinate] = '.';

                            if (playArr[yCordinate][xCordinate] == 'X') {
                                this.mines++;
                            }
                        }
                    }
                }

                if (!gameOver) {
                    System.out.println(this);
                }


            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


        }
        if (lost) {
            for(int i=0; i<playArr.length; i++) {
                for(int j = 0; j < playArr[i].length; j++) {
                    if(playArr[i][j] == 'X') {
                        playField[i][j] = 'X';
                    }
                }
            }
            System.out.println(this);
            System.out.println("You stepped on a mine and failed!");
        } else {
            System.out.println("Congratulations! You found all the mines!");
        }
    }

    private boolean isValid(int yCoordinate, int xCoordinate) {
        return xCoordinate >= 0 && xCoordinate < xAxis && yCoordinate >= 0 && yCoordinate < yAxis;
    }

    private void processHints(int yCoordinate, int xCoordinate, char preV, char newV) {

        if(!isValid(yCoordinate,xCoordinate)) {
            return;
        }
        if(playArr[yCoordinate][xCoordinate] != preV && ((int)playArr[yCoordinate][xCoordinate] >= 48 && (int)playArr[yCoordinate][xCoordinate] <= 57) || playField[yCoordinate][xCoordinate] == '*') {
            playField[yCoordinate][xCoordinate] = playArr[yCoordinate][xCoordinate];
            return;
        }
        if(playField[yCoordinate][xCoordinate] == newV) {
            return;
        }

        if(playField[yCoordinate][xCoordinate] == preV) {
            playField[yCoordinate][xCoordinate] = newV;
            processHints(yCoordinate + 1, xCoordinate,preV,newV);
            processHints(yCoordinate - 1, xCoordinate,preV,newV);
            processHints(yCoordinate, xCoordinate - 1,preV,newV);
            processHints(yCoordinate, xCoordinate + 1,preV,newV);
            processHints(yCoordinate - 1, xCoordinate - 1,preV,newV);
            processHints(yCoordinate -1, xCoordinate + 1,preV,newV);
            processHints(yCoordinate + 1, xCoordinate -1,preV,newV);
            processHints(yCoordinate + 1, xCoordinate +1,preV,newV);
        }
    }


    private void setPlayArr() {
        for (int i = 0; i < playArr.length; i++) {
            Arrays.fill(playArr[i], '.');
            Arrays.fill(playField[i], '.');
        }

        int countMines = 0;
        while (countMines < mines) {
            int[] randomMine = createRandomMine();
            if (playArr[randomMine[0]][randomMine[1]] != 'X') {
                playArr[randomMine[0]][randomMine[1]] = 'X';
                countMines++;
            } else {
                boolean foundPlace = false;
                for (int i = 0; i < playArr.length; i++) {
                    for (int j = 0; j < playArr[i].length; j++) {
                        if(playArr[i][j] != 'X') {
                            playArr[i][j] = 'X';
                            mines++;
                            foundPlace = true;
                        }
                    }
                }
                if(!foundPlace) {
                    System.out.println("No more space for mines");
                    return;
                }
            }

        }

        displayHints();
    }


    private void displayHints() {
        for (int i = 0; i < playArr.length; i++) {
            for (int j = 0; j < playArr[i].length; j++) {
                if (playArr[i][j] == 'X') {
                    if (i - 1 >= 0) {
                        playArr[i - 1][j] = playArr[i - 1][j] == '.' ? '1' : playArr[i - 1][j] == 'X' ? 'X' : (char) ((int) playArr[i - 1][j] + 1); // Field above
                        if (j - 1 >= 0) {
                            playArr[i - 1][j - 1] = playArr[i - 1][j - 1] == '.' ? '1' : playArr[i - 1][j - 1] == 'X' ? 'X' : (char) ((int) playArr[i - 1][j - 1] + 1); // Field left above
                        }
                        if (j + 1 < playArr[i].length) {
                            playArr[i - 1][j + 1] = playArr[i - 1][j + 1] == '.' ? '1' : playArr[i - 1][j + 1] == 'X' ? 'X' : (char) ((int) playArr[i - 1][j + 1] + 1); // Field right above
                        }
                    }
                    if (i + 1 < playArr.length) {
                        playArr[i + 1][j] = playArr[i + 1][j] == '.' ? '1' : playArr[i + 1][j] == 'X' ? 'X' : (char) ((int) playArr[i + 1][j] + 1); // Field bellow
//
                        if (j - 1 >= 0) {
                            playArr[i + 1][j - 1] = playArr[i + 1][j - 1] == '.' ? '1' : playArr[i + 1][j - 1] == 'X' ? 'X' : (char) ((int) playArr[i + 1][j - 1] + 1); // Field left bellow
                        }
//
                        if (j + 1 < playArr[i].length) {
                            playArr[i + 1][j + 1] = playArr[i + 1][j + 1] == '.' ? '1' : playArr[i + 1][j + 1] == 'X' ? 'X' : (char) ((int) playArr[i + 1][j + 1] + 1); // Field right bellow
                        }
                    }
//
                    if (j - 1 >= 0) {
                        playArr[i][j - 1] = playArr[i][j - 1] == '.' ? '1' : playArr[i][j - 1] == 'X' ? 'X' : (char) ((int) playArr[i][j - 1] + 1); // Field left
                    }
//
                    if (j + 1 < playArr[i].length) {
                        playArr[i][j + 1] = playArr[i][j + 1] == '.' ? '1' : playArr[i][j + 1] == 'X' ? 'X' : (char) ((int) playArr[i][j + 1] + 1); // Field right
                    }
                }
            }
        }
    }

    private int[] createRandomMine() {
        Random random = new Random();
        int xRandom = random.nextInt(xAxis - 1);
        int yRandom = random.nextInt(yAxis - 1);

        return new int[]{xRandom, yRandom};
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n |123456789|\n");
        stringBuilder.append("-|---------|\n");
        for (int i = 0; i < playField.length; i++) {
            stringBuilder.append(i + 1 + "|");
            for (int j = 0; j < playField[i].length; j++) {
                stringBuilder.append(playField[i][j]);
            }
            stringBuilder.append("|\n");
        }
        stringBuilder.append("-|---------|\n");
        return stringBuilder.toString();
    }
}
