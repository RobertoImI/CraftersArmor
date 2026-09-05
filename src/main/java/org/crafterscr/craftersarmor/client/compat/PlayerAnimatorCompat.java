package org.crafterscr.craftersarmor.client.compat;

import net.minecraft.world.entity.Entity;

import java.lang.reflect.Method;

/**
 * Optional bridge to PlayerAnimator/Emotecraft.
 *
 * <p>Crafters Armor must keep working when Emotecraft is not installed, so this
 * class deliberately uses reflection instead of linking PlayerAnimator as a hard
 * dependency. When PlayerAnimator is present we read the final bend axis/value
 * that it is already applying to the player model.</p>
 */
public final class PlayerAnimatorCompat {

    private static volatile boolean initialized;
    private static boolean available;

    private static Class<?> animatedPlayerClass;
    private static Method getAnimationMethod;
    private static Method isActiveMethod;
    private static Method getBendMethod;
    private static Method pairGetLeftMethod;
    private static Method pairGetRightMethod;

    private PlayerAnimatorCompat() {
    }

    public static BendPose getBend(Entity entity, String partName) {
        if (entity == null || partName == null) {
            return BendPose.NONE;
        }

        try {
            ensureInitialized(entity.getClass().getClassLoader());

            if (!available || !animatedPlayerClass.isInstance(entity)) {
                return BendPose.NONE;
            }

            Object animation = getAnimationMethod.invoke(entity);
            if (animation == null) {
                return BendPose.NONE;
            }

            if (isActiveMethod == null) {
                isActiveMethod = animation.getClass().getMethod("isActive");
            }

            if (!(boolean) isActiveMethod.invoke(animation)) {
                return BendPose.NONE;
            }

            if (getBendMethod == null) {
                getBendMethod = animation.getClass().getMethod("getBend", String.class);
            }

            Object pair = getBendMethod.invoke(animation, partName);
            if (pair == null) {
                return BendPose.NONE;
            }

            if (pairGetLeftMethod == null || pairGetRightMethod == null) {
                pairGetLeftMethod = pair.getClass().getMethod("getLeft");
                pairGetRightMethod = pair.getClass().getMethod("getRight");
            }

            float axis = ((Number) pairGetLeftMethod.invoke(pair)).floatValue();
            float angle = ((Number) pairGetRightMethod.invoke(pair)).floatValue();

            if (!Float.isFinite(axis) || !Float.isFinite(angle) || Math.abs(angle) < 0.0001F) {
                return BendPose.NONE;
            }

            return new BendPose(axis, angle);
        }
        catch (Throwable ignored) {
            // PlayerAnimator is optional. A changed/absent API must never break
            // normal armor rendering.
            return BendPose.NONE;
        }
    }

    private static void ensureInitialized(ClassLoader classLoader) {
        if (initialized) {
            return;
        }

        synchronized (PlayerAnimatorCompat.class) {
            if (initialized) {
                return;
            }

            initialized = true;

            try {
                animatedPlayerClass = Class.forName(
                        "dev.kosmx.playerAnim.impl.IAnimatedPlayer",
                        false,
                        classLoader
                );

                getAnimationMethod = animatedPlayerClass.getMethod(
                        "playerAnimator_getAnimation"
                );

                available = true;
            }
            catch (Throwable ignored) {
                available = false;
            }
        }
    }

    /**
     * PlayerAnimator stores bend axis and bend amount in radians.
     */
    public record BendPose(float axis, float angle) {
        public static final BendPose NONE = new BendPose(0.0F, 0.0F);

        public boolean active() {
            return Math.abs(this.angle) >= 0.0001F;
        }
    }
}
