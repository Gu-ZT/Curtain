package dev.dubhe.curtain.mixins.rules.fill_updates;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.tileentity.StructureBlockTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(StructureBlockTileEntity.class)
public abstract class StructureBlockEntityMixin {
    @Redirect(
            method = "loadStructure(Lnet/minecraft/world/server/ServerWorld;ZLnet/minecraft/world/gen/feature/template/Template;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/template/Template;placeInWorldChunk(Lnet/minecraft/world/IServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/template/PlacementSettings;Ljava/util/Random;)V"
            )
    )
    private void onStructurePlace(Template structure, IServerWorld serverWorldAccess, BlockPos pos, PlacementSettings placementData, Random random) {
        if (!CurtainRules.impendingFillSkipUpdates.get()) {
            CurtainRules.impendingFillSkipUpdates.set(true);
        }
        try {
            structure.placeInWorld(serverWorldAccess, pos, placementData, random);
        } finally {
            CurtainRules.impendingFillSkipUpdates.set(false);
        }
    }
}
