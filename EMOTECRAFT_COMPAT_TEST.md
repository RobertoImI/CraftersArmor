# Emotecraft compatibility test branch

This branch is an experimental compatibility layer for **CraftersArmor + Emotecraft / Player Animator** on Minecraft 1.21.1.

## What this patch changes

- Keeps Emotecraft and Player Animator optional. CraftersArmor still loads without them.
- Keeps GeckoLib's normal head/body/limb pose synchronization.
- Reads the active Player Animator bend used by Emotecraft.
- Adds articulated lower-arm bones to both coat models.
- Adds articulated lower-leg bones to the shoes model.
- Preserves the existing coat textures by splitting only the sleeve geometry and mapping the original UV region onto the two segments.
- Does not change the hat or crown geometry because the head does not use Bendy-lib limb bending; GeckoLib already follows the animated head rotation.

## Suggested test

Use the current CraftersArmor dependencies plus an Emotecraft build for Minecraft 1.21.1.

1. Equip a normal clan coat and shoes.
2. Run emotes with strong elbow bends in several directions.
3. Check both arms in first and third person.
4. Run emotes with crouching/kneeling poses and check both shoes.
5. Repeat with the leader coat.
6. Test normal walking, sprinting, sneaking and no emote to confirm the appearance is unchanged.

## Current experimental limitation

Player Animator/Bendy-lib can deform a vanilla cuboid continuously. GeckoLib does not expose that same Bendy-lib cuboid mutator, so this patch reproduces the bend as an articulated lower limb. It should follow elbows and knees much better than the unpatched renderer, but extreme bends may still show a small seam at the joint.
