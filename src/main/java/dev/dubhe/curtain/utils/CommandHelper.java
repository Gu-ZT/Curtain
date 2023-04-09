package dev.dubhe.curtain.utils;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerPlayer;

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
        server.tell(new TickTask(server.getTickCount(), () ->
        {
            try {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    server.getCommands().sendCommands(player);
                }
            } catch (NullPointerException ignored) {
            }
        }));
    }

    /**
     * Whether the given source has enough permission level to run a command that requires the given commandLevel
     */
    public static boolean canUseCommand(CommandSourceStack source, Object commandLevel) {
        if (commandLevel instanceof Boolean) return (Boolean) commandLevel;
        String commandLevelString = commandLevel.toString();
        return switch (commandLevelString) {
            case "true" -> true;
            case "ops" -> source.hasPermission(2); // typical for other cheaty commands
            case "0", "1", "2", "3", "4" -> source.hasPermission(Integer.parseInt(commandLevelString));
            default -> false; //"false" or default
        };
    }
}
