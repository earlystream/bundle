package dev.kila.bundleui.mixin;

import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import net.minecraft.component.type.BundleContentsComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleTooltipComponent.class)
abstract class BundleTooltipComponentMixin {
    @Shadow
    @Final
    private BundleContentsComponent bundleContents;

    @Shadow
    private static boolean shouldDrawExtraItemsCount(boolean hiddenItemsExist, int column, int row) {
        throw new AssertionError();
    }

    @Shadow
    protected abstract int getNumVisibleSlots();

    @Inject(method = "getNumVisibleSlots", at = @At("HEAD"), cancellable = true)
    private void bundleui$showAllVisibleSlots(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(this.bundleContents.size());
    }

    @Redirect(
        method = "drawNonEmptyTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/tooltip/BundleTooltipComponent;shouldDrawExtraItemsCount(ZII)Z"
        )
    )
    private boolean bundleui$skipOverflowMarker(boolean hiddenItemsExist, int column, int row) {
        if (this.bundleContents.size() <= this.getNumVisibleSlots()) {
            return false;
        }

        return shouldDrawExtraItemsCount(hiddenItemsExist, column, row);
    }
}
