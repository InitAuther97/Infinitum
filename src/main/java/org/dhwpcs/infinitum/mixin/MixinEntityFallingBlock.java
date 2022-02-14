package org.dhwpcs.infinitum.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import org.dhwpcs.infinitum.Global;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FallingBlockEntity.class)
public abstract class MixinEntityFallingBlock extends Entity {

    public MixinEntityFallingBlock(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Redirect(target = @Desc("tick"),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isRemoved()Z", remap = false))
    public boolean proxyIsRemoved() {
        return !Global.sandDuping && super.isRemoved();
    }
}
