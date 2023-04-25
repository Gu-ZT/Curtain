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
        if ("true".equals(commandLevelString)) return true;
        else if ("ops".equals(commandLevelString)) return source.hasPermission(2);
        else if ("0".equals(commandLevelString) ||
                "1".equals(commandLevelString) ||
                "2".equals(commandLevelString) ||
                "3".equals(commandLevelString) ||
                "4".equals(commandLevelString)) return source.hasPermission(Integer.parseInt(commandLevelString));
        else return false;
    }
}
