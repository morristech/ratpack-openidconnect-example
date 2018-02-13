package ratpack.oidc.example;

import org.pac4j.oidc.client.GoogleOidcClient;
import ratpack.guice.Guice;
import ratpack.oidc.example.api.ApiEndpoints;
import ratpack.oidc.example.api.ApiModule;
import ratpack.oidc.example.config.AuthConfig;
import ratpack.pac4j.RatpackPac4j;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;

public class Main {

    public static void main(String... args) throws Exception {
        RatpackServer.start(s -> s
                .serverConfig(c -> c
                        .yaml("config.yaml")
                        .env()
                        .require("/auth", AuthConfig.class)
                        .baseDir(BaseDir.find())
                        .build()
                )
                .registry(Guice.registry(b -> b
                        .module(ApiModule.class))
                )
                .handlers(chain -> chain
                        .all(RatpackPac4j.authenticator(chain.getRegistry().get(GoogleOidcClient.class)))
                        .insert(ApiEndpoints.class)
                        .files(f -> f.dir("public").indexFiles("index.html"))
                )
        );
    }
}
