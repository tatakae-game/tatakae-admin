import com.tatakae.admin.cli.Application;
import com.tatakae.admin.gui.Main;

public class Starter {
    public static void main(String[] args) {

        System.out.println(args.length);

        if (args.length == 0 || args[0].equals("gui")) {
            Main.main(args);
        } else {
            Application.main(args);
        }
    }
}
