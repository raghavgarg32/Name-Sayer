package helpers;

/**
 * This is a helper class that allows users selected name to fit on the screen
 */
public class MakeHeadingNameFit {

    /**
     * If the name is too long then this method will add ... to make it fit on the scene
     * @param name
     * @return
     */
    public static String changeName(String name){
        if (name.length() > 20) {
            name = name.substring(0, 17);
            name = name + "...";
        }
        return  name;
    }

}
