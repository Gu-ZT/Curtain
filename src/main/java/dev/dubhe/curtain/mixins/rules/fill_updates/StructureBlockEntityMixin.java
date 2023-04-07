package dev.dubhe.curtain.mixins.rules.fill_updates;

import dev.dubhe.curtain.CurtainRules;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(StructureBlockEntity.class)
public abstract class StructureBlockEntityMixin {
    @Redirect(
            method = "loadStructure(Lnet/minecraft/server/level/ServerLevel;ZLnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;placeInWorld(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Ljava/util/Random;I)Z"
            )
    )
    private boolean onStructurePlace(StructureTemplate structure, ServerLevelAccessor serverWorldAccess, BlockPos pos, BlockPos blockPos, StructurePlaceSettings placementData, Random random, int i) {
        if (!CurtainRules.impendingFillSkipUpdates.get()) {
            CurtainRules.impendingFillSkipUpdates.set(true);
        }
        try {
            return structure.placeInWorld(serverWorldAccess, pos, blockPos, placementData, random, i);
        } finally {
            CurtainRules.impendingFillSkipUpdates.set(false);
        }
    }
}
