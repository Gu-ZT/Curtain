package dev.dubhe.curtain.utils;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Messenger {
    public static final Logger LOG = LogManager.getLogger("Messaging System");

    private static final Pattern colorExtract = Pattern.compile("#([0-9a-fA-F]{6})");

    public enum CarpetFormatting {
        ITALIC('i', (s, f) -> s.withItalic(true)),
        STRIKE('s', (s, f) -> s.withColor(TextFormatting.STRIKETHROUGH)),
        UNDERLINE('u', (s, f) -> s.withColor(TextFormatting.UNDERLINE)),
        BOLD('b', (s, f) -> s.withBold(true)),
        OBFUSCATE('o', (s, f) -> s.withColor(TextFormatting.OBFUSCATED)),

        WHITE('w', (s, f) -> s.withColor(TextFormatting.WHITE)),
        YELLOW('y', (s, f) -> s.withColor(TextFormatting.YELLOW)),
        LIGHT_PURPLE('m', (s, f) -> s.withColor(TextFormatting.LIGHT_PURPLE)), // magenta
        RED('r', (s, f) -> s.withColor(TextFormatting.RED)),
        AQUA('c', (s, f) -> s.withColor(TextFormatting.AQUA)), // cyan
        GREEN('l', (s, f) -> s.withColor(TextFormatting.GREEN)), // lime
        BLUE('t', (s, f) -> s.withColor(TextFormatting.BLUE)), // light blue, teal
        DARK_GRAY('f', (s, f) -> s.withColor(TextFormatting.DARK_GRAY)),
        GRAY('g', (s, f) -> s.withColor(TextFormatting.GRAY)),
        GOLD('d', (s, f) -> s.withColor(TextFormatting.GOLD)),
        DARK_PURPLE('p', (s, f) -> s.withColor(TextFormatting.DARK_PURPLE)), // purple
        DARK_RED('n', (s, f) -> s.withColor(TextFormatting.DARK_RED)),  // brown
        DARK_AQUA('q', (s, f) -> s.withColor(TextFormatting.DARK_AQUA)),
        DARK_GREEN('e', (s, f) -> s.withColor(TextFormatting.DARK_GREEN)),
        DARK_BLUE('v', (s, f) -> s.withColor(TextFormatting.DARK_BLUE)), // navy
        BLACK('k', (s, f) -> s.withColor(TextFormatting.BLACK)),

        COLOR('#', (s, f) -> {
            Color color = Color.parseColor("#" + f);
            return color == null ? s : s.withColor(color);
        }, s -> {
            Matcher m = colorExtract.matcher(s);
            return m.find() ? m.group(1) : null;
        }),
        ;

        public char code;
        public BiFunction<Style, String, Style> applier;
        public Function<String, String> container;

        CarpetFormatting(char code, BiFunction<Style, String, Style> applier) {
            this(code, applier, s -> s.indexOf(code) >= 0 ? Character.toString(code) : null);
        }

        CarpetFormatting(char code, BiFunction<Style, String, Style> applier, Function<String, String> container) {
            this.code = code;
            this.applier = applier;
            this.container = container;
        }

        public Style apply(String format, Style previous) {
            String fmt;
            if ((fmt = container.apply(format)) != null) return applier.apply(previous, fmt);
            return previous;
        }
    }

    ;

    public static Style parseStyle(String style) {
        Style myStyle = Style.EMPTY.withColor(TextFormatting.WHITE);
        for (CarpetFormatting cf : CarpetFormatting.values()) myStyle = cf.apply(style, myStyle);
        return myStyle;
    }

    public static String heatmap_color(double actual, double reference) {
        String color = "g";
        if (actual >= 0.0D) color = "e";
        if (actual > 0.5D * reference) color = "y";
        if (actual > 0.8D * reference) color = "r";
        if (actual > reference) color = "m";
        return color;
    }

    public static String creatureTypeColor(EntityClassification type) {
        switch (type) {
            case MONSTER:
                return "n";
            case CREATURE:
                return "e";
            case AMBIENT:
                return "f";
            case WATER_CREATURE:
                return "v";
            case WATER_AMBIENT:
                return "q";
        }
        return "w";
    }

    private static IFormattableTextComponent _getChatComponentFromDesc(String message, IFormattableTextComponent previous_message) {
        if (message.equalsIgnoreCase("")) {
            return new StringTextComponent("");
        }
        if (Character.isWhitespace(message.charAt(0))) {
            message = "w" + message;
        }
        int limit = message.indexOf(' ');
        String desc = message;
        String str = "";
        if (limit >= 0) {
            desc = message.substring(0, limit);
            str = message.substring(limit + 1);
        }
        if (desc.charAt(0) == '/') // deprecated
        {
            if (previous_message != null)
                previous_message.setStyle(
                        previous_message.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, message))
                );
            return previous_message;
        }
        if (desc.charAt(0) == '?') {
            if (previous_message != null)
                previous_message.setStyle(
                        previous_message.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, message.substring(1)))
                );
            return previous_message;
        }
        if (desc.charAt(0) == '!') {
            if (previous_message != null)
                previous_message.setStyle(
                        previous_message.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, message.substring(1)))
                );
            return previous_message;
        }
        if (desc.charAt(0) == '^') {
            if (previous_message != null)
                previous_message.setStyle(
                        previous_message.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, c(message.substring(1))))
                );
            return previous_message;
        }
        StringTextComponent txt = new StringTextComponent(str);
        txt.setStyle(parseStyle(desc));
        return txt;
    }

    public static IFormattableTextComponent tp(String desc, Vector3d pos) {
        return tp(desc, pos.x, pos.y, pos.z);
    }

    public static IFormattableTextComponent tp(String desc, BlockPos pos) {
        return tp(desc, pos.getX(), pos.getY(), pos.getZ());
    }

    public static IFormattableTextComponent tp(String desc, double x, double y, double z) {
        return tp(desc, (float) x, (float) y, (float) z);
    }

    public static IFormattableTextComponent tp(String desc, float x, float y, float z) {
        return _getCoordsTextComponent(desc, x, y, z, false);
    }

    public static IFormattableTextComponent tp(String desc, int x, int y, int z) {
        return _getCoordsTextComponent(desc, (float) x, (float) y, (float) z, true);
    }

    /// to be continued
    public static IFormattableTextComponent dbl(String style, double double_value) {
        return c(String.format("%s %.1f", style, double_value), String.format("^w %f", double_value));
    }

    public static IFormattableTextComponent dbls(String style, double... doubles) {
        StringBuilder str = new StringBuilder(style + " [ ");
        String prefix = "";
        for (double dbl : doubles) {
            str.append(String.format("%s%.1f", prefix, dbl));
            prefix = ", ";
        }
        str.append(" ]");
        return c(str.toString());
    }

    public static IFormattableTextComponent dblf(String style, double... doubles) {
        StringBuilder str = new StringBuilder(style + " [ ");
        String prefix = "";
        for (double dbl : doubles) {
            str.append(String.format("%s%f", prefix, dbl));
            prefix = ", ";
        }
        str.append(" ]");
        return c(str.toString());
    }

    public static IFormattableTextComponent dblt(String style, double... doubles) {
        List<Object> components = new ArrayList<>();
        components.add(style + " [ ");
        String prefix = "";
        for (double dbl : doubles) {

            components.add(String.format("%s %s%.1f", style, prefix, dbl));
            components.add("?" + dbl);
            components.add("^w " + dbl);
            prefix = ", ";
        }
        //components.remove(components.size()-1);
        components.add(style + "  ]");
        return c(components.toArray(new Object[0]));
    }

    private static IFormattableTextComponent _getCoordsTextComponent(String style, float x, float y, float z, boolean isInt) {
        String text;
        String command;
        if (isInt) {
            text = String.format("%s [ %d, %d, %d ]", style, (int) x, (int) y, (int) z);
            command = String.format("!/tp %d %d %d", (int) x, (int) y, (int) z);
        } else {
            text = String.format("%s [ %.1f, %.1f, %.1f]", style, x, y, z);
            command = String.format("!/tp %.3f %.3f %.3f", x, y, z);
        }
        return c(text, command);
    }

    //message source
    public static void m(CommandSource source, Object... fields) {
        if (source != null)
            source.sendSuccess(Messenger.c(fields), source.getServer() != null && source.getServer().getLevel(World.OVERWORLD) != null); //OW
    }

    public static void m(PlayerEntity player, Object... fields) {
        player.sendMessage(Messenger.c(fields), Util.NIL_UUID);
    }

    /*
    composes single line, multicomponent message, and returns as one chat messagge
     */
    public static IFormattableTextComponent c(Object... fields) {
        IFormattableTextComponent message = new StringTextComponent("");
        IFormattableTextComponent previous_component = null;
        for (Object o : fields) {
            if (o instanceof IFormattableTextComponent) {
                message.append((IFormattableTextComponent) o);
                previous_component = (IFormattableTextComponent) o;
                continue;
            }
            String txt = o.toString();
            IFormattableTextComponent comp = _getChatComponentFromDesc(txt, previous_component);
            if (comp != previous_component) message.append(comp);
            previous_component = comp;
        }
        return message;
    }

    //simple text

    public static IFormattableTextComponent s(String text) {
        return s(text, "");
    }

    public static IFormattableTextComponent s(String text, String style) {
        IFormattableTextComponent message = new StringTextComponent(text);
        message.setStyle(parseStyle(style));
        return message;
    }


    public static void send(PlayerEntity player, Collection<IFormattableTextComponent> lines) {
        lines.forEach(message -> player.sendMessage(message, Util.NIL_UUID));
    }

    public static void send(CommandSource source, Collection<IFormattableTextComponent> lines) {
        lines.stream().forEachOrdered((s) -> source.sendSuccess(s, false));
    }


    public static void print_server_message(MinecraftServer server, String message) {
        if (server == null)
            LOG.error("Message not delivered: " + message);
        server.sendMessage(new StringTextComponent(message), Util.NIL_UUID);
        IFormattableTextComponent txt = c("gi " + message);
        for (PlayerEntity entityplayer : server.getPlayerList().getPlayers()) {
            entityplayer.sendMessage(txt, Util.NIL_UUID);
        }
    }

    public static void print_server_message(MinecraftServer server, IFormattableTextComponent message) {
        if (server == null)
            LOG.error("Message not delivered: " + message.getString());
        server.sendMessage(message, Util.NIL_UUID);
        for (PlayerEntity entityplayer : server.getPlayerList().getPlayers()) {
            entityplayer.sendMessage(message, Util.NIL_UUID);
        }
    }
}
