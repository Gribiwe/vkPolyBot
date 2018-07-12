import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class BootInit {

    public boolean initFiles() {
        try {
            Files.createDirectory(PathSystem.BIN_DIR);
            Files.createDirectory(PathSystem.JAP_AUDIOS_DIR);
            Files.createFile(PathSystem.CONFIG_FILE);

            defaultConfigCreate();

            return false;

        } catch (FileAlreadyExistsException ignored) {
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void defaultConfigCreate() throws IOException {
        List<String> lines = new ArrayList<>();

        lines.add("First Polly key<>null");
        lines.add("Second Polly key<>null");
        lines.add("App ID<>null");
        lines.add("Group ID<>null");
        lines.add("Token<>null");
        lines.add("To create file char limit<>null");
        lines.add("Max char limit<>null");
        lines.add("Group answer code<>null");

        Files.write(PathSystem.CONFIG_FILE, lines);

    }
}