package dev.kila.bundleui.mixin;

import net.minecraft.component.type.BundleContentsComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleContentsComponent.class)
abstract class BundleContentsComponentMixin {
    @Shadow
    public abstract int size();

    @Inject(method = "getNumberOfStacksShown", at = @At("HEAD"), cancellable = true)
    private void bundleui$showEveryStack(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(this.size());
    }
}
