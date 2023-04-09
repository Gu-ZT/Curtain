package dev.dubhe.curtain.features.logging;

import dev.dubhe.curtain.Curtain;
import dev.dubhe.curtain.features.logging.builtin.MemoryLogger;
import dev.dubhe.curtain.features.logging.builtin.MobcapsLogger;
import dev.dubhe.curtain.features.logging.builtin.TPSLogger;
import dev.dubhe.curtain.features.logging.helper.ExplosionLogHelper;
import dev.dubhe.curtain.features.logging.helper.TNTLogHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class LoggerManager {
    private static final Map<String, AbstractLogger> registeredLogger = new HashMap<>();
    // Map<playerName, Set<loggerName>>
    private static final Map<String, Set<String>> subscribedPlayer = new HashMap<>();

    public static void ableSendToChat(String loggerName) {
        if (!registeredLogger.containsKey(loggerName)) {
            Curtain.LOGGER.error("Can' t find logger named: {}", loggerName);
            return;
        }
        AbstractLogger logger = registeredLogger.get(loggerName);
        if (logger.getType() != DisplayType.CHAT) {
            Curtain.LOGGER.error("Logger {} not a chat logger", loggerName);
            return;
        }

        for (Map.Entry<String, Set<String>> entry : subscribedPlayer.entrySet()) {
            if (!entry.getValue().contains(loggerName)) {
                continue;
            }
            ServerPlayer player = Curtain.minecraftServer.getPlayerList().getPlayerByName(entry.getKey());
            if (player != null) {
                Component msg = logger.display(player);
                player.sendSystemMessage(msg);
            }
        }
    }

    public static void updateHUD() {
        for (Map.Entry<String, Set<String>> entry : subscribedPlayer.entrySet()) {
            ServerPlayer player = Curtain.minecraftServer.getPlayerList().getPlayerByName(entry.getKey());
            if (player == null) {
                continue;
            }
            MutableComponent msg = Component.empty();
            for (Iterator<String> iterator = entry.getValue().iterator(); iterator.hasNext(); ) {
                String loggerName = iterator.next();
                if (registeredLogger.containsKey(loggerName) && registeredLogger.get(loggerName).getType() == DisplayType.HUD) {
                    msg.append(registeredLogger.get(loggerName).display(player));
                    if (iterator.hasNext()) msg.append("\n");
                }
            }
            player.setTabListFooter(msg);
        }
    }

    public static void registerLogger(AbstractLogger logger) {
        registeredLogger.put(logger.getName(), logger);
    }

    public static void subscribeLogger(String playerName, String loggerName) {
        if (!registeredLogger.containsKey(loggerName)) {
            Curtain.LOGGER.error("Can' t find logger named: {}", loggerName);
            return;
        }
        Set<String> loggerSet;
        if (!subscribedPlayer.containsKey(playerName)) {
            loggerSet = new HashSet<>();
        } else {
            loggerSet = subscribedPlayer.get(playerName);
        }
        loggerSet.add(loggerName);
        subscribedPlayer.put(playerName, loggerSet);

        ServerPlayer player = Curtain.minecraftServer.getPlayerList().getPlayerByName(playerName);
        if (player != null) {
            player.sendSystemMessage(Component.literal("%s subscribed logger %s".formatted(playerName, loggerName))
                    .withStyle(style -> style.withColor(ChatFormatting.GRAY)), false);
        }
    }

    public static void subscribeLogger(String playerName, String[] loggers) {
        Set<String> loggerSet;
        if (!subscribedPlayer.containsKey(playerName)) {
            loggerSet = new HashSet<>();
        } else {
            loggerSet = subscribedPlayer.get(playerName);
        }
        loggerSet.addAll(Arrays.asList(loggers));
        subscribedPlayer.put(playerName, loggerSet);
    }

    public static void unsubscribeLogger(String playerName, String loggerName) {
        if (!registeredLogger.containsKey(loggerName)) {
            Curtain.LOGGER.error("Can' t find logger named: {}", loggerName);
            return;
        }
        Set<String> loggerSet;
        if (!subscribedPlayer.containsKey(playerName)) {
            loggerSet = new HashSet<>();
        } else {
            loggerSet = subscribedPlayer.get(playerName);
        }
        loggerSet.remove(loggerName);
        subscribedPlayer.put(playerName, loggerSet);

        ServerPlayer player = Curtain.minecraftServer.getPlayerList().getPlayerByName(playerName);
        if (player != null) {
            player.sendSystemMessage(Component.literal("%s unsubscribed logger %s".formatted(playerName, loggerName))
                    .withStyle(style -> style.withColor(ChatFormatting.GRAY)), false);
        }
    }

    public static void unsubscribeAllLogger(String playerName) {
        subscribedPlayer.remove(playerName);
    }

    public static boolean isSubscribedLogger(String playerName, String loggerName) {
        if (!registeredLogger.containsKey(loggerName)) {
            Curtain.LOGGER.error("Can' t find logger named: {}", loggerName);
            return false;
        }
        Set<String> loggerSet;
        if (!subscribedPlayer.containsKey(playerName)) {
            loggerSet = new HashSet<>();
        } else {
            loggerSet = subscribedPlayer.get(playerName);
        }
        return loggerSet.contains(loggerName);
    }

    public static boolean hasSubscribedLogger(String playerName) {
        return subscribedPlayer.containsKey(playerName);
    }

    public static void registryBuiltinLogger() {
        registerLogger(new TPSLogger());
        registerLogger(new MobcapsLogger());
        registerLogger(new MemoryLogger());
        registerLogger(new ExplosionLogHelper.ExplosionLogger());
        registerLogger(new TNTLogHelper.TNTLogger());
    }

    public static Set<String> getLoggerSet() {
        return registeredLogger.keySet();
    }
}
