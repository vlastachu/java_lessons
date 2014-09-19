import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;
import org.fusesource.jansi.AnsiConsole;

public class ConsoleCalendar {
    public static void main (String[] args) {
        AnsiConsole.systemInstall();
        System.out.println(ansi().eraseScreen().fg(RED).a("Hello").fg(GREEN).a(" World").reset());
        AnsiConsole.systemUninstall();
    }
}
