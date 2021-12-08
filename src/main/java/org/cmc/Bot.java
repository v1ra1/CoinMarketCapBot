package org.cmc;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.security.auth.login.LoginException;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Bot extends ListenerAdapter {

    public static JDA jda;

    public static String token;

    public static String coinmarketcapApiKey;

    public static String guildId;

    public static String coinToken;

    public static String presenceMessageType;

    public static String prePresenceMessage;

    public static String presenceMessageIcon;

    public static int intervalMins;

    public static void main(String[] args) throws LoginException, NullPointerException, InterruptedException {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream("app.config")) {
            prop.load(fis);
        } catch (IOException ex) {
            try (Writer inputStream = new FileWriter(new File("app.config"))) {
                prop.setProperty("COINMARKETCAP_API_KEY", "");
                prop.setProperty("GUILD_ID", "");
                prop.setProperty("COIN_TOKEN", "CWAR");
                prop.setProperty("DISCORD_TOKEN", "");
                prop.setProperty("INTERVAL_MINUTES", "15");
                prop.setProperty("PRESENCE_MESSAGE_TYPE", "percent_change_24h");
                prop.store(inputStream, "COINMARKETCAP BOT");
                System.out.println("PLEASE CONFIGURE THE app.config FILE THAT WAS JUST GENERATED BEFORE RUNNING.");
                return;
            } catch (IOException er) {
                ex.printStackTrace();
                return;
            }
        }
        coinToken = prop.getProperty("COIN_TOKEN");
        guildId = prop.getProperty("GUILD_ID");
        coinmarketcapApiKey = prop.getProperty("COINMARKETCAP_API_KEY");
        token = prop.getProperty("DISCORD_TOKEN");
        intervalMins = Integer.parseInt(prop.getProperty("INTERVAL_MINUTES"));
        presenceMessageType = prop.getProperty("PRESENCE_MESSAGE_TYPE");

        jda = JDABuilder.createDefault(token)
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS)
                .build();
        Thread.sleep(3000);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                JSONObject quoteObject = null;
                try {
                    quoteObject = getQuote();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                NumberFormat formatter = new DecimalFormat("#0.00");
                Guild guild = jda.getGuildById(guildId);
                guild.modifyNickname(guild.getSelfMember(), "$" + formatter.format((Double.parseDouble(((BigDecimal) quoteObject.get("price")).toString())))).queue();
                double trendNumber;
                double volNumber;
                switch(presenceMessageType) {
                    case "volume_24h":
                        prePresenceMessage = "VOL 24HR";
                        volNumber = Double.parseDouble(quoteObject.get(presenceMessageType).toString());
                        if (volNumber >= 1000000000) {
                            trendNumber = volNumber / 1000000000;
                            jda.getPresence().setActivity(Activity.playing(prePresenceMessage+" | "+formatter.format(trendNumber)+"b"));
                        } else if(volNumber >= 1000000) {
                            trendNumber = volNumber / 1000000;
                            jda.getPresence().setActivity(Activity.playing(prePresenceMessage+" | "+formatter.format(trendNumber)+"m"));
                        } else if(volNumber >= 1000000) {
                            trendNumber = volNumber / 100000;
                            jda.getPresence().setActivity(Activity.playing(prePresenceMessage+" | "+formatter.format(trendNumber)+"k"));
                        } else {
                            trendNumber = volNumber;
                            jda.getPresence().setActivity(Activity.playing(prePresenceMessage+" | "+trendNumber));
                        }
                        break;
                    case "fully_diluted_market_cap":
                        prePresenceMessage = "DMCAP";
                        volNumber = Double.parseDouble(quoteObject.get(presenceMessageType).toString());
                        if (volNumber >= 1000000000) {
                            trendNumber = volNumber / 1000000000;
                            jda.getPresence().setActivity(Activity.playing(prePresenceMessage+" | "+formatter.format(trendNumber)+"b"));
                        } else if(volNumber >= 1000000) {
                            trendNumber = volNumber / 1000000;
                            jda.getPresence().setActivity(Activity.playing(prePresenceMessage+" | "+formatter.format(trendNumber)+"m"));
                        } else if(volNumber >= 1000000) {
                            trendNumber = volNumber / 100000;
                            jda.getPresence().setActivity(Activity.playing(prePresenceMessage+" | "+formatter.format(trendNumber)+"k"));
                        } else {
                            trendNumber = volNumber;
                            jda.getPresence().setActivity(Activity.playing(prePresenceMessage+" | "+trendNumber));
                        }
                        break;
                    case "market_cap":
                        prePresenceMessage = "MCAP";
                        volNumber = Double.parseDouble(quoteObject.get(presenceMessageType).toString());
                        if (volNumber >= 1000000000) {
                            trendNumber = volNumber / 1000000000;
                            jda.getPresence().setActivity(Activity.playing(prePresenceMessage+" | "+formatter.format(trendNumber)+"b"));
                        } else if(volNumber >= 1000000) {
                            trendNumber = volNumber / 1000000;
                            jda.getPresence().setActivity(Activity.playing(prePresenceMessage+" | "+formatter.format(trendNumber)+"m"));
                        } else if(volNumber >= 1000000) {
                            trendNumber = volNumber / 100000;
                            jda.getPresence().setActivity(Activity.playing(prePresenceMessage+" | "+formatter.format(trendNumber)+"k"));
                        } else {
                            trendNumber = volNumber;
                            jda.getPresence().setActivity(Activity.playing(prePresenceMessage+" | "+trendNumber));
                        }
                        break;
                    case "volume_change_24h":
                        trendNumber = Double.parseDouble(((BigDecimal) quoteObject.get(presenceMessageType)).toString());
                        if (trendNumber < 0) {
                            presenceMessageIcon = "\uD83D\uDCC9";
                        } else {
                            presenceMessageIcon = "\uD83D\uDCC8";
                        }
                        jda.getPresence().setActivity(Activity.playing("VOL 24HR | "+presenceMessageIcon+" "+ formatter.format(trendNumber)+"%"));
                        break;
                    case "percent_change_1h":
                        trendNumber = Double.parseDouble(((BigDecimal) quoteObject.get(presenceMessageType)).toString());
                        if (trendNumber < 0) {
                            presenceMessageIcon = "\uD83D\uDCC9";
                        } else {
                            presenceMessageIcon = "\uD83D\uDCC8";
                        }
                        jda.getPresence().setActivity(Activity.playing("1HR | "+presenceMessageIcon+" "+ formatter.format(trendNumber)+"%"));
                        break;
                    case "percent_change_24h":
                        trendNumber = Double.parseDouble(((BigDecimal) quoteObject.get(presenceMessageType)).toString());
                        if (trendNumber < 0) {
                            presenceMessageIcon = "\uD83D\uDCC9";
                        } else {
                            presenceMessageIcon = "\uD83D\uDCC8";
                        }
                        jda.getPresence().setActivity(Activity.playing("24HR | "+presenceMessageIcon+" "+ formatter.format(trendNumber)+"%"));
                        break;
                    case "percent_change_7d":
                        trendNumber = Double.parseDouble(((BigDecimal) quoteObject.get(presenceMessageType)).toString());
                        if (trendNumber < 0) {
                            presenceMessageIcon = "\uD83D\uDCC9";
                        } else {
                            presenceMessageIcon = "\uD83D\uDCC8";
                        }
                        jda.getPresence().setActivity(Activity.playing("7D | "+presenceMessageIcon+" "+ formatter.format(trendNumber)+"%"));
                        break;
                    case "percent_change_30d":
                        trendNumber = Double.parseDouble(((BigDecimal) quoteObject.get(presenceMessageType)).toString());
                        if (trendNumber < 0) {
                            presenceMessageIcon = "\uD83D\uDCC9";
                        } else {
                            presenceMessageIcon = "\uD83D\uDCC8";
                        }
                        jda.getPresence().setActivity(Activity.playing("30D | "+presenceMessageIcon+" "+ formatter.format(trendNumber)+"%"));
                        break;
                    case "percent_change_60d":
                        trendNumber = Double.parseDouble(((BigDecimal) quoteObject.get(presenceMessageType)).toString());
                        if (trendNumber < 0) {
                            presenceMessageIcon = "\uD83D\uDCC9";
                        } else {
                            presenceMessageIcon = "\uD83D\uDCC8";
                        }
                        jda.getPresence().setActivity(Activity.playing("60D | "+presenceMessageIcon+" "+ formatter.format(trendNumber)+"%"));
                        break;
                    case "percent_change_90d":
                        trendNumber = Double.parseDouble(((BigDecimal) quoteObject.get(presenceMessageType)).toString());
                        if (trendNumber < 0) {
                            presenceMessageIcon = "\uD83D\uDCC9";
                        } else {
                            presenceMessageIcon = "\uD83D\uDCC8";
                        }
                        jda.getPresence().setActivity(Activity.playing("90D | "+presenceMessageIcon+" "+ formatter.format(trendNumber)+"%"));
                        break;
                }
            }
        }, 0, (60 * 1000) * intervalMins);
    }

    public static JSONObject getQuote() throws IOException {
        URL url = new URL("https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest?symbol="+coinToken+"&convert=USD");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestProperty("X-CMC_PRO_API_KEY", coinmarketcapApiKey);
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        if(content.toString().contains("status")) {
            JSONObject jsonObject = new JSONObject(content.toString());
            JSONObject dataObject = (JSONObject)jsonObject.get("data");
            JSONObject coinObject = (JSONObject)dataObject.get(coinToken);
            JSONObject quoteObject = (JSONObject)coinObject.get("quote");
            JSONObject usdObject = (JSONObject)quoteObject.get("USD");
            return usdObject;
        }
        return new JSONObject();
    }
}
