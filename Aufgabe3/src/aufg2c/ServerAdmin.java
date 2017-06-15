//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package aufg2c;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerAdmin {
    private static final String CONFIG_FILENAME = "server.conf";
    private ServerConfig[] serverConfig;
    private int serverAnzahl = 0;
    private int serverAnzahlVerfuegbar = 0;
    private int searchStart = 0;
    private String configFilename = "server.conf";
    private static final Logger LOGGER = Logger.getLogger(ServerAdmin.class.getName());

    public ServerAdmin(String configFilename) throws IOException {
        if(configFilename != "") {
            this.configFilename = configFilename;
        }

        this.readConfigFile();
    }

    public ServerConfig bind() {
        while(true) {
            synchronized(this.serverConfig) {
                if(this.serverAnzahlVerfuegbar == 0) {
                    try {
                        this.serverConfig.wait();
                    } catch (InterruptedException var3) {
                        var3.printStackTrace();
                    }
                }

                for(int i = 0; i< serverAnzahl; ++i) {
                    if(this.serverConfig[i].isAvailable()) {
                        --this.serverAnzahlVerfuegbar;
                        this.serverConfig[i].bind();
                        return this.serverConfig[i];
                    }
                    if(i==serverAnzahl-1){
                        i=0;
                    }

                }
            }
        }
    }

    public void release(ServerConfig config) {

        synchronized(this.serverConfig) {
            config.release();
            this.serverAnzahlVerfuegbar++;
            this.serverConfig.notify();
        }
    }

    public void setLogLevel(Level level) {
        Handler[] var5;
        int var4 = (var5 = Logger.getLogger("").getHandlers()).length;

        for(int var3 = 0; var3 < var4; ++var3) {
            Handler h = var5[var3];
            h.setLevel(level);
        }

        LOGGER.setLevel(level);
        LOGGER.info("Log level set to " + level);
    }

    private void readConfigFile() throws IOException {
        FileReader fileReader = new FileReader(this.configFilename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        int var3;
        for(var3 = 0; bufferedReader.readLine() != null; ++this.serverAnzahl) {
            ;
        }

        this.serverConfig = new ServerConfig[this.serverAnzahl];
        this.serverAnzahlVerfuegbar = this.serverAnzahl;
        bufferedReader.close();
        fileReader = new FileReader(this.configFilename);

        String input;
        String[] words;
        for(bufferedReader = new BufferedReader(fileReader); (input = bufferedReader.readLine()) != null; this.serverConfig[var3++] = new ServerConfig(words[0], Integer.parseInt(words[1]), Integer.parseInt(words[2]))) {
            words = input.split(" ");
        }

        for(int i = 0; i < this.serverAnzahl; ++i) {
            LOGGER.info("Server " + i + ": " + this.serverConfig[i].toString());
        }

        bufferedReader.close();
    }
}
