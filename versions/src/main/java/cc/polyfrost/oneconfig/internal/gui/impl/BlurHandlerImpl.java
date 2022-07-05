package cc.polyfrost.oneconfig.internal.gui.impl;

import cc.polyfrost.oneconfig.events.event.RenderEvent;
import cc.polyfrost.oneconfig.events.event.ScreenOpenEvent;
import cc.polyfrost.oneconfig.events.event.Stage;
import cc.polyfrost.oneconfig.gui.OneConfigGui;
import cc.polyfrost.oneconfig.internal.config.Preferences;
import cc.polyfrost.oneconfig.internal.gui.BlurHandler;
import cc.polyfrost.oneconfig.internal.mixin.ShaderGroupAccessor;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.libs.universal.UScreen;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * An implementation of the BlurMC mod by tterrag1098.
 * <p>
 * For the original source see <a href="https://github.com/tterrag1098/Blur/blob/1.8.9/src/main/java/com/tterrag/blur/Blur.java">...</a>
 * For the public license, see <a href="https://github.com/tterrag1098/Blur/blob/1.8.9/LICENSE">...</a>
 * <p>
 * License available under <a href="https://github.com/boomboompower/ToggleChat/blob/master/src/main/resources/licenses/BlurMC-License.txt">...</a>
 *
 * @author tterrag1098, boomboompower
 * <p>
 * Taken from ToggleChat
 * <a href="https://github.com/boomboompower/ToggleChat/blob/master/LICENSE">...</a>
 */
public class BlurHandlerImpl implements BlurHandler {
    private final ResourceLocation blurShader = new ResourceLocation("shaders/post/fade_in_blur.json");
    private final Logger logger = LogManager.getLogger("OneConfig - Blur");
    private long start;
    private float progress = 0;

    @Subscribe
    private void onGuiChange(ScreenOpenEvent event) {
        reloadBlur(event.screen);
    }

    @Subscribe
    private void onRenderTick(RenderEvent event) {
        if (event.stage != Stage.END) {
            return;
        }

        // Only blur on our own menus
        if (UScreen.getCurrentScreen() == null) {
            return;
        }

        // Only update the shader if one is active
        if (!isShaderActive()) {
            return;
        }
        if (progress >= 5) return;
        progress = getBlurStrengthProgress();

        // This is hilariously bad, and could cause frame issues on low-end computers.
        // Why is this being computed every tick? Surely there is a better way?
        // This needs to be optimized.
        try {
            final List<Shader> listShaders = ((ShaderGroupAccessor) UMinecraft.getMinecraft().entityRenderer.getShaderGroup()).getListShaders();

            // Should not happen. Something bad happened.
            if (listShaders == null) {
                return;
            }

            // Iterate through the list of shaders.
            for (Shader shader : listShaders) {
                ShaderUniform su = shader.getShaderManager().getShaderUniform("Progress");

                if (su == null) {
                    continue;
                }

                // All this for this.
                su.set(progress);
            }
        } catch (IllegalArgumentException ex) {
            this.logger.error("An error.png occurred while updating OneConfig's blur. Please report this!", ex);
        }
    }

    /**
     * Activates/deactivates the blur in the current world if
     * one of many conditions are met, such as no current other shader
     * is being used, we actually have the blur setting enabled
     */
    public void reloadBlur(Object gui) {
        // Don't do anything if no world is loaded
        if (UMinecraft.getWorld() == null) {
            return;
        }

        // If a shader is not already active and the UI is
        // a one of ours, we should load our own blur!

        if (!isShaderActive() && gui instanceof OneConfigGui && Preferences.enableBlur) {
            UMinecraft.getMinecraft().entityRenderer.loadShader(this.blurShader);

            this.start = System.currentTimeMillis();
            this.progress = 0;

            // If a shader is active and the incoming UI is null or we have blur disabled, stop using the shader.
        } else if (isShaderActive() && (gui == null || !Preferences.enableBlur)) {
            String name = UMinecraft.getMinecraft().entityRenderer.getShaderGroup().getShaderGroupName();

            // Only stop our specific blur ;)
            if (!name.endsWith("fade_in_blur.json")) {
                return;
            }

            UMinecraft.getMinecraft().entityRenderer.stopUseShader();
        }
    }

    /**
     * Returns the strength of the blur as determined by the duration the effect of the blur.
     * <p>
     * The strength of the blur does not go below 5.0F.
     */
    private float getBlurStrengthProgress() {
        return Math.min((System.currentTimeMillis() - this.start) / 50F, 5.0F);
    }

    private boolean isShaderActive() {
        return UMinecraft.getMinecraft().entityRenderer.getShaderGroup() != null
                //#if MC<=11202
                && net.minecraft.client.renderer.OpenGlHelper.shadersSupported
                //#endif
        ;
    }
}
