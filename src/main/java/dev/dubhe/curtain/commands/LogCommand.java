package dev.dubhe.curtain.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.dubhe.curtain.CurtainRules;
import dev.dubhe.curtain.features.logging.LoggerManager;
import dev.dubhe.curtain.utils.CommandHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;
import static net.minecraft.command.ISuggestionProvider.suggest;

public class LogCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> literalargumentbuilder = literal("log")
                .requires((player) -> CommandHelper.canUseCommand(player, CurtainRules.commandLog))
                .then(argument("loggerName", StringArgumentType.word())
                        .suggests((context, builder) -> suggest(LoggerManager.getLoggerSet(), builder))
                        .executes(context -> {
                            String loggerName = StringArgumentType.getString(context, "loggerName");
                            ServerPlayerEntity player = context.getSource().getPlayerOrException();
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
