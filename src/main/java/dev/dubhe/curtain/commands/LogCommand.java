package dev.dubhe.curtain.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.logging.LoggerManager;
import dev.dubhe.curtain.utils.CommandHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.SharedSuggestionProvider.suggest;

public class LogCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> literalargumentbuilder = literal("log")
                .requires((player) -> CommandHelper.canUseCommand(player, CurtainRules.commandLog))
                .then(argument("loggerName", StringArgumentType.word())
                        .suggests((context, builder) -> suggest(LoggerManager.getLoggerSet(), builder))
                        .executes(context -> {
                            String loggerName = StringArgumentType.getString(context, "loggerName");
                            ServerPlayer player = context.getSource().getPlayer();
                            if (player == null) {
                                return 0;
                            }
                            if (LoggerManager.isSubscribedLogger(player.getName().getString(), loggerName)) {
                                LoggerManager.unsubscribeLogger(player.getName().getString(), loggerName);
                            } else {
                                LoggerManager.subscribeLogger(player.getName().getString(), loggerName);
                            }
                            return 1;
                        }));
        dispatcher.register(literalargumentbuilder);
    }
}
