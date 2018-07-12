import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.docs.Doc;
import com.vk.api.sdk.queries.docs.DocsGetMessagesUploadServerType;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class MessageManager {

    private UserActor actor2;
    private GroupActor actor;
    private VkApiClient vk;
    private Config config;
    private PolyWritter poly;

    public MessageManager(UserActor actor2, GroupActor actor, VkApiClient vk, Config config, PolyWritter poly) {
        this.actor2 = actor2;
        this.actor = actor;
        this.vk = vk;
        this.config = config;
        this.poly = poly;
    }

    public void reactToMessage(JSONObject jsonMSG){
        int userId = Integer.parseInt(jsonMSG.get("user_id").toString());
        String msg =  jsonMSG.get("body").toString();

        if (msg.startsWith("/help")) {
            sendMsg(userId, "Вызов помощи" +
                    "\n/say [日文]　- прочитать слово (до "+config.getMaxWordLim()+"　символов)." +
                    "\n/help　- вызвать список команд.");
        } else if (msg.startsWith("/say")) {
            if(msg.length() > 5) {
                if (msg.length() <= 100) {
                    sendVoice(userId, msg.substring(5));
                } else {
                    sendMsg(userId, "Слишком большое сообщение, мне лень читать!");
                }
            } else {
                sendMsg(userId, "/say [日文]");
            }
        }
    }

    public void sendMsg(int id, String msg) {
        try {
            vk.messages().send(actor).message(msg).userId(id).randomId(new Random().nextInt()).execute();


        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public void sendVoice(int id, String msg) {
        try {
            if(!Files.exists(Paths.get(PathSystem.JAP_AUDIOS_DIR+"/"+msg+".ogg"))){
                poly.createReadingFile(msg);
            }

            File file = Paths.get(PathSystem.JAP_AUDIOS_DIR+"/"+msg+".ogg").toFile();

            String respUrl = vk.docs().getMessagesUploadServer(actor2).peerId(id).
                    type(DocsGetMessagesUploadServerType.AUDIO_MESSAGE).execute().getUploadUrl();

            MultipartUtility multipart = new MultipartUtility(respUrl,"UTF-8");
            multipart.addFilePart("file", file);
            List<String> list = multipart.finish();

            JSONObject res = (JSONObject) JSONValue.parse(list.get(list.size()-1));
            String resp = res.get("file").toString();
            Doc doc =vk.docs().save(actor2, resp).execute().get(0);

            vk.messages().send(actor).attachment("doc"+doc.getOwnerId()+"_"+doc.getId()+"_"+doc.getAccessKey())
                    .userId(id).randomId(new Random().nextInt()).execute();

            if (msg.length()>config.getFileWordLim()) {
                Files.delete(Paths.get(PathSystem.JAP_AUDIOS_DIR+"/"+msg+".ogg"));
            }

        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
