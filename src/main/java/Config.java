import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by nikit on 2018/02/22.
 */
public class Config {


    private String fPolyKey;
    private String sPolyKey;
    private int appID;
    private int groupID;
    private String token;
    private int fileWordLim;
    private int maxWordLim;
    private String publicAnswerCode;

    public void configInit() {
        try {
            List<String> lines = Files.readAllLines(PathSystem.CONFIG_FILE);
            fPolyKey = lines.get(0).split("<>")[1];
            sPolyKey = lines.get(1).split("<>")[1];
            appID = Integer.parseInt(lines.get(2).split("<>")[1]);
            groupID = Integer.parseInt(lines.get(3).split("<>")[1]);
            token = lines.get(4).split("<>")[1];
            fileWordLim = Integer.parseInt(lines.get(5).split("<>")[1]);
            maxWordLim = Integer.parseInt(lines.get(6).split("<>")[1]);
            publicAnswerCode = lines.get(7).split("<>")[1];

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPublicAnswerCode() {
        return publicAnswerCode;
    }

    public int getFileWordLim() {
        return fileWordLim;
    }

    public int getMaxWordLim() {
        return maxWordLim;
    }

    public String getFPolyKey() {
        return fPolyKey;
    }

    public String getSPolyKey() {
        return sPolyKey;
    }

    public int getAppID() {
        return appID;
    }

    public int getGroupID() {
        return groupID;
    }

    public String getToken() {
        return token;
    }
}