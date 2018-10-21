package helpers;

public class MakeHeadingNameFit {

    public static String changeName(String name){
        if (name.length() > 20) {
            name = name.substring(0, 17);
            name = name + "...";
        }
        return  name;
    }

}
