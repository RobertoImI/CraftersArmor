package org.crafterscr.craftersarmor.client.compat;

import net.minecraft.world.entity.Entity;

import java.lang.reflect.Method;

/**
 * Optional bridge to Player Animator, the animation library used by Emotecraft.
 *
 * <p>Reflection keeps Player Animator optional: CraftersArmor still loads
 * normally when Emotecraft / Player Animator is not installed.</p>
 */
public final class PlayerAnimatorCompat {

    private static final Bend ZERO =
            new Bend(0.0F, 0.0F);

    private static volatile boolean initialized;
    private static boolean available;

    private static Class<?> animatedPlayerClass;
    private static Method getAnimationMethod;
    private static Method isActiveMethod;
    private static Method getBendMethod;
    private static Method getLeftMethod;
    private static Method getRightMethod;

    private PlayerAnimatorCompat() {
    }

    public static boolean isActive(Entity entity) {
        if (entity == null || !ensureInitialized()) {
            return false;
        }

        if (!animatedPlayerClass.isInstance(entity)) {
            return false;
        }

        try {
            Object animation =
                    getAnimationMethod.invoke(entity);

            return animation != null
                    && Boolean.TRUE.equals(
                            isActiveMethod.invoke(animation)
                    );
        }
        catch (ReflectiveOperationException
               | LinkageError ignored) {
            return false;
        }
    }

    public static Bend getBend(
            Entity entity,
            String partName
    ) {
        if (entity == null
                || partName == null
                || !ensureInitialized()) {
            return ZERO;
        }

        if (!animatedPlayerClass.isInstance(entity)) {
            return ZERO;
        }

        try {
            Object animation =
                    getAnimationMethod.invoke(entity);

            if (animation == null
                    || !Boolean.TRUE.equals(
                            isActiveMethod.invoke(animation)
                    )) {
                return ZERO;
            }

            Object pair =
                    getBendMethod.invoke(
                            animation,
                            partName
                    );

            if (pair == null) {
                return ZERO;
            }

            Object left =
                    getLeftMethod.invoke(pair);

            Object right =
                    getRightMethod.invoke(pair);

            if (!(left instanceof Number axis)
                    || !(right instanceof Number amount)) {
                return ZERO;
            }

            return new Bend(
                    axis.floatValue(),
                    amount.floatValue()
            );
        }
        catch (ReflectiveOperationException
               | LinkageError ignored) {
            return ZERO;
        }
    }

    public static Bend combine(
            Bend first,
            Bend second
    ) {
        if (first == null) {
            return second == null
                    ? ZERO
                    : second;
        }

        if (second == null) {
            return first;
        }

        return new Bend(
                first.axis + second.axis,
                first.amount + second.amount
        );
    }

    private static boolean ensureInitialized() {
        if (initialized) {
            return available;
        }

        synchronized (PlayerAnimatorCompat.class) {
            if (initialized) {
                return available;
            }

            try {
                ClassLoader loader =
                        PlayerAnimatorCompat.class
                                .getClassLoader();

                animatedPlayerClass = Class.forName(
                        "dev.kosmx.playerAnim.impl.IAnimatedPlayer",
                        false,
                        loader
                );

                Class<?> animationApplierClass =
                        Class.forName(
                                "dev.kosmx.playerAnim.impl.animation.AnimationApplier",
                                false,
                                loader
                        );

                Class<?> pairClass =
                        Class.forName(
                                "dev.kosmx.playerAnim.core.util.Pair",
                                false,
                                loader
                        );

                getAnimationMethod =
                        animatedPlayerClass.getMethod(
                                "playerAnimator_getAnimation"
                        );

                isActiveMethod =
                        animationApplierClass.getMethod(
                                "isActive"
                        );

                getBendMethod =
                        animationApplierClass.getMethod(
                                "getBend",
                                String.class
                        );

                getLeftMethod =
                        pairClass.getMethod("getLeft");

                getRightMethod =
                        pairClass.getMethod("getRight");

                available = true;
            }
            catch (ReflectiveOperationException
                   | LinkageError ignored) {
                available = false;
            }

            initialized = true;
            return available;
        }
    }

    public record Bend(
            float axis,
            float amount
    ) {
        public boolean isActive() {
            return Math.abs(this.amount)
                    >= 1.0E-4F;
        }
    }
}
