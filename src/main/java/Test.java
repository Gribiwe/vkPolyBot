import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Test extends HttpServlet {

    VkAPIInit vk;
    Config configStart;
    PolyWritter polly;
    MessageManager msgManager;
    boolean booted;


    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        List<String> s = new ArrayList<>();
        BootInit bootInit = new BootInit();

        if (bootInit.initFiles()) {
            configStart = new Config();
            configStart.configInit();
            vk = new VkAPIInit(configStart.getAppID(), configStart.getGroupID(), configStart.getToken());
            polly = new PolyWritter(configStart.getFPolyKey(), configStart.getSPolyKey());
            msgManager = new MessageManager(vk.getActor2(), vk.getActor(), vk.getVk(), configStart, polly);

            s.add("init done");
            booted = true;
        }
        else {
            s.add("Please init variables in config.yml");
            try {
                Files.write(Paths.get("init.txt"), s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }

    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");

        PrintWriter pw = resp.getWriter();
        pw.println("<H1>Словарный запас Япония. by Gribiwe 2018</H1>");

    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();
        resp.setStatus(200);

        String answer = "";

        String saf = req.getReader().readLine();req.getReader().readLine();
        JSONObject object = (JSONObject) JSONValue.parse(saf);

        switch ((String) object.get("type")) {
            case "confirmation" :
                answer = configStart.getPublicAnswerCode();
                pw.println(answer+"");
                break;
            case "wall_post_new" :
                if(booted) {
                    answer = "ok";
                    pw.println(answer + "");
                    JSONObject object2 = (JSONObject) object.get("object");
                }
                break;
            case "message_new":
                if (booted) {
                    answer = "ok";
                    pw.println(answer + "");
                    JSONObject msg = (JSONObject) object.get("object");

                    msgManager.reactToMessage(msg);
                }
                break;
        }
    }
}