package dev.dubhe.curtain.utils;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.concurrent.TickDelayedTask;

/**
 * A few helpful methods to work with settings and commands.
 * <p>
 * This is not any kind of API, but it's unlikely to change
 */
public final class CommandHelper {
    private CommandHelper() {
    }

    /**
     * Notifies all players that the commands changed by resending the command tree.
     */
    public static void notifyPlayersCommandsChanged(MinecraftServer server) {
        if (server == null || server.getPlayerList().getPlayers().isEmpty()) {
            return;
        }
        server.tell(new TickDelayedTask(server.getTickCount(), () ->
        {
            try {
                for (ServerPlayerEntity player : server.getPlayerList().getPlayers()) {
                    server.getCommands().sendCommands(player);
                }
            } catch (NullPointerException ignored) {
            }
        }));
    }

    /**
     * Whether the given source has enough permission level to run a command that requires the given commandLevel
     */
    public static boolean canUseCommand(CommandSource source, Object commandLevel) {
        if (commandLevel instanceof Boolean) return (Boolean) commandLevel;
        String commandLevelString = commandLevel.toString();
        // typical for other cheaty commands
        //"false" or default
        switch (commandLevelString) {
            case "true":
                return true;
            case "ops":
                return source.hasPermission(2);
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
                return source.hasPermission(Integer.parseInt(commandLevelString));
            default:
                return false;
        }
    }
}
