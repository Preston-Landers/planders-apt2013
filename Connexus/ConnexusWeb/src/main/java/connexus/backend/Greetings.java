package connexus.backend;

import javax.inject.Named;
import java.util.ArrayList;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.users.User;
import connexus.Config;

/**
 * Defines v1 of a helloworld API, which provides simple "greeting" methods.
 */
@Api(name = "helloworld", version = "v1",
        scopes = {Config.EMAIL_SCOPE},
        clientIds = {
                com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID,
                Config.ANDROID_APP_KEY,
                Config.WEB_APP_KEY
        },
        audiences = {Config.ANDROID_AUDIENCE})
public class Greetings {
    public static ArrayList<HelloBackend> greetings = new ArrayList<HelloBackend>();

    static {
        greetings.add(new HelloBackend("hello world!"));
        greetings.add(new HelloBackend("goodbye world!"));
    }

    public HelloBackend getGreeting(@Named("id") Integer id) {
        return greetings.get(id);
    }

    @ApiMethod(name = "greetings.authed", path = "greeting/authed")
    public HelloBackend authedGreeting(User user) {
        HelloBackend response;
        if (user == null) {
            response = new HelloBackend("hello, MR NOT LOGGED IN");
        } else {
            response = new HelloBackend("hello " + user.getEmail());
        }

        return response;
    }
}