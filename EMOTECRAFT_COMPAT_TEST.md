# Emotecraft compatibility test branch

This branch tests **CraftersArmor + Emotecraft / Player Animator** on Minecraft 1.21.1 without splitting the visible armor into rigid pieces.

## Current approach

- The coat and shoe geo models are restored to the exact geometry from master.
- No forearm or lower-leg bones are added.
- During an active bend only, the renderer subdivides the original GeckoLib faces for that frame.
- Those temporary vertices are deformed using Bendy-lib-style bend math.
- The original UV layout is interpolated across the temporary vertices, so no texture remap is required.
- Arm bends deform the original sleeve itself.
- Leg bends move/deform the original shoes around the normal humanoid knee.
- Torso/body bends deform the coat body.
- Head and arms receive Player Animator's body-bend matrix, which GeoArmorRenderer normally misses.
- Animated ModelPart scale is copied to the GeckoLib head/body/arms/legs.
- Clan hats and the leader crown now use the same compatibility renderer.
- Emotecraft / Player Animator is still optional and is accessed through reflection.

## What to test

1. First launch without playing an emote. All armor should look exactly like master.
2. Test clan coat and leader coat with:
   - strong elbow bend,
   - crossed arms,
   - arm raised above the head,
   - sideways elbow bend.
3. Test shoes with:
   - kneeling,
   - sitting,
   - one leg raised,
   - strong knee bend.
4. Test every clan hat and the leader crown with:
   - torso bent forward,
   - torso bent backward,
   - head looking/rotating strongly,
   - emotes that move both torso and head.
5. Watch especially for:
   - hat/crown separation,
   - wrong bend direction,
   - sleeve texture stretching,
   - lighting seams,
   - shoes bending around the wrong point.

## Important

This version intentionally does **not** solve bends by visually cutting the sleeve in two. The source sleeve remains one GeckoLib cube/model piece; the bend exists only as render-time vertex deformation.
