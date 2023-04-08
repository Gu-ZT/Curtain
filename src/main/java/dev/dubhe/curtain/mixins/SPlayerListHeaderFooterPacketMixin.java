package dev.dubhe.curtain.mixins;

import net.minecraft.network.play.server.SPlayerListHeaderFooterPacket;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPlayerListHeaderFooterPacket.class)
public interface SPlayerListHeaderFooterPacketMixin {
    @Accessor("header")
    void setHeader(ITextComponent text);

    @Accessor("footer")
    void setFooter(ITextComponent text);
}
