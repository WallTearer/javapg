/**
 * Created by Kirill on 11-Jan-17.
 */
public class Index {
    public static void main(String[] args) {

        char chA = 'A';
        char chTM = '\u2122';
        System.out.println("Chars are: " + chA + ", " + chTM);

        int number = 0;
        if (number == 0) {
            System.out.println("Condition is true");
        }

        System.out.println("Number is: " + (2.0 - 1.1));
        System.out.println("Index works again!");

        stringMagic();
    }

    private static void stringMagic() {

//        char ch = '𝕆'; // this symbol has 2 code units (=4 bytes, 1 code unit=2 bytes) thus can not fit into a char value (char is 2 bytes)

        String str = "𝕆𝕷a"; //3 symbols (code points), but 1st and 2nd chars contain each 2 code units
        System.out.println("str: " + str);
        System.out.println("str length: " + str.length()); //3 chars but length is 5, because 1st and 2nd chars contain each 2 code units
        System.out.println("str first char: " + str.charAt(0)); //outputs ? because it's just the part of the whole symbol
        System.out.println("str second char: " + str.charAt(1)); //outputs ? because it's just the part of the whole symbol
        System.out.println("str correct length (code point count): " + str.codePointCount(0, str.length()));

        // iterating through string symbols
        int[] symbolsInString = str.codePoints().toArray();
        for (int i = 0; i < symbolsInString.length; i++) {
            System.out.printf("%d symbol: as character - %c and as string - %s%n", i, symbolsInString[i], symbolsInString[i]);
        }
    }
}
