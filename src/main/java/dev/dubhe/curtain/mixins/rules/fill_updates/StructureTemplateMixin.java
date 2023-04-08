package dev.dubhe.curtain.mixins.rules.fill_updates;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Template.class)
public abstract class StructureTemplateMixin {
    @Redirect(
            method = "placeInWorld(Lnet/minecraft/world/IServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/template/PlacementSettings;Ljava/util/Random;I)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/IServerWorld;blockUpdated(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"
            )
    )
    private void skipUpdateNeighbours(IServerWorld serverWorldAccessor, BlockPos blockPos, Block block) {
        if (!CurtainRules.impendingFillSkipUpdates.get()) {
            serverWorldAccessor.blockUpdated(blockPos, block);
        }
    }

    @Redirect(
            method = "placeInWorld(Lnet/minecraft/world/IServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/template/PlacementSettings;Ljava/util/Random;I)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/template/PlacementSettings;getKnownShape()Z"
            )
    )
    private boolean skipPostprocess(PlacementSettings structurePlacementData) {
        return structurePlacementData.getKnownShape() || CurtainRules.impendingFillSkipUpdates.get();
    }
}
