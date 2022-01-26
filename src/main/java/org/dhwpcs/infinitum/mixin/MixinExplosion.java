package org.dhwpcs.infinitum.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import org.dhwpcs.infinitum.GlobalConfig;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Explosion.class)
public abstract class MixinExplosion {

    @Redirect(method = "explode()V",
            slice = @Slice(from = @At(
                    value = "FIELD",
                    target = "Lorg/bukkit/craftbukkit/event/CraftEventFactory;entityDamage:Lnet/minecraft/world/entity/Entity;",
                    opcode = Opcodes.PUTSTATIC,
                    ordinal = 1,
                    remap = false
            )), at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/Entity;forceExplosionKnockback:Z",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0,
                    remap = false
            )
    )
    private boolean forceExplosionKnockback(Entity entity) {
        return GlobalConfig.fixExplosion || entity.forceExplosionKnockback;
    }
}
