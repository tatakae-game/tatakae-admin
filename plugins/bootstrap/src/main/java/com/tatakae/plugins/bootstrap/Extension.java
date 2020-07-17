import com.tatakae.admin.core.Plugin;

public class Extension implements Plugin {
    public static String name = "Plugin 1";
    public static String view = "view.fxml";
    public static String description = "Here the description of Plugin 1";

    public void start() {
        System.out.println("Start Plugin 1");
    }

    public String getMainViewName() {
        return view;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void startCLI() {
        System.out.println("Start CLI of plugin 1");
    }
}
