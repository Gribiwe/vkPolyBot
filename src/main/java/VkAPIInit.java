import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;

public class VkAPIInit {

    private String TOKEN;
    private int APP_ID;
    private int GROUP_ID;

    private VkApiClient vk;
    private GroupActor actor;
    private UserActor actor2;

    public VkApiClient getVk() {
        return vk;
    }

    public GroupActor getActor() {
        return actor;
    }

    public UserActor getActor2() {
        return actor2;
    }

    public VkAPIInit(int appID, int groupId, String token ) {

        APP_ID = appID;
        TOKEN = token;
        GROUP_ID = groupId;

        TransportClient transportClient = HttpTransportClient.getInstance();
         vk = new VkApiClient(transportClient);

        actor = new GroupActor(GROUP_ID, TOKEN);
        actor2 = new UserActor(APP_ID, TOKEN);

    }
}
