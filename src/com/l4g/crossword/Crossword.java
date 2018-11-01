package com.javarush.task.task20.task2027;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* 
Кроссворд
*/
public class Solution {
    public static void main(String[] args) {
        int[][] crossword = new int[][]{
                //0    1    2    3    4    5    6
                {'f', 'd', 'e', 'r', 'l', 'k', 'u'},//0
                {'u', 's', 'a', 'm', 'e', 'o', 'n'},//1
                {'l', 'n', 'g', 'r', 'o', 'v', 's'},//2
                {'m', 'l', 'p', 'e', 'r', 'h', 't'},//3
                {'p', 'o', 'e', 'z', 'j', 'j', 'y'},//4
                {'p', 'o', 'e', 'e', 'j', 'j', 'l'},//5
                {'p', 'o', 'e', 'e', 'j', 'j', 'i'},//6
                {'p', 'o', 'e', 'u', 'j', 'j', 's'},//7
                {'p', 'o', 'e', 'q', 's', 'j', 'h'},//8
                {'p', 'o', 'e', 's', 'j', 'j', 'k'},//9
        };
        List<Word> words = (detectAllWords(crossword, "squeeze", "stylish", "unstylish", "lleejj", "jjeell", "qsj", "edf"));
        for (Word word:words) {
            System.out.println(word);
        }
        System.out.println("List<Word> words size is: " + words.size());
   /*
Ожидаемый результат
home - (5, 3) - (2, 0)
same - (1, 1) - (4, 1)
         */
    }

    public static List<Word> detectAllWords(int[][] crossword, String... words) {
        char[][] charsCrossword = getCharArrayFromIntegerArray(crossword);
        List<Integer[][]> allCoordinates = null;
        List<Word> wordList = new ArrayList();
        int[][] startCoordinates = detectStartCoordinates(charsCrossword, words);

        for(int wordsQuantity = 0; wordsQuantity < words.length; wordsQuantity++){
            for(int i = 0; i < startCoordinates.length; i++){
                int column = startCoordinates[i][0];
                int row = startCoordinates[i][1];
                allCoordinates = detectFinalCoordinates(charsCrossword, column, row, words[wordsQuantity]);
                //----------------------------//
                if(allCoordinates.size() > 0){
                    int resSize = allCoordinates.size();
                    for (int j = 0; j < allCoordinates.size(); j++) {
                        wordList.add(new Word(words[wordsQuantity]));
                        int startX = allCoordinates.get(j)[0][1];
                        int startY = allCoordinates.get(j)[0][0];
                        int endX = allCoordinates.get(j)[words[wordsQuantity].length() - 1][1];
                        int endY = allCoordinates.get(j)[words[wordsQuantity].length() - 1][0];
                        wordList.get(wordList.size() - 1).setStartPoint(startX, startY);
                        wordList.get(wordList.size() - 1).setEndPoint(endX, endY);
                    }
                }
            }
        }
        clearRepeatedWords(wordList);
        return wordList;
    }

    private static void clearRepeatedWords(List<Word> wordList) {

        for (int i = 0; i < wordList.size() - 1; i++) {
            if (wordList.get(i).equals(wordList.get(i + 1))) wordList.remove(i--);
        }
    }

    private static char[][] getCharArrayFromIntegerArray(int[][] crossword) {
        char[][] result = new char[crossword.length][];
        for (int i = 0; i < crossword.length; i++) {
            result[i] = new char[crossword[i].length];
            for (int j = 0; j < crossword[i].length; j++){
                result[i][j] = (char) crossword[i][j];
            }
        }
        return result;
    }

    /**
     *Считаем все коолличество букв, содержащихся в массиве "char[][] a",
     *с которых начинаются искомые слова в массиве "String[] words".
     *Находим их координаты и заносим их в массив "int[][] startCoordinates" и возвращаем его.
     */
    private static int[][] detectStartCoordinates(char[][] a, String[] words){
        //----------------------------------//
        int countStartCoordinates = 0;
        //----------------------------------//
        for(int ind=0;ind<words.length;ind++){
            char c = words[ind].charAt(0);
            for(int j=0;j<a.length;j++){
                for(int k=0;k<a[j].length;k++){
                    if(c == a[j][k]){
                        countStartCoordinates++;
//                    System.out.println(countStartCoordinates);
                    }
                }
            }
        }
        //----------------------------------//
        int[][] startCoordinates = new int[countStartCoordinates][2];
        //----------------------------------//
        int index = 0;
        for(int ind=0;ind<words.length;ind++){
            char c = words[ind].charAt(0);
            for(int j=0;j<a.length;j++){
                for(int k=0;k<a[j].length;k++){
                    if(c == a[j][k]){
                        startCoordinates[index][0] = j;
                        startCoordinates[index++][1] = k;
//                        System.out.println(c + " (" + k + ") " +" (" + j + ") ");
                    }
                }
            }
        }
//        System.out.println(Arrays.deepToString(startCoordinates));
        return startCoordinates;
    }

    /**
     * Гуляем по массиву "char[][] a" во все стороны от стартовых координат(влево, вправо, ввер, вниз и по всем диагоналям),
     * и собираем в массив "char[] temp" все буквы из массива "a", и в массив "Integer[][] coordinates"
     * записываем все координаты. Далее проверяем соответствие "String word" слову из массива "temp",
     * при совпадении "coordinates" добавляеь в "List<Integer[][]> allCoordinates".
     * Возврщаем список "List<Integer[][]> allCoordinates".
     */
    private static List<Integer[][]> detectFinalCoordinates(char[][] a, int column, int row, String word){
        //----------------------------------//
        List<Integer[][]> allCoordinates = new ArrayList<>();
        char[] temp = null;
        Integer[][] coordinates = null;
        String w = null;
        int length = word.length();
        //----------------------------------//
        boolean hasRight = row + length <= a[column].length;
        boolean hasLeft = row - length + 1 >= 0;
        boolean hasBottom = column + length <= a.length;
        boolean hasTop = column - length + 1 >= 0;
        //----------------------------------//
        //Right searching
        //----------------------------------//
        if(hasRight){
//            System.out.println("right");
            int index = 0;
            temp = new char[length];
            coordinates = new Integer[length][2];
            for(int i = 0;i < length; i++){
                temp[index] = a[column][row + i];
                coordinates[index][0] = column;
                coordinates[index++][1] = row + i;
            }
            w = String.valueOf(temp);
            if(w.equals(word)){
//                System.out.println(w);
//                System.out.println(Arrays.deepToString(coordinates));
                allCoordinates.add(coordinates);
            }
        }
        //----------------------------------//
        //Left searching
        //----------------------------------//
        if(hasLeft){
//            System.out.println("left");
            int index = 0;
            temp = new char[length];
            coordinates = new Integer[length][2];
            for(int i = 0;i < length; i++) {
                temp[index] = a[column][row - i];
                coordinates[index][0] = column;
                coordinates[index++][1] = row - i;
            }
            w = String.valueOf(temp);
            if(w.equals(word)){
//                System.out.println(w);
//                System.out.println(Arrays.deepToString(coordinates));
                allCoordinates.add(coordinates);
            }
        }
        //----------------------------------//
        //Bottom searching
        //----------------------------------//
        if(hasBottom){
//            System.out.println("bottom");
            int index = 0;
            temp = new char[length];
            coordinates = new Integer[length][2];
            for(int i = 0;i < length; i++){
                temp[index] = a[column + i][row];
                coordinates[index][0] = column + i;
                coordinates[index++][1] = row;
            }
            w = String.valueOf(temp);
            if(w.equals(word)){
//                System.out.println(w);
//                System.out.println(Arrays.deepToString(coordinates));
                allCoordinates.add(coordinates);
            }
        }
        //----------------------------------//
        //Top searching
        //----------------------------------//
        if(hasTop){
//            System.out.println("top");
            int index = 0;
            temp = new char[length];
            coordinates = new Integer[length][2];
            for(int i = 0; i < length; i++){
                temp[index] = a[column - i][row];
                coordinates[index][0] = column - i;
                coordinates[index++][1] = row;
            }
            w = String.valueOf(temp);
            if(w.equals(word)){
//                System.out.println(w);
//                System.out.println(Arrays.deepToString(coordinates));
                allCoordinates.add(coordinates);
            }
        }
        //----------------------------------//
        //Right & Bottom searching
        //----------------------------------//
        if(hasRight && hasBottom){
//            System.out.println("right & bottom");
            int index = 0;
            temp = new char[length];
            coordinates = new Integer[length][2];
            for(int i = 0; i < length; i++){
                temp[index] = a[column + i][row + i];
                coordinates[index][0] = column + i;
                coordinates[index++][1] = row + i;
            }
            w = String.valueOf(temp);
            if(w.equals(word)){
//                System.out.println(w);
//                System.out.println(Arrays.deepToString(coordinates));
                allCoordinates.add(coordinates);
            }
        }
        //----------------------------------//
        //Right & Top searching
        //----------------------------------//
        if(hasRight && hasTop){
//            System.out.println("right & top");
            int index = 0;
            temp = new char[length];
            coordinates = new Integer[length][2];
            for(int i = 0; i < length; i++){
                temp[index] = a[column - i][row + i];
                coordinates[index][0] = column - i;
                coordinates[index++][1] = row + i;
            }
            w = String.valueOf(temp);
            if(w.equals(word)){
//                System.out.println(w);
//                System.out.println(Arrays.deepToString(coordinates));
                allCoordinates.add(coordinates);
            }
        }
        //----------------------------------//
        //Left & Bottom searching
        //----------------------------------//
        if(hasLeft && hasBottom){
//            System.out.println("left & bottom");
            int index = 0;
            temp = new char[length];
            coordinates = new Integer[length][2];
            for(int i = 0; i < length; i++){
                temp[index] = a[column + i][row - i];
                coordinates[index][0] = column + i;
                coordinates[index++][1] = row - i;
            }
            w = String.valueOf(temp);
            if(w.equals(word)){
//                System.out.println(w);
//                System.out.println(Arrays.deepToString(coordinates));
                allCoordinates.add(coordinates);
            }
        }
        //----------------------------------//
        //Left & Top searching
        //----------------------------------//
        if(hasLeft & hasTop){
//            System.out.println("left & top");
            int index = 0;
            temp = new char[length];
            coordinates = new Integer[length][2];
            for(int i = 0; i < length; i++){
                temp[index] = a[column - i][row - i];
                coordinates[index][0] = column - i;
                coordinates[index++][1] = row - i;
            }
            w = String.valueOf(temp);
            if(w.equals(word)){
//                System.out.println(w);
//                System.out.println(Arrays.deepToString(coordinates));
                allCoordinates.add(coordinates);
            }
        }
//        System.out.println(allCoordinates.size());
//        System.out.println("::::::::::::::::::::::::::::::::::::::::");
        return allCoordinates;
    }

    public static class Word {
        private String text;
        private int startX;
        private int startY;
        private int endX;
        private int endY;

        public Word(String text) {
            this.text = text;
        }

        public void setStartPoint(int i, int j) {
            startX = i;
            startY = j;
        }

        public void setEndPoint(int i, int j) {
            endX = i;
            endY = j;
        }

        @Override
        public String toString() {
            return String.format("%s - (%d, %d) - (%d, %d)", text, startX, startY, endX, endY);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass())return false;
            Word anotherWord = (Word) o;
            return this.text.equals(anotherWord.text) && this.startX == anotherWord.startX &&
                    this.startY == anotherWord.startY && this.endX == anotherWord.endX &&
                    this.endY == anotherWord.endY;
        }

    }
}
