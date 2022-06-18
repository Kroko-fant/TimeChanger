package eu.midnightdust.timechanger.mixin;

import eu.midnightdust.timechanger.config.TimeChangerConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)

public abstract class MixinClientWorld {

    @Shadow @Final private ClientWorld.Properties clientWorldProperties;
    @Shadow @Final private MinecraftClient client;

    @Inject(at = @At("TAIL"), method = "setTimeOfDay", cancellable = true)
    @Environment(EnvType.CLIENT)
    public void setTimeOfDay(long time, CallbackInfo ci) {
        if (client.getCurrentServerEntry() != null) {
            if (TimeChangerConfig.custom_time >= 0 && TimeChangerConfig.allowlist.isEmpty()) {
                this.clientWorldProperties.setTimeOfDay(TimeChangerConfig.custom_time);
            } else if (TimeChangerConfig.custom_time >= 0 && client.getCurrentServerEntry().address != null) {
                if (!TimeChangerConfig.blocklist && TimeChangerConfig.allowlist.contains(client.getCurrentServerEntry().address)) {
                    this.clientWorldProperties.setTimeOfDay(TimeChangerConfig.custom_time);
                } else if (TimeChangerConfig.blocklist && !TimeChangerConfig.allowlist.contains(client.getCurrentServerEntry().address)) {
                    this.clientWorldProperties.setTimeOfDay(TimeChangerConfig.custom_time);
                }
                else {ci.cancel();}
            }
            else {ci.cancel();}
        }
        else {ci.cancel();}
    }
}
